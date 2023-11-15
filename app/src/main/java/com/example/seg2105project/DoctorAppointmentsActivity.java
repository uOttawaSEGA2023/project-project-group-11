package com.example.seg2105project;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorAppointmentsActivity extends AppCompatActivity {

    // Firebase Real-Time Database for holding database
    private static FirebaseDatabase database;
    // reference variable to database
    private static DatabaseReference databaseReference;
    // reference variable to Firebase Authentication
    private static FirebaseAuth mAuth;

    //ListView to display the list.
    ListView appointment;
    TabLayout tabLayout;
    ArrayList<Appointment> listData;
    int onList;


    //Doctor object
    private Doctor doctor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointments);

        // get database instances
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        // get object of user from previous page
        doctor = (Doctor) getIntent().getExtras().getSerializable("User");

        //Get tablayout to cycle between upcoming and past appointments
        tabLayout = findViewById(R.id.tabLayout);

        // get listview element for shifts
        appointment = findViewById(R.id.appointments);

        //Creates the ability to switch between upcoming and past appointments.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    onList = 0;

                    // display upcoming appointments
                    databaseReference.child("users").child(mAuth.getUid()).child("upcomingAppointments")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    doctor.getUpcomingAppointments().clear();
                                    // display shift list
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        Patient patient = ds.child("patient").getValue(Patient.class);
                                        Doctor doctor = ds.child("doctor").getValue(Doctor.class);
                                        String status = ds.child("status").getValue(String.class);
                                        String date = ds.child("date").getValue(String.class);
                                        String startTime = ds.child("startTime").getValue(String.class);
                                        String endTime = ds.child("endTime").getValue(String.class);
                                        Appointment appointmentO = new Appointment(patient, doctor, status, date, startTime, endTime);
                                        doctor.addUpcomingAppointment(appointmentO);
                                    }
                                    UpcomingAppointmentList appointmentAdapter = new UpcomingAppointmentList(DoctorAppointmentsActivity.this,
                                            doctor.getUpcomingAppointments());
                                    appointment.setAdapter(appointmentAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                } else if (position == 1) {
                    onList = 1;
                    // Display past appointments
                    databaseReference.child("users").child(mAuth.getUid()).child("pastAppointments")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    doctor.getPastAppointments().clear();
                                    // display shift list
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        Patient patient = ds.child("patient").getValue(Patient.class);
                                        Doctor doctor = ds.child("doctor").getValue(Doctor.class);
                                        String status = ds.child("status").getValue(String.class);
                                        String date = ds.child("date").getValue(String.class);
                                        String startTime = ds.child("startTime").getValue(String.class);
                                        String endTime = ds.child("endTime").getValue(String.class);
                                        Appointment appointmentO = new Appointment(patient, doctor, status, date, startTime, endTime);
                                        doctor.addUpcomingAppointment(appointmentO);
                                    }
                                    UpcomingAppointmentList appointmentAdapter = new UpcomingAppointmentList(DoctorAppointmentsActivity.this,
                                            doctor.getPastAppointments());
                                    appointment.setAdapter(appointmentAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
