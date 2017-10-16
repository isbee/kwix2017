package com.hardcopy.btctemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hardcopy.btctemplate.SplashView.ISplashListener;

public class SplashActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashView splash = (SplashView) findViewById(R.id.splash_view);
        splash.splashAndDisappear(new ISplashListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onUpdate(float completionFraction) {

            }

            @Override
            public void onEnd() {
                startActivity(new Intent(getApplication(), MainActivity.class));
                SplashActivity.this.finish();
            }
        });

    }

}