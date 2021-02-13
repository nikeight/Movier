package com.appchef.movier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ChatActivity extends AppCompatActivity {

    RecyclerView matchListRv, chatListRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    private void init() {
        matchListRv = findViewById(R.id.matchListRv);
        chatListRv = findViewById(R.id.chatListRv);
    }
}