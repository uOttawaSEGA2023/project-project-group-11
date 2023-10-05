package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class WelcomePageActivity extends AppCompatActivity {

    TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        User user = (User) getIntent().getExtras().getSerializable("User");
        String type = getIntent().getExtras().getString("Type");
        welcomeText = (TextView)findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome " + user.getFirstName() + " " + user.getLastName() +
                "! You are logged in as a " + type);
    }
}