package com.appchef.movier.navBarActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.appchef.movier.HomeModel.Profile;
import com.appchef.movier.HomeModel.TinderCard;
import com.appchef.movier.R;
import com.appchef.movier.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView upperNavigationView;

    SwipePlaceHolderView mSwipeView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Init();
        SetUpUpperNavBar();
        SwipeLayout();
    }

    public void SwipeLayout() {
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.home_swipe_in)
                        .setSwipeOutMsgLayoutId(R.layout.home_swipe_out));


        for(Profile profile : Objects.requireNonNull(Utils.loadProfiles(this.getApplicationContext()))){
            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
        }

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });
    }

    private void SetUpUpperNavBar() {
        upperNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemSelected = item.getItemId();
                switch (itemSelected){
                    case R.id.chatNavBar:
                        startActivity(new Intent(HomeActivity.this, ChatActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.notificationNavBar:
                        startActivity(new Intent(HomeActivity.this, NotificaionActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profileNavBar:
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });
    }

    public void Init() {
        upperNavigationView = findViewById(R.id.upperNavigationBarView);
        upperNavigationView.setSelectedItemId(R.id.homeNavBar);

        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();
    }
}