package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorAppointmentsActivityNew extends AppCompatActivity {

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
        setContentView(R.layout.activity_doctor_appointments_new);

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

        /* TEST CASE: REMEMBER TO COMMENT THIS OUT ONCE THE CASE IS IN THE DATABASE OR AN
         ERROR WILL OCCUR */
//        Address address = new Address("123 Anywhere St.", "A1A1A1",
//                "Ottawa", "Ontario", "Canada");
//        Patient newPatient = new Patient("Test", "One", "testone@gmail.com",
//                "test123", "12345678", address, "123456789");
//        Doctor newDoctor = new Doctor(doctor.getFirstName(), doctor.getLastName(), doctor.getEmail(),
//                doctor.getAccountPassword(),doctor.getPhoneNumber(), doctor.getAddress(),
//                doctor.getEmployeeNumber(), doctor.getSpecialties());
//        Appointment appoint = new Appointment(newPatient, newDoctor, "Not Approved Yet",
//                "01-03-2024", "01:30","10:30");
//        doctor.addUpcomingAppointment(appoint);
//        databaseReference.child("users").child(mAuth.getUid())
//                .child("upcomingAppointments").setValue(doctor.getUpcomingAppointments());

        // when an individual appointment is clicked
        appointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DoctorAppointmentsActivityNew.this,
                        DoctorAppointmentInfoDisplay_Activity.class);
                intent.putExtra("User", doctor);
                intent.putExtra("Index", i);
                if(onList == 0) {
                    intent.putExtra("Appointment", doctor.getUpcomingAppointments().get(i));
                }
                else{
                    intent.putExtra("Appointment", doctor.getPastAppointments().get(i));
                }
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // display upcoming appointments
        displayUpcoming();
        //Creates the ability to switch between upcoming and past appointments.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                // upcoming tab
                if (position == 0) {
                    onList = 0;
                    displayUpcoming();

                // past tab
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
                                        DataSnapshot dsP = ds.child("patient");
                                        DataSnapshot dsA = dsP.child("address");
                                        Address address = new Address(dsA.child("postalAddress").getValue(String.class),
                                                dsA.child("postalCode").getValue(String.class),
                                                dsA.child("city").getValue(String.class),
                                                dsA.child("province").getValue(String.class),
                                                dsA.child("country").getValue(String.class));
                                        Patient patient = new Patient(dsP.child("firstName").getValue(String.class),
                                                dsP.child("lastName").getValue(String.class),
                                                dsP.child("email").getValue(String.class),
                                                dsP.child("accountPassword").getValue(String.class),
                                                dsP.child("phoneNumber").getValue(String.class),
                                                address,
                                                dsP.child("healthCardNumber").getValue(String.class));
                                        String status = ds.child("status").getValue(String.class);
                                        String date = ds.child("date").getValue(String.class);
                                        String startTime = ds.child("startTime").getValue(String.class);
                                        String endTime = ds.child("endTime").getValue(String.class);
                                        Appointment appointmentO = new Appointment(patient, doctor, status, date, startTime, endTime);
                                        doctor.addPastAppointment(appointmentO);
                                    }
                                    UpcomingAppointmentList appointmentAdapter = new UpcomingAppointmentList(DoctorAppointmentsActivityNew.this,
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

    /**
     * Displays upcoming appointments as a ListView
     */
    private void displayUpcoming() {
        // display upcoming appointments
        databaseReference.child("users").child(mAuth.getUid()).child("upcomingAppointments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        doctor.getUpcomingAppointments().clear();
                        // display appointment list
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            DataSnapshot dsP = ds.child("patient");
                            DataSnapshot dsA = dsP.child("address");
                            Address address = new Address(dsA.child("postalAddress").getValue(String.class),
                                    dsA.child("postalCode").getValue(String.class),
                                    dsA.child("city").getValue(String.class),
                                    dsA.child("province").getValue(String.class),
                                    dsA.child("country").getValue(String.class));
                            Patient patient = new Patient(dsP.child("firstName").getValue(String.class),
                                    dsP.child("lastName").getValue(String.class),
                                    dsP.child("email").getValue(String.class),
                                    dsP.child("accountPassword").getValue(String.class),
                                    dsP.child("phoneNumber").getValue(String.class),
                                    address,
                                    dsP.child("healthCardNumber").getValue(String.class));
                            String status = ds.child("status").getValue(String.class);
                            String date = ds.child("date").getValue(String.class);
                            String startTime = ds.child("startTime").getValue(String.class);
                            String endTime = ds.child("endTime").getValue(String.class);
                            Appointment appointmentO = new Appointment(patient, doctor, status, date, startTime, endTime);
                            doctor.addUpcomingAppointment(appointmentO);
                        }
                        UpcomingAppointmentList appointmentAdapter = new UpcomingAppointmentList(DoctorAppointmentsActivityNew.this,
                                doctor.getUpcomingAppointments());
                        appointment.setAdapter(appointmentAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
