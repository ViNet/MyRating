package com.example.vit.myrating;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SplashActivity extends ActionBarActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            // if already exist some cookies then start main activity
                if(SharedPreferenceHelper.isCookieExist(getBaseContext())){
                    startActivity(
                            new Intent(SplashActivity.this, MainActivity.class).
                                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                    );
                } else{
                    // there are no logged users, show login activity
                    startActivity(
                            new Intent(SplashActivity.this, LoginActivity.class).
                                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                    );
                }
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
