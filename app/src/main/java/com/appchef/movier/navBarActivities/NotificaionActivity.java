package com.appchef.movier.navBarActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.appchef.movier.BaseActivity;
import com.appchef.movier.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotificaionActivity extends AppCompatActivity {

    private BottomNavigationView upperNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaion);

        Init();
        SetUpUpperNavBar();
    }

    private void SetUpUpperNavBar() {
        upperNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemSelected = item.getItemId();
                switch (itemSelected){
                    case R.id.homeNavBar:
                        startActivity(new Intent(NotificaionActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.chatNavBar:
                        startActivity(new Intent(NotificaionActivity.this, ChatActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profileNavBar:
                        startActivity(new Intent(NotificaionActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });
    }

    private void Init() {
        upperNavigationView = findViewById(R.id.upperNavigationBarView);
        upperNavigationView.setSelectedItemId(R.id.notificationNavBar);
    }
}