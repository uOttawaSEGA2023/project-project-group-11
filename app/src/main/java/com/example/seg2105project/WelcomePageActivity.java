package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * The WelcomePageActivity class represents the activity displayed upon successful user login.
 * It provides a welcome message, user type-specific functionality, and the ability to sign out.
 */
public class WelcomePageActivity extends AppCompatActivity implements View.OnClickListener {

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
        if(type.equals("admin")){
            View button = findViewById(R.id.requestsButton);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(this);
        }
        welcomeText.setText("Welcome " + user.getFirstName() + " " + user.getLastName() +
                "! You are logged in as " + type);
    }

    /**
     * Logs out a user and redirects them to the main activity upon successful signout. Otherwise,
     * the user will be notified of a failure to sign out.
     *
     * @param view The View that triggered the signout button click.
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.requestsButton){
            try{
                Intent requestsLists = new Intent(WelcomePageActivity.this, AdminInboxActivity.class);
                startActivity(requestsLists);
            }
            catch(Exception e){
                Toast.makeText(WelcomePageActivity.this, "Error showing requests lists", Toast.LENGTH_SHORT).show();
                System.out.println(e);
            }
        }
    }
}