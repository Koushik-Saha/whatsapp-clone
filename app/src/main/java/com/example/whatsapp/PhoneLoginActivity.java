package com.example.whatsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneLoginActivity extends AppCompatActivity {

    private Button SendVerificationCodeButton, VerifyButton;
    private EditText InputPhoneNumber, InputVerificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);



        SendVerificationCodeButton = (Button) findViewById(R.id.send_ver_code_button);
        VerifyButton = (Button) findViewById(R.id.verify_button);
        InputPhoneNumber = (EditText) findViewById(R.id.phone_number_input);
        InputVerificationCode = (EditText) findViewById(R.id.verification_code_input);



        SendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                VerifyButton.setVisibility(View.VISIBLE);
                InputVerificationCode.setVisibility(View.VISIBLE);
            }
        });

    }
}
