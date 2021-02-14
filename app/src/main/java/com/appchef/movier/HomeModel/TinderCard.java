package com.appchef.movier.HomeModel;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appchef.movier.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.util.HashMap;
import java.util.Map;

@Layout(R.layout.person_home_card_view)
public class TinderCard {

    @View(R.id.profileImageView)
    public ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    public TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    public TextView locationNameTxt;

//    Profile mProfile;
//    Context mContext;
//    SwipePlaceHolderView mSwipeView;
//
//    public TinderCard(Context context, Profile profile, SwipePlaceHolderView swipeView) {
//        mContext = context;
//        mProfile = profile;
//        mSwipeView = swipeView;
//    }
//
//    @Resolve
//    public void onResolved() {
//        Log.i("SwipeLayout","Yaha tk aya TinderCard");
//        Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
//        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
//        locationNameTxt.setText(mProfile.getLocation());
//    }

    Users mUsers;
    Context mContext;
    SwipePlaceHolderView mSwipeView;

    public TinderCard(Context context, Users users, SwipePlaceHolderView swipeView) {
        mContext = context;
        mUsers = users;
        mSwipeView = swipeView;
    }

    @Resolve
    public void onResolved() {
        Log.i("SwipeLayout","ImageUrl" + mUsers.getImageUrl() );
        Glide.with(mContext)
                .load(mUsers.getImageUrl())
                .override(320,420)
                .into(profileImageView);

        nameAgeTxt.setText(mUsers.getUsername() + ", " + mUsers.getAge());
        locationNameTxt.setText(mUsers.gender);
    }

    @SwipeOut
    public void onSwipedOut() {
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeIn
    public void onSwipeIn() {
        Log.d("EVENT", "onSwipedIn");
        // Saving the Swipe In Data.
        Map<String,Object> userData = new HashMap<>();
        userData.put("username",mUsers.getUsername());
        userData.put("uniqueID",mUsers.getUniqueID());
        userData.put("requestStatus",true);
        userData.put("imageUrl",mUsers.getImageUrl());

        String uniqueID = FirebaseAuth.getInstance().getUid();
        if (uniqueID != null)
        FirebaseFirestore.getInstance().collection("users").document(uniqueID)
                .collection("swipeAccept").add(userData).addOnSuccessListener(
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(mContext, "Request Send", Toast.LENGTH_SHORT).show();
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("TAG",Log.getStackTraceString(e));
                Toast.makeText(mContext, "The user didn't get your accept request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SwipeCancelState
    public void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeInState
    public void onSwipeInState() {
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState");
    }
}


