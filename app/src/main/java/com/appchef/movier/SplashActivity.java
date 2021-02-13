package com.appchef.movier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    private ImageView clapperBoard, foodCircleIv, musicNotesIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
        int number = getRandomNumber();
        Log.d("random number", number+"");
        setIcon(number);
        splashScreenHandler();
    }

    private void splashScreenHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

    private void setIcon(int number) {
        Log.d("random number", number+" switch");
        switch (number) {
            case 1:
                clapperBoard.setVisibility(View.GONE);
                foodCircleIv.setVisibility(View.VISIBLE);
                musicNotesIv.setVisibility(View.GONE);
                break;
            case 2:
                clapperBoard.setVisibility(View.GONE);
                foodCircleIv.setVisibility(View.GONE);
                musicNotesIv.setVisibility(View.VISIBLE);
                break;
            default:
                clapperBoard.setVisibility(View.VISIBLE);
                foodCircleIv.setVisibility(View.GONE);
                musicNotesIv.setVisibility(View.GONE);
        }
    }

    private int getRandomNumber() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(3);
        return randomNumber;
    }

    private void init() {
        clapperBoard = findViewById(R.id.clapperBoardIv);
        foodCircleIv = findViewById(R.id.foodCircleIv);
        musicNotesIv = findViewById(R.id.musicNotesIv);
    }
}