package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.*;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class WelcomePageActivity extends AppCompatActivity {

    // variable to display message
    TextView welcomeText;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        mAuth = FirebaseAuth.getInstance();
        // get User object from log in
        User user = (User) getIntent().getExtras().getSerializable("User");
        // get type of User
        String type = getIntent().getExtras().getString("Type");
        welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + user.getFirstName() + " " + user.getLastName() +
                "! You are logged in as " + type);
    }

    /**
     * Logs out a user and redirects them to {@link MainActivity} upon successful signout. Else,
     * the user will be notified of a failure to sign out.
     * @param view
     */
    public void onClickSignOut(View view){
        try{
            mAuth.signOut();
            Intent signOut = new Intent(WelcomePageActivity.this, MainActivity.class);
            startActivity(signOut);
            Toast.makeText(WelcomePageActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(WelcomePageActivity.this, "Error logging out", Toast.LENGTH_SHORT).show();
            System.out.println(e);
        }
    }
}