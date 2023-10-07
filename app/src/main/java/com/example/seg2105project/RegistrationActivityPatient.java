package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivityPatient extends AppCompatActivity {

    private EditText firstName, lastName, emailAddress, password, phoneNumber,
            postalAddress, postalCode, city, province, country, healthCardNumber;

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
        //address elements
        postalAddress = (EditText)findViewById(R.id.editTextTextPostalAddress);
        postalCode = (EditText)findViewById(R.id.editTextText);
        city = (EditText)findViewById(R.id.editTextText4);
        province = (EditText)findViewById(R.id.editTextText5);
        country = (EditText)findViewById(R.id.editTextText11);

        healthCardNumber = (EditText)findViewById(R.id.editTextNumber);

        // create Address object once it is created
        Address address = new Address(postalAddress.getText().toString(),postalAddress.getText().toString(),city.getText().toString(),
                province.getText().toString(),country.getText().toString());

        try{
            Registration.createUserPatient(firstName.getText().toString(), lastName.getText().toString(),
                    emailAddress.getText().toString(), password.getText().toString(), phoneNumber.getText().toString(),
                    address, healthCardNumber.getText().toString());
            // add toast to show that the form is submitted
            Toast.makeText(RegistrationActivityPatient.this, "Submitted!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(RegistrationActivityPatient.this, LoginActivity.class);
            startActivity(i);
        }catch(Exception e) {
            // if the information fails to be added to the database
            Toast.makeText(RegistrationActivityPatient.this, "Failed to submit!", Toast.LENGTH_SHORT).show();
            System.out.println(e);
        }
    }
}
