package com.appchef.movier.Activity;

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
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Google_Sign_In";
    private static final String tag = "LifeCycle Events";
    private static final int RC_SIGN_IN = 9001;

    // GoogleSignIn
    GoogleSignInOptions signInOptions;
    GoogleSignInClient mGoogleSignInClient;

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
        mProgressDialog.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            mProgressDialog.dismiss();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            ForwardToRegistrationScreen();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, ""+e.getMessage()+" "+ e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }


    private void Init() {
        googleSignInBtn = findViewById(R.id.googleSignInBtn);
        phoneLoginBtn = findViewById(R.id.phoneLoginBtn);
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void onStart() {
        super.onStart();
        Log.d(tag, "In the onStart() event");

        // Check if the user is signed in or not.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!= null){
            ForwardToRegistrationScreen();
        }
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