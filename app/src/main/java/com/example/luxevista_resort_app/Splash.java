package com.example.luxevista_resort_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    private final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent to start Welcome_Screen Activity
                // Assuming Welcome_Screen.class is the correct name of your target Activity
                Intent mainIntent = new Intent(Splash.this, Welcome_Screen.class);
                startActivity(mainIntent);

                // Close Splash Activity so the user can't go back to it
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}