package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onClickButton(View view) {
        int pressID = view.getId();
        if(pressID == R.id.patientButton){
            Intent i = new Intent(SignUpActivity.this, RegistrationActivityPatient.class);
            startActivity(i);
        } else if(pressID == R.id.doctorButton){
            Intent i = new Intent(SignUpActivity.this, RegistrationActivityDoctor.class);
            startActivity(i);
        }
    }
}