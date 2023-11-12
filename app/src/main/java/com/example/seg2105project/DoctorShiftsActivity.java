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
        final CalendarView calendarView = dialogView.findViewById(R.id.calendar);
        TextView selectedDate = dialogView.findViewById(R.id.selectedDate);
        final EditText editTextStartTime  = dialogView.findViewById(R.id.editTextStartTime);
        final EditText editTextEndTime  = dialogView.findViewById(R.id.editTextEndTime);
        final Button buttonAdd = dialogView.findViewById(R.id.buttonAddShift);
        final TextView errorText = dialogView.findViewById(R.id.errorText);

        // display alert dialog
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // set data for calendar
        calendarView.setMinDate(new Date().getTime());
        calendarView.setOnDateChangeListener(new CalendarView
                .OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year,
                                            int month, int day) {
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
                boolean added = addShift(selectedDate.getText().toString().substring(13),
                        editTextStartTime.getText().toString(), editTextEndTime.getText().toString());

                if(added) {
                    b.dismiss();
                }
                else {
                    errorText.setText("ERROR: Shift conflicts with another shift or information " +
                            "is not entered correctly.");
                }
            }
        });
    }


    /**
     * Adds a shift for a Doctor object.
     * @param date the date of the shift
     * @param startTime the start-time of the shift
     * @param endTime the end-time of the shift
     * @return
     */
    private boolean addShift(String date, String startTime, String endTime) {

        if(date.equals("") || startTime.equals("") || endTime.equals("")){
            return false;
        }

        // create a Shift object
        Shift shift = new Shift(date, startTime, endTime);

        // check if shift being added conflicts with an existing shift)
        if (doctor.getShifts().contains(shift)){
            return false;
        }

        // add Shift object to shift list instance
        doctor.addShift(shift);

        // update list in database
        databaseReference.child("users").child(mAuth.getUid()).child("shifts")
                .setValue(doctor.getShifts());
        return true;
    }


    /**
     * Deletes a shift for a Doctor object.
     * @param date the date of the shift
     * @param startTime the start-time of the shift
     * @param endTime the end-time of the shift
     */
    private void deleteShift(String date, String startTime, String endTime) {

        // create a Shift object
        Shift shift = new Shift(date, startTime, endTime);

        // delete shift from shift list
        doctor.deleteShift(shift);

        // update list in database
        databaseReference.child("users").child(mAuth.getUid()).child("shifts")
                .setValue(doctor.getShifts());
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

            // sets the end time to be 30 minutes after the start time if the time has not changed yet
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

            // sets the start time to be 30 minutes before the end time if the time has not changed yet
            if(!editTextStartTime.getText().toString().equals(hourString + ":" + minuteString)){
                editTextStartTime.setText(hourString + ":" + minuteString);
            }

        }
    }

}