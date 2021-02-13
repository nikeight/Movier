package com.appchef.movier.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.appchef.movier.R;
import com.appchef.movier.navBarActivities.HomeActivity;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    private ImageView splashLogoIv;

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
                Intent intent = new Intent(SplashActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

    private void setIcon(int number) {
        switch (number) {
            case 1:
                splashLogoIv.setImageResource(R.drawable.ic_foodcircle);
                break;
            case 2:
                splashLogoIv.setImageResource(R.drawable.ic_musicalnotes);
                break;
            default:
                splashLogoIv.setImageResource(R.drawable.ic_clapperboard);
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