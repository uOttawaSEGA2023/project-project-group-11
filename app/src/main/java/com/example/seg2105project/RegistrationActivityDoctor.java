package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationActivityDoctor extends AppCompatActivity {

    private EditText firstName, lastName, emailAddress, password, phoneNumber,
            postalAddress, postalCode, city, province, country, employeeNumber, specialties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_doctor);
    }

    public void submitInformation(View view) {
        // initializing every input field variable
        firstName = findViewById(R.id.editTextText2);
        lastName = findViewById(R.id.editTextText3);
        emailAddress = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        phoneNumber = findViewById(R.id.editTextPhone);
        //address elements
        postalAddress = findViewById(R.id.editTextTextPostalAddress);
        postalCode = findViewById(R.id.editTextText);
        city = findViewById(R.id.editTextText4);
        province = findViewById(R.id.editTextText5);
        country = findViewById(R.id.editTextText11);

        employeeNumber = findViewById(R.id.editTextNumber);
        specialties = findViewById(R.id.editTextTextMultiLine);



        // create Address object
        Address address = new Address(postalAddress.getText().toString(),postalCode.getText().toString(),city.getText().toString(),
                province.getText().toString(),country.getText().toString());

        // call method in Registration class to create doctor user
        // parameters are String objects of the variables
        try{
            Registration.createUserDoctor(firstName.getText().toString(), lastName.getText().toString(),
                    emailAddress.getText().toString(), password.getText().toString(), phoneNumber.getText().toString(),
                    address, employeeNumber.getText().toString(), splitSpecialties(specialties.getText().toString()));
            // add toast to show user that an account has been created
            Toast.makeText(RegistrationActivityDoctor.this, "Submitted!", Toast.LENGTH_SHORT).show();
            // go to log in page
            Intent i = new Intent(RegistrationActivityDoctor.this, LoginActivity.class);
            startActivity(i);
        }catch(Exception e) {
            // if the information fails to be added to the database
            Toast.makeText(RegistrationActivityDoctor.this, "Failed to submit!", Toast.LENGTH_SHORT).show();

            Logger logger = Logger.getLogger(RegistrationActivityDoctor.class.getName());
            logger.log(Level.SEVERE, "Failed to submit!", e);
        }
    }

    public ArrayList<String> splitSpecialties(String specialties) {
        return (ArrayList<String>) Arrays.asList(specialties.split(" "));
    }
}