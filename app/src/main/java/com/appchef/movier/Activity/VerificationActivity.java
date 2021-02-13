package com.appchef.movier.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appchef.movier.R;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private PinView pinView;
    private Button verifyBtn;
    private FirebaseAuth mAuth;
    private  String verificationId;
    private TextView resend, phoneNumberTV;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        final String phonenumber = getIntent().getStringExtra("number");
        init();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);

        phoneNumberTV.setText(phonenumber);

        sendVerificationCode(phonenumber);

        verifyBtn.setOnClickListener(v -> onClickVerifyBtn());

        resend.setOnClickListener(v -> sendVerificationCode(phonenumber));
    }

    private void onClickVerifyBtn() {
        mProgressDialog.show();
        String InputCode = pinView.getText().toString();
        if (!InputCode.isEmpty()) {
            verifyCode(InputCode);
        }
    }

    private void sendVerificationCode(String phoneNumber) {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                resend.setText(""+millisUntilFinished/1000);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText(" Resend");
                resend.setEnabled(true);
            }
        }.start();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks );        // OnVerificationStateChangedCallbacks
    }

    private void init() {
        pinView = findViewById(R.id.pin_view);
        verifyBtn = findViewById(R.id.verifyCodeBtn);
        resend = findViewById(R.id.resend);
        phoneNumberTV = findViewById(R.id.phoneNumberTV);
        mAuth = FirebaseAuth.getInstance();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            mProgressDialog.dismiss();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String originalCode = phoneAuthCredential.getSmsCode();
            if (originalCode != null){
                mProgressDialog.dismiss();
                verifyCode(originalCode);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            mProgressDialog.dismiss();
            Log.d("pratik", e.getMessage());
            Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String originalCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, originalCode);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(VerificationActivity.this, MainActivity.class));
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerificationActivity.this, "Verification filed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}