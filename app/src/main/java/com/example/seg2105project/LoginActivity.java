package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    // variables for input fields
    private EditText username, password;
    // Firebase Authentication for log in
    private FirebaseAuth mAuth;
    // Firebase Real-Time Database for holding database
    private static FirebaseDatabase database;
    // reference variable to database
    private static DatabaseReference databaseReference;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initializing Firebase variables
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Verifies login of a user and creates an intent to {@link WelcomePageActivity}
     * on successful login. Else, the user will be notified of a failure to log in.
     * @param view
     */
    public void onClickLogin(View view) {
        // initializing input field variables
        username = findViewById(R.id.username);
        password = findViewById(R.id.logInPassword);
        // sign in the user using email and password
        // Note: code assisted by https://firebase.google.com/docs/auth/android/password-auth
        // and https://stackoverflow.com/questions/47296771/firebase-how-to-get-child-of-child#:~:
        // text=To%20solve%20this%2C%20you%20need%20to%20query%20your,rootRef.child%20%28%22users%22
        // %29%3B%20Query%20regnoQuery%20%3D%20usersRef.orderByChild%20%28%22regno%22%29.equalsTo%20
        // %28regno%29%3B
        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText()
                        .toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // if the user is authenticated
                        if(task.isSuccessful()){
                            // reference to "users" key in the database
                            DatabaseReference usersRef = databaseReference.child("users");
                            // getting every child that has an email equal to the username input
                            Query usernameQuery =usersRef.orderByChild("email")
                                    .equalTo(username.getText().toString());
                            usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                                // retrieving user data that matches the username input
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try{
                                        // loop through snapshot data of user obtained from match
                                        // above
                                        // NOTE: should only have to loop through 1 child since only
                                        // one match
                                        for(DataSnapshot ds : snapshot.getChildren()) {
                                            // create variables to create new user object based on
                                            // user data
                                            String firstName = ds.child("firstName")
                                                    .getValue(String.class);
                                            String lastName = ds.child("lastName")
                                                    .getValue(String.class);
                                            String email = ds.child("email")
                                                    .getValue(String.class);
                                            String accountPassword = ds.child("accountPassword")
                                                    .getValue(String.class);
                                            String phoneNumber = ds.child("phoneNumber")
                                                    .getValue(String.class);

                                            // receiving for address class
                                            String postalAddress = ds.child("address")
                                                    .child("postalAddress").getValue(String.class);
                                            String postalCode = ds.child("address")
                                                    .child("postalCode").getValue(String.class);
                                            String city = ds.child("address")
                                                    .child("city").getValue(String.class);
                                            String province = ds.child("address")
                                                    .child("province").getValue(String.class);
                                            String country = ds.child("address")
                                                    .child("country").getValue(String.class);
                                            Address address = new Address(postalAddress, postalCode, city, province, country);

                                            String type = ds.child("type").getValue(String.class);
                                            // creating new objects based on above data
                                            switch (type) {
                                                case "patient":
                                                    String healthCardNumber = ds.child("healthCardNumber")
                                                            .getValue(String.class);
                                                    if (healthCardNumber != null) {
                                                        user = new Patient(firstName, lastName, email,
                                                                accountPassword, phoneNumber, address, healthCardNumber);
                                                    }
                                                    break;
                                                case "doctor":
                                                    String employeeNumber = ds.child("employeeNumber")
                                                            .getValue(String.class);
                                                    ArrayList<String> specialties = (ArrayList<String>)
                                                            ds.child("specialties").getValue();
                                                    if (employeeNumber != null && specialties != null) {
                                                        user = new Doctor(firstName, lastName, email,
                                                                accountPassword, phoneNumber, address,
                                                                employeeNumber, specialties);
                                                    }
                                                    break;
                                                case "admin":
                                                    user = new Admin(firstName, lastName, email,
                                                            accountPassword, phoneNumber, address);
                                                    break;
                                            }

                                            // put user object into bundle and go to welcome page
                                            Intent i = new Intent(LoginActivity.this,
                                                    WelcomePageActivity.class);
                                            i.putExtra("User", user);
                                            i.putExtra("Type", type);
                                            startActivity(i);
                                            // toast to tell user that log in is successful
                                            Toast.makeText(LoginActivity.this, "Logged in!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }catch(Exception e){
                                        // catch exception if error occurs retrieving data
                                        Toast.makeText(LoginActivity.this, "Failed to retrieve user data!",
                                                Toast.LENGTH_SHORT).show();
                                        System.out.println(e);
                                    }

                                }

                                // required method to implement (can ignore)
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            // user failed to log in based on wrong credentials
                            Toast.makeText(LoginActivity.this, "Username or password is incorrect!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}