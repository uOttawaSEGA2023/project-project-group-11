package com.example.seg2105project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    //Doctor object
    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_shifts);

        // get database instances
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        // get object of user from previous page
        doctor = (Doctor) getIntent().getExtras().getSerializable("User");

        // get tablayout
        tabLayout = findViewById(R.id.shiftList);

        // get recycler view of lists
        recyclerView = findViewById(R.id.recyclerviewShift);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // display shifts using new item_view xml, ListViewHolder, and ListAdapter
    }

    /**
     * Creates an Alert Dialog to add a shift
     */
    public void createAlertDialog(View view){

        // create alert dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_shift_layout, null);
        dialogBuilder.setView(dialogView);

        // get data from view elements
        final CalendarView calendar = (CalendarView) dialogView.findViewById(R.id.calendar);
        TextView selectedDate = (TextView) dialogView.findViewById(R.id.selectedDate);
        final EditText editTextStartTime  = (EditText) dialogView.findViewById(R.id.editTextStartTime);
        final EditText editTextEndTime  = (EditText) dialogView.findViewById(R.id.editTextEndTime);
        final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddShift);

        // display alert dialog
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // set data for calendar
        calendar.setOnDateChangeListener(new CalendarView
                .OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = (month + 1) + "-" + day + "-" + year;
                selectedDate.setText("Date Chosen: " + date);
            }
        });

        // add a shift when button is clicked
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addShift();
            }
        });

        // if using calendar, check if specified date and time has passed and prevent from being clicked
        // Update the start-time and end-time so that they are 30-minute incremented every time one is updated
        // call addShift()
    }

    /**
     * Adds a shift for a Doctor object.
     */
    public void addShift() {
        // get data from Views (date, startTime, endTime)
        // If not using calendar, check if the specified date and time that the Doctor wants to add has passed
        // Check double booking (shift being added must not conflict with an existing one)
        // create a Shift object
        // add Shift object to shift list instance
        //doctor.addShift(shift);

        // update list in database
        databaseReference.child("users").child(mAuth.getUid()).child("shifts").setValue(doctor.getShifts());
    }

    /**
     * Deletes a shift for a Doctor object.
     */
    public void deleteShift() {
        // get location of shift that has been clicked
        // delete shift from shift list
        //doctor.deleteShift(shift);

        // update list in database
        databaseReference.child("users").child(mAuth.getUid()).child("shifts").setValue(doctor.getShifts());
    }

}