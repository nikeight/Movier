package com.appchef.movier.FirebaseQueries;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appchef.movier.Adapters.NotificationAdapter;
import com.appchef.movier.HomeModel.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationList {

    public static List<Users> GettingNotificationList(Context context) {

        List<Users> notificationList = new ArrayList<>();

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
                        }
                    });

            FirebaseFirestore.getInstance().collection("users")
                .document(uniqueID)
                .collection("swipeAccept")
                .get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "No Notifications available, you are as lonely as me.", Toast.LENGTH_SHORT).show();
                }
            });

            return notificationList;
    }
}
