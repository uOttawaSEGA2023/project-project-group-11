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

    public void onClickButton(View view) {
        int pressID = view.getId();
        if(pressID == R.id.signUpButton){
            Intent i = new Intent(MainActivity.this, RegistrationActivityPatient.class);
            startActivity(i);
        }
    }
}