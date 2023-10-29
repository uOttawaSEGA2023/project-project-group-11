package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The RejectionPageActivity class represents the activity displayed when a user's registration is rejected.
 * It provides an option for the user to return to the login or signup screen.
 */
public class RejectionPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejection_page);
    }

    /**
     * Handles the button clicks and sends the user back to the login or create account screen.
     *
     * @param view The View that triggered the button click.
     */
    public void onClickButton(View view){
        int pressID = view.getId();
        // Sends user back to login or signup screen
        if(pressID == R.id.returnToLogin){
            Intent i = new Intent(RejectionPageActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

}