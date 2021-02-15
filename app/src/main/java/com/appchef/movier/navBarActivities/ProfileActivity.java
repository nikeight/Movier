package com.appchef.movier.navBarActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.appchef.movier.ProfileFragments.BaseProfileFragment;
import com.appchef.movier.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView upperNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Init();
        SetUpUpperNavBar();
        SetUpBaseFragment();
    }

    public void SetUpBaseFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.profileFrameContainer,new BaseProfileFragment(),"BaseProfileFragment")
                .commit();
    }

    private void SetUpUpperNavBar() {
        upperNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemSelected = item.getItemId();
                switch (itemSelected){
                    case R.id.homeNavBar:
                        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                    case R.id.chatNavBar:
                        startActivity(new Intent(ProfileActivity.this, ChatActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                    case R.id.notificationNavBar:
                        startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                }
                return true;
            }
        });
    }

    private void Init() {
        upperNavigationView = findViewById(R.id.upperNavBarProfile);
        upperNavigationView.setSelectedItemId(R.id.profileNavBar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SetUpBaseFragment();
    }
}