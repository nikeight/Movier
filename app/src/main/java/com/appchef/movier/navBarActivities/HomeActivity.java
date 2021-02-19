package com.appchef.movier.navBarActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.appchef.movier.HomeModel.Profile;
import com.appchef.movier.HomeModel.TinderCard;
import com.appchef.movier.HomeModel.Users;
import com.appchef.movier.R;
import com.appchef.movier.SharedPreferenceValues.SessionManager;
import com.appchef.movier.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView upperNavigationView;

    SwipePlaceHolderView mSwipeView;
    Context mContext;
    String TAG = "TestLogcat";
    List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Init();
        gettingUsersList();
        SetUpUpperNavBar();
        Toast.makeText(this, "onBaseActivity() ", Toast.LENGTH_SHORT).show();
        Log.w("UserDetailsHome", "UniqueToken: -" + SessionManager.getUserToken() + "uniqueId: -" + FirebaseAuth.getInstance().getUid());

    }

    private void gettingUsersList() {
        CollectionReference endCollection = FirebaseFirestore.getInstance().collection("users");
        endCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                    Users users = snapshot.toObject(Users.class);
                    usersList.add(users);
//                    Log.i(TAG,usersList.toString() +
//                            " List Size " + usersList.size());
                }
                Log.i(TAG,usersList.toString() +
                        " List Size " + usersList.size());
                SwipeLayout();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Connection issue, try again", Toast.LENGTH_SHORT).show();
                Log.i(TAG,Log.getStackTraceString(e));
            }
        });
    }

    public void SwipeLayout() {

        Log.i("SwipeLayout","Yaha tk aya SWIPE LAYOUT");
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.home_swipe_in)
                        .setSwipeOutMsgLayoutId(R.layout.home_swipe_out));

//        for(Profile profile : Objects.requireNonNull(Utils.loadProfiles(this.getApplicationContext()))){
//            Log.i("SwipeLayout","Yaha tk aya For loop");
//            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
//        }

        Log.i("SwipeLayout","Yaha tk aya Before For Loop"
        + "userListSize:" + usersList.size());

        for(Users users : usersList){
            Log.i("SwipeLayout","Yaha tk aya For loop");
            mSwipeView.addView(new TinderCard(mContext, users, mSwipeView));
        }

//        for (int i = 0; i<usersList.size(); i++){
//            Log.i("SwipeLayout","Yaha tk aya For loop");
//            mSwipeView.addView(new TinderCard(mContext, usersList.get(i) , mSwipeView));
//        }

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
                        finishAffinity();
                        break;
                    case R.id.notificationNavBar:
                        startActivity(new Intent(HomeActivity.this, NotificaionActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        break;
                    case R.id.profileNavBar:
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
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

        usersList = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}