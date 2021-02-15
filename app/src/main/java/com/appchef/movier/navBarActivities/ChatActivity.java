package com.appchef.movier.navBarActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.appchef.movier.Adapter.AdapterChatList;
import com.appchef.movier.Adapter.AdapterNewMatch;
import com.appchef.movier.Model.ModelChat;
import com.appchef.movier.Model.ModelChatList;
import com.appchef.movier.Model.ModelNewMatch;
import com.appchef.movier.Model.ModelUser;
import com.appchef.movier.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ChatActivity extends AppCompatActivity {

    private BottomNavigationView upperNavigationView;
    RecyclerView matchListRv, chatListRv;

    FirebaseAuth firebaseAuth;
    private FirebaseFirestore fstore;
    FirebaseUser currentUser;
    DatabaseReference reference;

    List<ModelNewMatch> newMatchList;
    AdapterNewMatch adapterNewMatch;

    List<ModelChatList> chatList;
    AdapterChatList adapterChatList;
    List<ModelUser> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Init();
        SetUpUpperNavBar();

        loadNewMatch();
        loadChatList();
    }

    private void loadNewMatch() {
        ModelNewMatch m1 = new ModelNewMatch("Jvl4yTrixcfMsK7maPyNnxKKnqt1", "https://www.swisswatchexpo.com/TheWatchClub/wp-content/uploads/2020/03/tom-cruise-200x200.png", "Pratik");
        ModelNewMatch m2 = new ModelNewMatch("w1oxCS9WH2gnOB9DYdeR2YI5NrB2", "null", "Niket");
        newMatchList.add(m1);
        newMatchList.add(m2);

        adapterNewMatch = new AdapterNewMatch(this, newMatchList);
        matchListRv.setAdapter(adapterNewMatch);
    }

    private void loadChatList() {
        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChatList modelChatList = ds.getValue(ModelChatList.class);
                    chatList.add(modelChatList);
                }
                loadChats();
                Log.d("user list", chatList.size() + " cl");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void SetUpUpperNavBar() {
        upperNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemSelected = item.getItemId();
                switch (itemSelected) {
                    case R.id.homeNavBar:
                        startActivity(new Intent(ChatActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                    case R.id.notificationNavBar:
                        startActivity(new Intent(ChatActivity.this, NotificationActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                    case R.id.profileNavBar:
                        startActivity(new Intent(ChatActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
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

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();

        newMatchList = new ArrayList<>();
        chatList = new ArrayList<>();
    }

    private void loadChats() {
        usersList = new ArrayList<>();
        CollectionReference colRef = fstore.collection("users");
        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot snapshot : value) {
                    ModelUser users = snapshot.toObject(ModelUser.class);
                    for (ModelChatList chatList : chatList) {
                        if (users.getUid() != null && users.getUid().equals(chatList.getId())) {
                            usersList.add(users);
                            break;
                        }
                    }
                    adapterChatList = new AdapterChatList(ChatActivity.this, usersList);
                    chatListRv.setAdapter(adapterChatList);
                    for (int i = 0; i < usersList.size(); i++) {
                        lastMassage(usersList.get(i).getUid());
                    }
                }
            }
        });
        Log.d("user list", usersList.size() + "");
    }

    private void lastMassage(String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String theLastMsg = "default";
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat == null) {
                        continue;
                    }
                    String sender = chat.getSender();
                    String receiver = chat.getReceiver();
                    if (sender == null || receiver == null) {
                        continue;
                    }
                    if (chat.getReceiver().equals(currentUser.getUid()) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(currentUser.getUid())) {
                        if (chat.getType().equals("image")) {
                            theLastMsg = "Sent a image";
                        } else {
                            theLastMsg = chat.getMessage();
                        }
                    }
                }

                adapterChatList.setLastMsgMap(userId, theLastMsg);
                adapterChatList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}