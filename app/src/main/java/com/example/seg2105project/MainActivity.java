package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Creates an intent to either {@link SignUpActivity} or
     * {@link LoginActivity}
     * @param view
     */
    public void onClickButton(View view) {
        // id of button clicked
        int pressID = view.getId();

        // go to sign up page
        if(pressID == R.id.signUpButton){
            Intent i = new Intent(MainActivity.this, AdminInboxActivity.class);
            startActivity(i);
        }
        // go to log in page
        if(pressID == R.id.logInButton){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
}