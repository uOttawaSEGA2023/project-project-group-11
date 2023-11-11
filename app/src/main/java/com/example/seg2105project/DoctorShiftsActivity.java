package com.example.seg2105project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

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
     * Creates an Alert Dialog to add a shift.
     */
    public void createAlertDialog(View view){

        // create alert dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_shift_layout, null);
        dialogBuilder.setView(dialogView);

        // get data from view elements
        final CalendarView calendarView = (CalendarView) dialogView.findViewById(R.id.calendar);
        TextView selectedDate = (TextView) dialogView.findViewById(R.id.selectedDate);
        final EditText editTextStartTime  = (EditText) dialogView.findViewById(R.id.editTextStartTime);
        final EditText editTextEndTime  = (EditText) dialogView.findViewById(R.id.editTextEndTime);
        final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddShift);

        // display alert dialog
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // set data for calendar
        calendarView.setMinDate(new Date().getTime());
        calendarView.setOnDateChangeListener(new CalendarView
                .OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = (month + 1) + "-" + day + "-" + year;
                selectedDate.setText("Date Chosen: " + date);
            }
        });

        // handler for delayed text check
        final Handler handler = new Handler();

        // ensures end time is 30 minutes after start time
        editTextStartTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String startTime = editTextStartTime.getText().toString();
                // split time into hour and minute
                String[] startSplit = startTime.split(":");
                // if the time input is valid, change time after 500 milliseconds
                if(!startTime.equals("") && startSplit.length == 2 && startSplit[1].length() == 2) {
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(() -> {
                        changeTime(editTextStartTime, editTextEndTime, true);
                    }, 500);
                }
            }
        });

        // ensures start time is 30 minutes before end time
        editTextEndTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String endTime = editTextEndTime.getText().toString();
                // split time into hour and minute
                String[] endSplit = endTime.split(":");
                // if the time input is valid, change time after 500 milliseconds
                if(!endTime.equals("") && endSplit.length == 2 && endSplit[1].length() == 2) {
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(() -> {
                        changeTime(editTextStartTime, editTextEndTime, false);
                    }, 500);
                }
            }
        });

        // add a shift when button is clicked
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addShift();
            }
        });
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

    /**
     * Updates the start-time and end-time so that they are 30-minute incremented every time one
     * is updated.
     * @param editTextStartTime object representing the user input of the start time
     * @param editTextEndTime object representing the user input of the end time
     * @param forward if a forward or backward time change should be implemented
     */
    private void changeTime(EditText editTextStartTime, EditText editTextEndTime, boolean forward) {
        if(forward){
            String startTime = editTextStartTime.getText().toString();
            // split time into hour and minute
            String[] startSplit = startTime.split(":");
            int minute = Integer.parseInt(startSplit[1]);
            int hour = Integer.parseInt(startSplit[0]);

            // add an hour if adding 30 minutes to the time causes minute clock
            // to exceed 59
            if (minute >= 30) {
                hour = hour + 1;
            }
            // resets to 0 o'clock if 24 hours is reached
            if (hour > 23) {
                hour = 0;
            }
            // adds 30 minutes
            minute = (minute + 30) % 60;

            // string representations of minute and hour
            String hourString = String.valueOf(hour);
            String minuteString = String.valueOf(minute);

            // adds a 0 in front of single digit times
            if(hour < 10) {
                hourString = "0" + hourString;
            }
            if(minute <10) {
                minuteString = "0" + minuteString;
            }

            // sets the end time to be 30 minutes after the start time if the text has not changed yet
            if(!editTextEndTime.getText().toString().equals(hourString + ":" + minuteString)){
                editTextEndTime.setText(hourString + ":" + minuteString);
            }

        }
        else {
            String endTime = editTextEndTime.getText().toString();
            // split time into hour and minute
            String[] endSplit = endTime.split(":");
            int minute = Integer.parseInt(endSplit[1]);
            int hour = Integer.parseInt(endSplit[0]);

            // subtract an hour if subtracting 30 minutes to the time causes minute clock
            // to be less than 0
            if(minute < 30){
                hour = hour - 1;
            }
            // resets to 0 o'clock if 24 hours is reached
            if(hour < 0) {
                hour = 23;
            }
            // subtracts 30 minutes
            minute = (minute + 30) % 60;

            // string representations of minute and hour
            String hourString = String.valueOf(hour);
            String minuteString = String.valueOf(minute);

            // adds a 0 in front of single digit times
            if(hour < 10) {
                hourString = "0" + hourString;
            }
            if(minute <10) {
                minuteString = "0" + minuteString;
            }

            // sets the start time to be 30 minutes before the end time if the text has not changed yet
            if(!editTextStartTime.getText().toString().equals(hourString + ":" + minuteString)){
                editTextStartTime.setText(hourString + ":" + minuteString);
            }

        }
    }

}