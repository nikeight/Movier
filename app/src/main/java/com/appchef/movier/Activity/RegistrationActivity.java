package com.appchef.movier.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.appchef.movier.R;
import com.appchef.movier.navBarActivities.HomeActivity;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // For testing.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
            }
        },2000);

    }
}