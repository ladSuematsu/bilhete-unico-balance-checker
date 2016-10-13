package com.ladsoft.bilheteunicobalancechecker.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ladsoft.bilheteunicobalancechecker.R;
import com.ladsoft.bilheteunicobalancechecker.databinding.ActivitySplashBinding;

import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_TIMEOUT = 3000L;

    private ActivitySplashBinding binding;
    private Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        uiHandler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleSession();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }

    @Override
    protected void onPause() {
        super.onPause();

        uiHandler.removeCallbacksAndMessages(null);
    }

    private void handleSession() {
        if(isFinishing()) {
           return;
        }

        Intent intent = new Intent(this, CurrentBalanceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
