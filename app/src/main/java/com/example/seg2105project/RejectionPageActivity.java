package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RejectionPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejection_page);
    }

    public void onClickButton(View view){
        int pressID = view.getId();
        // Sends user back to login or signup screen
        if(pressID == R.id.returnToLogin){
            Intent i = new Intent(RejectionPageActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

}