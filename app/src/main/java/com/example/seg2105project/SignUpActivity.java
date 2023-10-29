package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    /**
     * Handles button clicks and creates an intent to navigate to either the patient registration
     * or doctor registration page.
     *
     * @param view The View that triggered the button click.
     */
    public void onClickButton(View view) {
        // id of button clicked
        int pressID = view.getId();

        // go to patient sign up page
        if(pressID == R.id.patientButton){
            Intent i = new Intent(SignUpActivity.this, RegistrationActivityPatient.class);
            startActivity(i);
        }
        // go to doctor sign up page
        if(pressID == R.id.doctorButton){
            Intent i = new Intent(SignUpActivity.this, RegistrationActivityDoctor.class);
            startActivity(i);
        }
    }
}