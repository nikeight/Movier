package com.appchef.movier.navBarActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Toast;

import com.appchef.movier.Adapters.NotificationAdapter;
import com.appchef.movier.HomeModel.Users;
import com.appchef.movier.R;
import com.appchef.movier.Registration.GenresAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificaionActivity extends AppCompatActivity {

    private BottomNavigationView upperNavigationView;
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter adapter;
    private List<Users> notificationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaion);

        Init();
        SetUpUpperNavBar();
        GettingNotificationList();
//        AdapterSetUp();
    }

    private void GettingNotificationList() {
        Log.i("NotificationListGettingListFunction",notificationList.toString());
        String uniqueID = FirebaseAuth.getInstance().getUid();
        if (uniqueID != null)
        FirebaseFirestore.getInstance().collection("users")
                .document(uniqueID)
                .collection("swipeAccept")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                     for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                         Users users = snapshot.toObject(Users.class);
                         notificationList.add(users);
                         Log.i("NotificationListAdapter",notificationList.toString()
                            + "Size: " + notificationList.size());
                     }
                        AdapterSetUp();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NotificaionActivity.this, "No Notifications avilables, you are as lonely as me.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AdapterSetUp() {
        notificationRecyclerView.setHasFixedSize(true);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new NotificationAdapter(notificationList,NotificaionActivity.this);
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
                        startActivity(new Intent(NotificaionActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                    case R.id.chatNavBar:
                        startActivity(new Intent(NotificaionActivity.this, ChatActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                    case R.id.profileNavBar:
                        startActivity(new Intent(NotificaionActivity.this, ProfileActivity.class));
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