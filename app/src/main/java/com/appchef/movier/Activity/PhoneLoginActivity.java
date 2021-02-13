package com.appchef.movier.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appchef.movier.R;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

public class PhoneLoginActivity extends AppCompatActivity {

    private TextInputEditText phoneEt;
    private Button getOTPBtn;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        init();
        getOTPBtn.setOnClickListener(v -> onGetOTPClick());
    }

    private void onGetOTPClick() {
        if (TextUtils.isEmpty(phoneEt.getText().toString())) {
            Toast.makeText(PhoneLoginActivity.this, "Enter number....", Toast.LENGTH_SHORT).show();
        } else if (phoneEt.getText().toString().replace(" ", "").length()!=10){
            Toast.makeText(PhoneLoginActivity.this, "Enter correct number....", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(PhoneLoginActivity.this, VerificationActivity.class);
            intent.putExtra("number", countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        }
    }

    private void init() {
        phoneEt = findViewById(R.id.phoneEt);
        getOTPBtn = findViewById(R.id.getOTPBtn);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        countryCodePicker.registerCarrierNumberEditText(phoneEt);
    }
}