package com.appchef.movier.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appchef.movier.Adapter.AdapterChat;
import com.appchef.movier.Model.ModelChat;
import com.appchef.movier.R;
import com.appchef.movier.navBarActivities.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class ConversationActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nameTv, userOnlineStatusTv;
    EditText messageEt;
    ImageButton sendBtn, attachBtn;
    LinearLayout chatLayout;

    LinearLayoutManager linearLayoutManager;

    FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChat> chatList;
    AdapterChat adapterChat;

    String hisUid;
    String myUid;
    String hisImage;
    String hisUserName;
    String onlineStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        init();

        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        getDataViaIntent();

        loadHisData();

        recyclerViewLayout();

        sendBtn.setOnClickListener(v -> onclickSendBtn());

        readMessage();

        seenMessage();

        seenMessage();
    }

    private void recyclerViewLayout() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getDataViaIntent() {
        Intent intent = getIntent();
        hisUid =intent.getStringExtra("hisUid");
    }

    private void onclickSendBtn() {
        String message = messageEt.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(ConversationActivity.this, "Cannot send the empty message", Toast.LENGTH_SHORT).show();
        } else {
            sendMessage(message);
        }
        messageEt.setText("");
    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("type", "text");
        hashMap.put("isSeen", false);
        databaseReference.child("Chats").push().setValue(hashMap);

//        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ModelUsers users = snapshot.getValue(ModelUsers.class);
//
//                if (notify) {
//                    sentNotification(hisUid, users.getName(), message);
//                }
//                notify = false;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(myUid)
                .child(hisUid);
        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef1.child("id").setValue(hisUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(hisUid)
                .child(myUid);

        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef2.child("id").setValue(myUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage() {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) || chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)){
                        chatList.add(chat);
                    }

                    adapterChat = new AdapterChat(ConversationActivity.this, chatList, hisImage);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)){
                        HashMap<String, Object> hasSeenHasMap = new HashMap<>();
                        hasSeenHasMap.put("isSeen", "true");
                        ds.getRef().updateChildren(hasSeenHasMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.chat_recyclerView);
        profileIv = findViewById(R.id.profileIv);
        nameTv = findViewById(R.id.nameTv);
        userOnlineStatusTv = findViewById(R.id.userStatusTv);
        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.sendBtn);
        attachBtn = findViewById(R.id.attachBtn);
        chatLayout = findViewById(R.id.chatLayout);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
    }

    private void loadHisData() {
        Log.d("hisName", "1 "+hisUid );
        DocumentReference docRef = fstore.collection("users").document(hisUid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                hisUserName =documentSnapshot.getString("userName");
                onlineStatus = documentSnapshot.getString("onlineStatus");
                hisImage = documentSnapshot.getString("profileImage");

                nameTv.setText(hisUserName);
                if (onlineStatus.equals("online")) {
                    userOnlineStatusTv.setText(R.string.userOnline);
                } else {
                    userOnlineStatusTv.setText(R.string.userOffline);
                }
                try {
                    Picasso.get().load(hisImage).placeholder(R.drawable.ic_account_black).into(profileIv);
                }
                catch (Exception z){
                    Picasso.get().load(R.drawable.ic_account_black).into(profileIv);
                }

            }
        });
    }

    private void checkUserStatus() {
//        get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            myUid = user.getUid();
        } else {
            startActivity(new Intent(this, BaseActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }
}