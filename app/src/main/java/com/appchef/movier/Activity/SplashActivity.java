package com.appchef.movier.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.appchef.movier.R;
import com.appchef.movier.navBarActivities.HomeActivity;

import com.appchef.movier.Activity.LoginActivity;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    private LottieAnimationView splashLogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
        int number = getRandomNumber();
        setIcon(number);
        splashScreenHandler();
    }

    private void splashScreenHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

    private void setIcon(int number) {
        switch (number) {
            case 1:
                splashLogoIv.setAnimation(R.raw.movie_lottie);
                break;
            case 2:
                splashLogoIv.setAnimation(R.raw.music_lottie);
                break;
            default:
                splashLogoIv.setAnimation(R.raw.mobile_lottie);
        }
    }

    private int getRandomNumber() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(3);
        return randomNumber;
    }

    private void init() {
        splashLogoIv = findViewById(R.id.splashLogoIv);
    }
}