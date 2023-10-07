package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class WelcomePageActivity extends AppCompatActivity {

    // variable to display message
    TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        // get User object from log in
        User user = (User) getIntent().getExtras().getSerializable("User");
        // get type of User
        String type = getIntent().getExtras().getString("Type");
        welcomeText = (TextView)findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + user.getFirstName() + " " + user.getLastName() +
                "! You are logged in as " + type);
    }
}