package com.appchef.movier.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.appchef.movier.R;
import com.appchef.movier.Registration.GenresSelectionFragment;
import com.appchef.movier.Registration.RegistrationFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ChangeFragments();
    }

    private void ChangeFragments() {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.registrationFrameContainer,new RegistrationFragment()).commit();
    }
}