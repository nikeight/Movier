package com.appchef.movier.navBarActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.appchef.movier.Activity.ConversationActivity;
import com.appchef.movier.Adapter.AdapterNewMatch;
import com.appchef.movier.Model.ModelNewMatch;
import com.appchef.movier.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private BottomNavigationView upperNavigationView;
    RecyclerView matchListRv, chatListRv;

    Button btnChat;

    FirebaseAuth firebaseAuth;

    List<ModelNewMatch> newMatchList;
    AdapterNewMatch adapterNewMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Init();
        SetUpUpperNavBar();

        ModelNewMatch m1 = new ModelNewMatch("Jvl4yTrixcfMsK7maPyNnxKKnqt1", "https://www.swisswatchexpo.com/TheWatchClub/wp-content/uploads/2020/03/tom-cruise-200x200.png", "Pratik");
        ModelNewMatch m2 = new ModelNewMatch("w1oxCS9WH2gnOB9DYdeR2YI5NrB2", "null", "Niket");
        newMatchList.add(m1);
        newMatchList.add(m2);
        loadNewMatch();
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, ConversationActivity.class));
            }
        });
    }

    private void loadNewMatch() {
        adapterNewMatch = new AdapterNewMatch(this, newMatchList);
        matchListRv.setAdapter(adapterNewMatch);
    }

    private void SetUpUpperNavBar() {
        upperNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemSelected = item.getItemId();
                switch (itemSelected){
                    case R.id.homeNavBar:
                        startActivity(new Intent(ChatActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.notificationNavBar:
                        startActivity(new Intent(ChatActivity.this, NotificaionActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profileNavBar:
                        startActivity(new Intent(ChatActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });
    }

    private void Init() {
        upperNavigationView = findViewById(R.id.upperNavigationBarView);
        upperNavigationView.setSelectedItemId(R.id.chatNavBar);
        matchListRv = findViewById(R.id.matchListRv);
        chatListRv = findViewById(R.id.chatListRv);
        btnChat = findViewById(R.id.btnChat);

        firebaseAuth = FirebaseAuth.getInstance();

        newMatchList = new ArrayList<>();
    }
}