package com.appchef.movier.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.appchef.movier.R;
import com.appchef.movier.navBarActivities.ChatActivity;
import com.appchef.movier.navBarActivities.HomeActivity;
import com.appchef.movier.navBarActivities.NotificaionActivity;
import com.appchef.movier.navBarActivities.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView upperNavigationView;
    protected int actionId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        upperNavigationView = (BottomNavigationView) findViewById(R.id.upperNavigationBarView);
        upperNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemSelected = item.getItemId();
        switch (itemSelected){
            case 0:
            default:
                startActivity(new Intent(BaseActivity.this, HomeActivity.class));
                actionId = 0;
                break;
            case 1:
                startActivity(new Intent(BaseActivity.this, ChatActivity.class));
                actionId = 1;
                break;
            case 2:
                startActivity(new Intent(BaseActivity.this, NotificaionActivity.class));
                actionId = 2;
                break;
            case 3:
                startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
                actionId = 3;
                break;
        }

        return true;
    }

    private void updateNavigationBarState() {
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = upperNavigationView.getMenu().findItem(itemId);
        if (item != null)
        item.setChecked(true);
    }

    protected abstract int getLayoutId(); // this is to return which layout(activity) needs to display when clicked on tabs.

    protected abstract int getBottomNavigationMenuItemId();//Which menu item selected and change the state of that menu item
}