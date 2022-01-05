package com.trucsoftware.drawonscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onClickButton(View view) {
      finish();
      startService();
  }

  private void startService() {
    Intent i = new Intent(getApplicationContext(), DrawingService.class);
    startService(i);
  }
}