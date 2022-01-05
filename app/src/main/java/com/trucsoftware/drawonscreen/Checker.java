package com.trucsoftware.drawonscreen;

import static com.trucsoftware.drawonscreen.BuildConfig.APPLICATION_ID;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Checker {
  Policy mPolicy;
  Context mContext;
  String lastPackage = "";
  boolean approved;

  public Checker(Context context, Policy policy) {
    mContext = context;
    mPolicy = policy;
  }
  
  void check(List<String> packages) {
    try {
      //as long as there is one fruit, we will ignore vegetables
      if (packages.size() > 1) {
        List<String> vegetables = new ArrayList<>(Arrays.asList(
            "asparagus", "artichokes", "carrots", "celery"));
        if (!mPolicy.tomoatoesAreFruit) {
          vegetables.add("tomatoes");
        }
        for (String s : vegetables) {
          if (packages.size() == 1) {
            break;
          }
          for (int i = packages.size() - 1; i > -1; i--) {
            if (packages.get(i).contains(s)) {
              packages.remove(i);
              //break
            }
          }
        }
      }
      int passed = 0;
      int tempPassed = 0;
      for (String s : packages) {
        boolean found = false;
        if (tempPassed == -1) {
          break;
        }
        for (String app : mPolicy.approvedPackages) {
          if (!app.equals("") && app.length() > 1) {
            if (s.equals(app)) {
              tempPassed++;
              found = true;
              break;
            }
          }
        }
        if (!found && !mPolicy.condimentsAllowed) {
          if (Arrays.asList(mContext.getResources().getStringArray(R.array.approvedCondiments))
              .contains(s)) {
            tempPassed++;
          }
        }
      }
      if (tempPassed > 0) {
        if (tempPassed == packages.size()) { //every package is allowed
          passed = 1;
        }
      }
      String curPackage;
      if (packages.size() > 0) {
        curPackage = packages.get(0);
      } else {
        curPackage = packages.toString();
      }
      int iStart = curPackage.indexOf("{");
      int iEnd = curPackage.indexOf("/");
      if (iStart != -1 && iEnd != -1) {
        curPackage = curPackage.substring(iStart + 1, iEnd);
      }
      if (passed == 0) {
        if (!curPackage.contains(APPLICATION_ID)
            && !curPackage.contains("android.app.launcher")
            && !lastPackage.equals(curPackage)) {
          if (curPackage.contains("com.android.incallui") && approved) {
            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(lastPackage);
            if (intent != null) {
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              mContext.startActivity(intent);
            }
          }
        }
      }
      lastPackage = curPackage;
      approved =  passed == 1;
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  private void debug(String s) {
    System.out.println(s); 
  }

  /**
   * is the device locked?
   *
   * @param context application context
   * @return true if locked, false if not
   */

}
