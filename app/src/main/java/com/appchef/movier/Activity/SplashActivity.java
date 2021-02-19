package com.appchef.movier.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.appchef.movier.R;
import com.appchef.movier.SharedPreferenceValues.SessionManager;
import com.appchef.movier.navBarActivities.HomeActivity;

import com.appchef.movier.Activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

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
        new Handler().postDelayed(this::checkUsersAccessToken, 2500);
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

    private void checkUsersAccessToken(){
        // Check Users details
        String uniqueID = FirebaseAuth.getInstance().getUid();

        if (uniqueID != null)
            Log.w("UserDetailsSplash", "UniqueToken: -" + SessionManager.getUserToken() + "uniqueId: -" + FirebaseAuth.getInstance().getUid());

        String accessToken = SessionManager.getUserToken();

        if (uniqueID!= null && accessToken != null){
            // Means the User has been successfully registered
            ForwardToHomeScreen();
        }else if (uniqueID == null){
            // The user has not logged in
            ForwardToLoginScreen();
        }else {
            // User has been logged in but not yet register.
            ForwardToRegistrationScreen();
        }
    }

    private void ForwardToRegistrationScreen() {
        Intent mainIntent = new Intent(SplashActivity.this, RegistrationActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void ForwardToLoginScreen() {
        Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void ForwardToHomeScreen(){
        Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void init() {
        splashLogoIv = findViewById(R.id.splashLogoIv);
    }
}