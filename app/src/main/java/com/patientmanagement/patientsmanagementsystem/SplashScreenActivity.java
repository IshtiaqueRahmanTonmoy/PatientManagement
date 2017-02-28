package com.patientmanagement.patientsmanagementsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import patientsmanagement.patientmanagement.patientsmanagementsystem.R;

/**
 * Created by Nils
 */
public class SplashScreenActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        Thread backgroundThread = new Thread() {
            public void run() {
                try {
                    sleep(4 * 1000);           // Thread sleep for 4 seconds
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();                 // to finish this activity.
                } catch (Exception e) {

                }
            }
        };
        backgroundThread.start();
    }
}
