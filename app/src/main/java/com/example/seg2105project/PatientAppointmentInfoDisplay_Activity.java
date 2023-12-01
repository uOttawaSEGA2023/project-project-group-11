package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity class to display appointment information for patients.
 */
public class PatientAppointmentInfoDisplay_Activity extends AppCompatActivity {
    TextView doctorName, appointmentDate, appointmentStartTime, appointmentEndTime, doctorSpecialities;
    Appointment appointment;

    User user;

    int index; // index of appointment in list

    // Firebase Real-Time Database for holding database
    private static FirebaseDatabase database;
    // reference variable to database
    private static DatabaseReference databaseReference;

    private static FirebaseAuth mAuth;

    // SimpleDateFormat for parsing time
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_info_display);

        user = (User) getIntent().getExtras().getSerializable("User");

        // Retrieve appointment object from intent
        appointment = (Appointment) getIntent().getExtras().getSerializable("Appointment");

        index = getIntent().getExtras().getInt("Index");

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        // Initialize TextViews
        doctorName = findViewById(R.id.doctor);
        doctorSpecialities = findViewById(R.id.specialties);
        appointmentDate = findViewById(R.id.appointmentDate);
        appointmentStartTime = findViewById(R.id.appointmentStartTime);
        appointmentEndTime = findViewById(R.id.appointmentEndTime);

        // Set text for appointment details dynamically
        doctorName.setText("Doctor: " + appointment.getDoctor().getFullName());
        doctorSpecialities.setText("Specialities" + appointment.getDoctor().getSpecialties().toString());
        appointmentDate.setText("Date: " + appointment.getDate());
        appointmentStartTime.setText("Start Time: " + appointment.getStartTime());
        appointmentEndTime.setText("End Time: " + appointment.getEndTime());

        Button backButton = findViewById(R.id.backButton);
        Button cancelAppointmentButton = findViewById(R.id.cancelAppointmentButton);
        Button rateDoctorButton = findViewById(R.id.rateDoctorButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancelAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelButtonClick(v);
            }
        });

        rateDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.rateDoctorButton){
                    Intent rateDoctorPage = new Intent(PatientAppointmentInfoDisplay_Activity.this, RateDoctorActivity.class);
                    startActivity(rateDoctorPage);
                    System.out.println("Does this work?"); }
            }
        });
    }

    public void onCancelButtonClick(View view) {
        if (!isAppointmentWithin60Minutes(appointment.getStartTime())) {
            // TODO: remove appointment from data base

            // Display a toast message
            Toast.makeText(this, "Appointment canceled!", Toast.LENGTH_SHORT).show();
        } else {
            // Display a toast message indicating that cancellation is not allowed
            Toast.makeText(this, "Cannot cancel appointment within 60 minutes!", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isAppointmentWithin60Minutes(String appointmentTime) {
        try {
            Date appointmentDateTime = timeFormat.parse(appointmentTime);

            Calendar currentCalendar = Calendar.getInstance();
            Date currentDate = currentCalendar.getTime();

            long timeDifferenceMillis = appointmentDateTime.getTime() - currentDate.getTime();

            long timeDifferenceMinutes = timeDifferenceMillis / (60 * 1000);

            return timeDifferenceMinutes <= 60;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
