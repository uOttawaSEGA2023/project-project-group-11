package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

import android.view.View;

public class PatientAppointmentInfoDisplay_Activity extends AppCompatActivity {
    TextView doctorName, healthCardNumber, appointmentDate, appointmentStartTime, appointmentEndTime, status;
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

        // Initializes TextViews
        doctorName = findViewById(R.id.doctor);
        healthCardNumber = findViewById(R.id.healthCardNumber);
        appointmentDate = findViewById(R.id.appointmentDate);
        appointmentStartTime = findViewById(R.id.appointmentStartTime);
        appointmentEndTime = findViewById(R.id.appointmentEndTime);
        status = findViewById(R.id.status);

        // Sets text views
        doctorName.setText(appointment.getPatient().getFullName());
        healthCardNumber.setText(appointment.getPatient().getHealthCardNumber());
        appointmentDate.setText(appointment.getDate());
        appointmentStartTime.setText(appointment.getStartTime());
        appointmentEndTime.setText(appointment.getEndTime());
        status.setText(appointment.getStatus());



    }

    public void OnClick(View view){
        Intent intent = new Intent(PatientAppointmentInfoDisplay_Activity.this,
                PatientAppointmentActivity.class);

        if(view.getId()==R.id.backButton){
            startActivity(intent);
        }

    }


}