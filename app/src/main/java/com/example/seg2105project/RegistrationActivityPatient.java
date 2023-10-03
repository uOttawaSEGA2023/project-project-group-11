package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivityPatient extends AppCompatActivity {

    EditText firstName, lastName, emailAddress, password, phoneNumber, postalAddress, healthCardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_patient);
    }

    public void submitInformation(View view) {
        firstName = (EditText)findViewById(R.id.editTextText2);
        lastName = (EditText)findViewById(R.id.editTextText3);
        emailAddress = (EditText)findViewById(R.id.editTextTextEmailAddress);
        password = (EditText)findViewById(R.id.editTextTextPassword);
        phoneNumber = (EditText)findViewById(R.id.editTextPhone);
        postalAddress = (EditText)findViewById(R.id.editTextTextPostalAddress);
        healthCardNumber = (EditText)findViewById(R.id.editTextNumber);
        try{
            Registration.createUserPatient(firstName.getText().toString(), lastName.getText().toString(),
                    emailAddress.getText().toString(), password.getText().toString(), phoneNumber.getText().toString(),
                    postalAddress.getText().toString(), healthCardNumber.getText().toString());
            // add toast to show that the form is submitted
            Toast.makeText(RegistrationActivityPatient.this, "Submitted!", Toast.LENGTH_SHORT).show();
        }catch(Exception e) {
            // if the information fails to be added to the database
            Toast.makeText(RegistrationActivityPatient.this, "Failed to submit!", Toast.LENGTH_SHORT).show();
        }
    }
}
