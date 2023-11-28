package com.example.seg2105project;// RateDoctorActivity.java


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class RateDoctorActivity extends AppCompatActivity {

    // Assume appointment is a member variable in your class
    private Appointment appointment;

    // Assume databaseReference is a member variable pointing to your Firebase database
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_doctor);

        // Get views from the layout
        RatingBar doctorRatingBar = findViewById(R.id.doctorRatingBar);
        Button submitRatingButton = findViewById(R.id.submitRatingButton);

        // Set click listener for the submitRatingButton
        submitRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the rating submission or any other action
                float rating = doctorRatingBar.getRating();

                // Retrieve doctor ID and rating value
                String doctorId = appointment.getDoctor().getEmployeeNumber();

                // Save the rating to the database
                saveDoctorRating(doctorId, rating);
            }
        });
    }

    private void saveDoctorRating(String doctorId, float ratingValue) {
        // Assuming you have a "doctors" node in your database
        DatabaseReference doctorRef = databaseReference.child("doctors").child(doctorId);

        // Retrieve existing ratings and count from the database
        doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long totalRatings = dataSnapshot.child("totalRatings").getValue(Long.class);
                    float averageRating = dataSnapshot.child("averageRating").getValue(Float.class);

                    // Update total ratings and calculate new average rating
                    totalRatings++;
                    averageRating = ((averageRating * (totalRatings - 1)) + ratingValue) / totalRatings;

                    // Update the database with the new ratings
                    doctorRef.child("totalRatings").setValue(totalRatings);
                    doctorRef.child("averageRating").setValue(averageRating);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
