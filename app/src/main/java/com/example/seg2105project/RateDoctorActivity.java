package com.example.seg2105project;// RateDoctorActivity.java


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

                Doctor doctor = appointment.getDoctor();

                // Save the rating to the database
                saveDoctorRating(doctor, rating);

                Intent backToMainPage = new Intent(RateDoctorActivity.this, WelcomePageActivity.class);
                startActivity(backToMainPage);
            }
        });
    }


    private void saveDoctorRating(Doctor doctor, float ratingValue) {

        databaseReference.child("users").child(mAuth.getUid()).child("doctorRatings")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Rating rating = new Rating(doctor, ratingValue);

                            appointment.getPatient().addRating(rating);

                            int totalRatings = appointment.getPatient().getRatings().size();

                            databaseReference.child("users").child(mAuth.getUid()).child("doctorRatings").
                                    child(String.valueOf(totalRatings-1)).child("rating").setValue(ratingValue);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}


