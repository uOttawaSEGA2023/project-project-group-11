package com.example.seg2105project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseReference;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickLogin(View view){
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.logInPassword);
        // sign in the user using email and password
        // Note: code assisted by https://firebase.google.com/docs/auth/android/password-auth
        // and https://stackoverflow.com/questions/47296771/firebase-how-to-get-child-of-child#:~:text=To%20solve%20this%2C%20you%20need%20to%20query%20your,rootRef.child%20%28%22users%22%29%3B%20Query%20regnoQuery%20%3D%20usersRef.orderByChild%20%28%22regno%22%29.equalsTo%20%28regno%29%3B
        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference usersRef = databaseReference.child("users");
                            Query usernameQuery =usersRef.orderByChild("email").equalTo(username.getText().toString());
                            usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    try{
                                        for(DataSnapshot ds : snapshot.getChildren()) {
                                            String key = ds.getKey();
                                            String firstName = ds.child("firstName").getValue(String.class);
                                            String lastName = ds.child("lastName").getValue(String.class);
                                            String email = ds.child("email").getValue(String.class);
                                            String accountPassword = ds.child("accountPassword").getValue(String.class);
                                            String phoneNumber = ds.child("phoneNumber").getValue(String.class);
                                            String address = ds.child("address").getValue(String.class);
                                            String type = ds.child("type").getValue(String.class);
                                            if(type.equals("patient")){
                                                String healthCardNumber = ds.child("healthCardNumber").getValue(String.class);
                                                user = new Patient(firstName, lastName, email, accountPassword, phoneNumber, address, healthCardNumber);
                                            } else if(type.equals("doctor")){
                                                ///create new doctor object
                                            } else if(type.equals("admin")){
                                                user = new Admin(firstName, lastName, email, accountPassword, phoneNumber, address);
                                            }
                                            Intent i = new Intent(LoginActivity.this, WelcomePageActivity.class);
                                            i.putExtra("User", user);
                                            i.putExtra("Type", type);
                                            startActivity(i);
                                            Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch(Exception e){
                                        Toast.makeText(LoginActivity.this, "Failed to log in!", Toast.LENGTH_SHORT).show();
                                        System.out.println(e);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError error) {

                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to Log In!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}