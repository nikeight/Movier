package com.appchef.movier.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.appchef.movier.HomeModel.Users;
import com.appchef.movier.R;
import com.appchef.movier.SharedPreferenceValues.SessionManager;
import com.appchef.movier.navBarActivities.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Google_Sign_In";
    private static final String tag = "LifeCycle Events";
    private static final int RC_SIGN_IN = 9001;
    private boolean authenticated;

    // GoogleSignIn
    GoogleSignInOptions signInOptions;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;

    // Buttons
    private Button googleSignInBtn, phoneLoginBtn;

    // Progress Dialog
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // OnClick Events.
        googleSignInBtn.setOnClickListener(view -> googleSignIn());
        phoneLoginBtn.setOnClickListener(view -> phoneSignIn());

    }

    private void phoneSignIn() {
        Intent intent = new Intent(LoginActivity.this, PhoneLoginActivity.class);
        startActivity(intent);
    }

    private void googleSignIn() {
        mProgressDialog.setMessage("Logging In...");
        mProgressDialog.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mProgressDialog.dismiss();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        mProgressDialog.setMessage("Logging In...");
        mProgressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mProgressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential:success");
                            generateAccessToken();
                        } else {
                            // If sign in fails, display a message to the user.
                            mProgressDialog.dismiss();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "No, internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkAuthentication() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (authenticated){
                    startActivity(new Intent(LoginActivity.this,BaseActivity.class));
                }else{
                    ForwardToRegistrationScreen();
                }
            }
        },2000);
    }

    private void generateAccessToken() {

        String uniqueID = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("users")
                .document(uniqueID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Users users = documentSnapshot.toObject(Users.class);
                        String uniqueKey = users.getAccess_token();

                        if (uniqueKey == null){
                            UUID newUniqueKey = UUID.randomUUID();
                            SessionManager.setUserToken(String.valueOf(newUniqueKey));
                        }else {
                            SessionManager.setUserToken(uniqueKey);
                        }

                        authenticated = true;
                        Toast.makeText(LoginActivity.this, "The id is " + uniqueKey + " and it is authenticated" + authenticated + ":Value", Toast.LENGTH_LONG).show();
                        Log.i("Authenticated","The id is " + uniqueKey + " and it is authenticated" + authenticated + ":Value");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                authenticated = false;
                Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        checkAuthentication();
    }

    private void Init() {
        googleSignInBtn = findViewById(R.id.googleSignInBtn);
        phoneLoginBtn = findViewById(R.id.phoneLoginBtn);
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mAuth = FirebaseAuth.getInstance();
    }


    private void ForwardToRegistrationScreen(){
        Intent mainIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(mainIntent);
        finish();
    }



    public void onRestart() {
        super.onRestart();
        Log.d(tag, "In the onRestart() event");
    }

    public void onResume() {
        super.onResume();
        Log.d(tag, "In the onResume() event");
    }

    public void onPause() {
        super.onPause();
        Log.d(tag, "In the onPause() event");
    }

    public void onStop() {
        super.onStop();
        Log.d(tag, "In the onStop() event");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "In the onDestroy() event");
    }
}