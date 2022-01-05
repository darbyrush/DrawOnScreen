package com.trucsoftware.drawonscreen;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Random;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

/**
 * App crashes on android 8+, fix it
 */
public class DrawingService extends Service {

  Random random = new Random();
  int screenHeight;
  int screenWidth;
  int drawableHeight;
  Handler handler;

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    startForeground(1, getOnGoingNotification());
    getScreenDimensions();
    drawingLoop();
  }

  private void drawingLoop() {
      if (handler == null) {
        handler = new Handler(getMainLooper());
      }
      handler.postDelayed(() -> {
        drawingLoop();
        drawSomething();
      }, 0);
  }

  private void getScreenDimensions() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    screenHeight = displayMetrics.heightPixels;
    screenWidth = displayMetrics.widthPixels;
  }

  private Notification getOnGoingNotification() {
    return new NotificationCompat.Builder(getApplicationContext())
        .setContentTitle(getString(R.string.ongoing_notification_title))
        .setContentText(getString(R.string.ongoing_notification_text))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setSmallIcon(R.drawable.ic_android)
        .setOngoing(true)
        .build();
  }

  private void drawSomething() {
    final Drawable drawable = getDrawable(R.drawable.ic_android);
    drawableHeight = drawable.getIntrinsicHeight();
    final int color = getRandomColor();
    drawable.setTint(color);
    WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
    ImageView imageView = new ImageView(getApplicationContext());
    imageView.setImageDrawable(drawable);
    imageView.setBackgroundColor(getComplimentaryColor(color));
    wm.addView(imageView, getLayoutParams());
  }

  private int getRandomColor() {
    return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
  }

  private int getComplimentaryColor(int color) {
    int r = (color >> 16) & 0xff;
    int g = (color >> 8) & 0xff;
    int b = color & 0xff;
    return Color.rgb(255 - r, 255 - g, 255 - b);
  }

  private LayoutParams getLayoutParams() {
    LayoutParams params = new WindowManager.LayoutParams(
          WRAP_CONTENT, WRAP_CONTENT,
          TYPE_SYSTEM_ALERT,
          FLAG_LAYOUT_NO_LIMITS | FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
    params.x = random.nextInt(screenWidth + 400) - screenWidth /2 - 200;
    params.y = random.nextInt(screenHeight + 400) - screenHeight /2 - 200;
    return params;
  }


}
