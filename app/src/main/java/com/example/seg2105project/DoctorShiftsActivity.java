package com.example.seg2105project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorShiftsActivity extends AppCompatActivity {

    // Firebase Real-Time Database for holding database
    private static FirebaseDatabase database;
    // reference variable to database
    private static DatabaseReference databaseReference;
    // reference variable to Firebase Authentication
    private static FirebaseAuth mAuth;

    RecyclerView recyclerView;
    TabLayout tabLayout;

    // shift list instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_shifts);

        // get database instances
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        // get tablayout
        tabLayout = findViewById(R.id.shiftList);

        // get recycler view of lists
        recyclerView = findViewById(R.id.recyclerviewShift);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get user information using the ID of the user from the database
        databaseReference.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // if there are shifts, display them
                if(snapshot.child("shifts").exists()){
                    // get list of shifts from the database
                    // update shift list instance with list of shifts from database
                    // display shifts using new item_view xml, ListViewHolder, and ListAdapter
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Creates an Alert Dialog to add a shift
     */
    public void createAlertDialog(){
        // create alert dialog
        // if using calendar, check if specified date and time has passed and prevent from being clicked
        // Update the start-time and end-time so that they are 30-minute incremented every time one is updated
        // call addShift()
    }

    /**
     * Adds a shift for a Doctor object.
     */
    public void addShift() {
        // get data from Views
        // If not using calendar, check if the specified date and time that the Doctor wants to add has passed
        // Check double booking (shift being added must not conflict with an existing one)
        // create a Shift object
        // add Shift object to shift list instance
        // update list in database
    }

    /**
     * Deletes a shift for a Doctor object.
     *
     */
    public void deleteShift() {
        // get location of shift that has been clicked
        // delete shift from shift list
        // update list in database
    }

}