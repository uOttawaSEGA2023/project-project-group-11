package com.example.seg2105project;// RateDoctorActivity.java


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RateDoctorActivity extends AppCompatActivity {

    private Appointment appointment;

    private static FirebaseDatabase database;

    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_doctor);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        // Get views from the layout
        RatingBar doctorRatingBar = findViewById(R.id.doctorRatingBar);
        Button submitRatingButton = findViewById(R.id.submitRatingButton);
        appointment = (Appointment) getIntent().getExtras().getSerializable("Appointment");

        // Set click listener for the submitRatingButton
        submitRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the rating submission or any other action
                float rating = doctorRatingBar.getRating();

                // Retrieve doctor ID and rating value
                Doctor doctorId = appointment.getDoctor();

                // Save the rating to the database
                saveDoctorRating(doctorId, rating);
            }
        });
    }

    private void saveDoctorRating(Doctor doctorId, float ratingValue) {
    }

}

