package com.appchef.movier.Registration;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.appchef.movier.R;
import com.appchef.movier.SharedPreferenceValues.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegistrationFragment extends Fragment {

    // Views
    private Button proceedBtn;
    private TextInputLayout fullNameLayout, usernameLayout, ageLayout, contactLayout, genderLayout;

    private Context context;

    // Firebase
    private FirebaseFirestore fireStore;
    private FirebaseAuth mAuth;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;

    // For Log Test
    String TAG = "NPE";

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        Init(view);

        context = getContext();

        proceedBtn.setOnClickListener(view1 -> checkCredentials());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseInit();
    }

    private void FirebaseInit() {
        //Fire base Init
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        collectionReference = fireStore.collection("users");
        documentReference = collectionReference.document("UUID");
    }

    private void checkCredentials() {
        if (!checkFullName() | !username() | !age() | !gender() | !checkContactNumber()) {
            return;
        }

        saveChangesToFireStore();
        Log.i(TAG,FirebaseAuth.getInstance().getUid() + ": Is the Unique Id of the User" +
                "Username:" + usernameLayout.getEditText().getText().toString());
    }

    private boolean checkContactNumber() {
        String contact = contactLayout.getEditText().getText().toString().trim();

        if (contact.isEmpty()) {
            contactLayout.setError("Field can't be empty");
            return false;
        } else if (contact.length() != 10 ) {
            contactLayout.setError("Not a valid Contact number");
            return false;
        }else {
            contactLayout.setErrorEnabled(false);
            contactLayout.setError(null);
            return true;
        }
    }

    private boolean gender() {
        String gender = genderLayout.getEditText().getText().toString();

        if (gender.isEmpty()) {
            genderLayout.setError("Field can't be empty");
            return false;
        } else {
            genderLayout.setError(null);
            return true;
        }
    }

    private boolean age() {
        String age = ageLayout.getEditText().getText().toString();

        if (age.isEmpty()) {
            ageLayout.setError("Field can't be empty");
            return false;
        } else {
            ageLayout.setError(null);
            return true;
        }
    }

    private boolean username() {
        String username = usernameLayout.getEditText().getText().toString();

        if (username.isEmpty()) {
            usernameLayout.setError("Field can't be empty");
            return false;
        } else {
            usernameLayout.setError(null);
            return true;
        }
    }

    private boolean checkFullName() {
        // To check the fields.
        String fullName = fullNameLayout.getEditText().getText().toString();

        if (fullName.isEmpty()) {
            fullNameLayout.setError("Field can't be empty");
            return false;
        } else {
            fullNameLayout.setError(null);
            return true;
        }
    }

    private void saveChangesToFireStore() {
        Map<String,Object> userDataMap = new HashMap<>();

        String username = usernameLayout.getEditText().getText().toString();
        String uniqueID = mAuth.getUid();
        String uniqueKey = SessionManager.getUserToken();

        userDataMap.put("fullName",fullNameLayout.getEditText().getText().toString());
        userDataMap.put("username",username);
        userDataMap.put("age",ageLayout.getEditText().getText().toString());
        userDataMap.put("gender",genderLayout.getEditText().getText().toString());
        userDataMap.put("contactNumber",contactLayout.getEditText().getText().toString());
        userDataMap.put("uniqueID",uniqueID);
        userDataMap.put("access_token",uniqueKey);


        if (uniqueID!=null)
        FirebaseFirestore.getInstance().collection("users").document(uniqueID)
                .set(userDataMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Successfully Registered the Data", Toast.LENGTH_SHORT).show();
                        changeFragment();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to register the data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeFragment() {
        try {
            getFragmentManager().beginTransaction()
                    .replace(R.id.registrationFrameContainer,new GenresSelectionFragment()).commit();

        }catch (NullPointerException e){
            Log.i(TAG,Log.getStackTraceString(e));
        }
    }

    private void Init(View view) {

        proceedBtn = view.findViewById(R.id.proceedBtn);
        fullNameLayout = view.findViewById(R.id.fullNameLayout);
        usernameLayout = view.findViewById(R.id.usernameLayout);
        ageLayout = view.findViewById(R.id.ageLayout);
        contactLayout = view.findViewById(R.id.contactLayout);
        genderLayout = view.findViewById(R.id.genderLayout);
    }
}