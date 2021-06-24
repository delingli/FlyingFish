package com.itc.screen_saver;

import android.os.Bundle;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ScreenSaverActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_activity_acreensaver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}




