package com.appchef.movier.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.appchef.movier.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Google_Sign_In";
    private static final String tag = "LifeCycle Events";
    private static final int RC_SIGN_IN = 9001;

    // GoogleSignIn
    GoogleSignInOptions signInOptions;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    // Buttons
    private Button googleSignInBtn;

    // Progress Dialog
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init();
        IntegrateGoogleSignIn();

        // OnClick Events.
        googleSignInBtn.setOnClickListener(view -> googleSignIn());

    }

    private void IntegrateGoogleSignIn() {
        // Step - 1 To integrate Google Services
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_google_key))
                .requestEmail()
                .build();

        // Google Client specified by our Options.
        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);
    }

    private void googleSignIn() {
        // Step - 2 Passing the Email/Data
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        Log.d(TAG, "Intent Started");
        startActivityForResult(googleSignInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "OnActivity Started");
        // Step - 3 Handling the Intent Data.
        // Result returned from launching the Intent from GoogleSignInApi
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                // Passing the required Token and SignInUp along with the data saved in the Firebase.
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            ForwardToMainScreen();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void Init() {
        googleSignInBtn = findViewById(R.id.googleSignInBtn);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();
        Log.d(tag, "In the onStart() event");

        // Check if the user is signed in or not.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!= null){
            ForwardToMainScreen();
        }
    }

    private void ForwardToMainScreen(){
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainIntent);
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