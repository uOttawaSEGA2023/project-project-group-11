package com.example.seg2105project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This Activity class displays the list of shifts for a Doctor.
 */
public class DoctorShiftsActivity extends AppCompatActivity{

    // Firebase Real-Time Database for holding database
    private static FirebaseDatabase database;
    // reference variable to database
    private static DatabaseReference databaseReference;
    // reference variable to Firebase Authentication
    private static FirebaseAuth mAuth;

    // listview to display list
    ListView shiftList;

    //Doctor object
    private Doctor doctor;

    String[] times = {"00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30",
    "04:00", "04:30", "05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30",
    "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
    "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30",
    "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"};


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
        getAppointmentsFromDatabase();

        // get listview element for shifts
        shiftList = findViewById(R.id.shiftList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get shift data from database
        databaseReference.child("users").child(mAuth.getUid()).child("shifts")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctor.getShifts().clear();
                // display shift list
                for(DataSnapshot ds: snapshot.getChildren()){
                    String date = ds.child("date").getValue(String.class);
                    String startTime = ds.child("startTime").getValue(String.class);
                    String endTime = ds.child("endTime").getValue(String.class);
                    Shift shift = new Shift(date, startTime, endTime);
                    doctor.addShift(shift);
                }
                DoctorShiftList shiftAdapter = new DoctorShiftList(DoctorShiftsActivity.this,
                        doctor.getShifts());
                shiftList.setAdapter(shiftAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Calls the method to delete a shift when the delete button is clicked.
     * @param view
     */
    public void clickedDelete(View view) {
        // get specific listview element
        View parent = (View) view.getParent();
        TextView textViewDate = parent.findViewById(R.id.date);
        TextView textViewStartTime = parent.findViewById(R.id.startTime);
        TextView textViewEndTime = parent.findViewById(R.id.endTime);
        // call method to delete shift
        try {
            deleteShift(textViewDate.getText().toString().substring(6),
                    textViewStartTime.getText().toString().substring(12),
                    textViewEndTime.getText().toString().substring(10));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an Alert Dialog to add a shift.
     * @param view
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
        final Spinner startTimeSpinner  = dialogView.findViewById(R.id.startTimeSpinner);
        final Spinner endTimeSpinner  = dialogView.findViewById(R.id.endTimeSpinner);
        final Button buttonAdd = dialogView.findViewById(R.id.buttonAddShift);
        final TextView errorText = dialogView.findViewById(R.id.errorText);

        // display alert dialog
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // indices for spinner position
        final int[] startTimeIndex = new int[1];
        final int[] endTimeIndex = new int[1];

        // start time spinner selection
        startTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                startTimeIndex[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // end time spinner selection
        endTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                endTimeIndex[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // adapter for spinner
        ArrayAdapter ad
                = new ArrayAdapter(this, android.R.layout.simple_spinner_item, times);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        startTimeSpinner.setAdapter(ad);
        endTimeSpinner.setAdapter(ad);

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

        // add a shift when button is clicked
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = selectedDate.getText().toString();
                String startTime = startTimeSpinner.getItemAtPosition(startTimeIndex[0]).toString();
                String endTime = endTimeSpinner.getItemAtPosition(endTimeIndex[0]).toString();
                String[] startSplit = startTime.split(":");
                String[] endSplit = endTime.split(":");
                if(date.length() <13){
                    errorText.setText("ERROR: Please select a date.");
                }
                else if(Integer.parseInt(startSplit[0]) > Integer.parseInt(endSplit[0])
                        || ((Integer.parseInt(startSplit[0]) == Integer.parseInt(endSplit[0]))
                        && (Integer.parseInt(startSplit[1]) >= Integer.parseInt(endSplit[1])))) {
                    errorText.setText("ERROR: Start time must be before the end time.");
                }
                else {
                    date = date.substring(13);
                    boolean added = addShift(date, startTime, endTime);
                    if(added) {
                        b.dismiss();
                    }
                    else {
                        errorText.setText("ERROR: Shift conflicts with another shift.");
                    }
                }
            }
        });
    }

    /**
     * Logs out a user and redirects them to the main activity upon successful signout. Otherwise,
     * the user will be notified of a failure to sign out.
     *
     * @param view The View that triggered the signout button click.
     */
    public void onClickSignOut(View view){
        try{
            mAuth.signOut();
            Intent signOut = new Intent(DoctorShiftsActivity.this, MainActivity.class);
            startActivity(signOut);
            Toast.makeText(DoctorShiftsActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(DoctorShiftsActivity.this, "Error logging out", Toast.LENGTH_SHORT).show();
            System.out.println(e);
        }
    }

    /**
     * Adds a shift for a Doctor object.
     * @param date the date of the shift
     * @param startTime the start-time of the shift
     * @param endTime the end-time of the shift
     * @return
     */
    private boolean addShift(String date, String startTime, String endTime) {

        // create a Shift object
        Shift shift = new Shift(date, startTime, endTime);

        // check if shift being added conflicts with an existing shift
        for(Shift sh: doctor.getShifts()) {
            if(shift.equals(sh)){
                return false;
            }
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
    private void deleteShift(String date, String startTime, String endTime) throws ParseException {
        // checks if shift conflicts with an appointment
        for(Appointment appointment: doctor.getUpcomingAppointments()){
            // appointment same date as a shift
            if(appointment.getDate().equals(date)) {
                //converting Strings to Date objects
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date appointmentStartTime = sdf.parse(appointment.getStartTime());
                Date shiftStart = sdf.parse(startTime);
                Date shiftEnd = sdf.parse(endTime);

                // appointment conflicts with shift
                if(appointmentStartTime.equals(shiftStart) || (appointmentStartTime.after(shiftStart) && appointmentStartTime.before(shiftEnd))){
                    Toast.makeText(DoctorShiftsActivity.this, "ERROR: Cancel all " +
                            "appointments linked with this shift.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        // create a Shift object
        Shift shift = new Shift(date, startTime, endTime);

        // delete shift from shift list
        doctor.deleteShift(shift);

        // update list in database
        databaseReference.child("users").child(mAuth.getUid()).child("shifts")
                .setValue(doctor.getShifts());
    }

    /**
     * Get the list of appointments that the doctor currently has in the database
     */
    private void getAppointmentsFromDatabase() {
        // find the doctor in the database by email
        Query emailQuery = databaseReference.child("users").orderByChild("email").equalTo(doctor.getEmail());

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // doctor object corresponding to the email
                for(DataSnapshot ds: snapshot.getChildren()) {
                    // upcoming appointments of the doctor
                    DataSnapshot doctorAppointments = ds.child("upcomingAppointments");
                    // loop through each appointment and add it to the doctor
                    for(DataSnapshot currentAppointment : doctorAppointments.getChildren()){
                        Patient p = new Patient(currentAppointment.child("patient").child("firstName").getValue().toString(),
                                currentAppointment.child("patient").child("lastName").getValue().toString(),
                                currentAppointment.child("patient").child("email").getValue().toString(),
                                currentAppointment.child("patient").child("accountPassword").getValue().toString(),
                                currentAppointment.child("patient").child("phoneNumber").getValue().toString(),
                                null,
                                currentAppointment.child("patient").child("healthCardNumber").getValue().toString());
                        Doctor d = new Doctor(doctor.getFirstName(), doctor.getLastName(), doctor.getEmail(),
                                doctor.getAccountPassword(), doctor.getPhoneNumber(), doctor.getAddress(),
                                doctor.getEmployeeNumber(), doctor.getSpecialties());
                        Appointment app = new Appointment(p,d, currentAppointment.child("status").getValue().toString(),
                                currentAppointment.child("date").getValue().toString(),
                                currentAppointment.child("startTime").getValue().toString(),
                                currentAppointment.child("endTime").getValue().toString());
                        doctor.addUpcomingAppointment(app);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}