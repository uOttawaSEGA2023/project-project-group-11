package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivityPatient extends AppCompatActivity {

    // variables representing every input field
    private EditText firstName, lastName, emailAddress, password, phoneNumber,
            postalAddress, postalCode, city, province, country, healthCardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_patient);
    }

    public void submitInformation(View view) {
        // initializing every input field variable
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

        // create Address object
        Address address = new Address(postalAddress.getText().toString(),postalCode.getText().toString(),city.getText().toString(),
                province.getText().toString(),country.getText().toString());

        // call method in Registration class to create patient user
        // parameters are String objects of the variables
        try{
            Registration.createUserPatient(firstName.getText().toString(), lastName.getText().toString(),
                    emailAddress.getText().toString(), password.getText().toString(), phoneNumber.getText().toString(),
                    address, healthCardNumber.getText().toString());
            // add toast to show user that an account has been created
            Toast.makeText(RegistrationActivityPatient.this, "Submitted!", Toast.LENGTH_SHORT).show();
            // go to log in page
            Intent i = new Intent(RegistrationActivityPatient.this, LoginActivity.class);
            startActivity(i);
        }catch(Exception e) {
            // if the information fails to be added to the database
            Toast.makeText(RegistrationActivityPatient.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
