package com.appchef.movier.navBarActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.MenuItem;

import com.appchef.movier.Adapters.NotificationAdapter;
import com.appchef.movier.FirebaseQueries.NotificationList;
import com.appchef.movier.HomeModel.Users;
import com.appchef.movier.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import static com.appchef.movier.FirebaseQueries.NotificationList.GettingNotificationList;

public class NotificationActivity extends AppCompatActivity {

    private BottomNavigationView upperNavigationView;
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaion);

        Init();
        SetUpUpperNavBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AdapterSetUp();
    }

    private void AdapterSetUp() {
        List<Users> notificationList = GettingNotificationList(this);
        notificationRecyclerView.setHasFixedSize(true);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificationAdapter(notificationList, NotificationActivity.this);
        notificationRecyclerView.setAdapter(adapter);
        Log.i("NotificationList",notificationList.toString() + " Size: " + notificationList.size());
    }

    private void SetUpUpperNavBar() {
        upperNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemSelected = item.getItemId();
                switch (itemSelected){
                    case R.id.homeNavBar:
                        startActivity(new Intent(NotificationActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                    case R.id.chatNavBar:
                        startActivity(new Intent(NotificationActivity.this, ChatActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                    case R.id.profileNavBar:
                        startActivity(new Intent(NotificationActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                }
                return true;
            }
        });
    }

    private void Init() {
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        upperNavigationView = findViewById(R.id.notificationUpperNavBar);
        upperNavigationView.setSelectedItemId(R.id.notificationNavBar);
    }
}