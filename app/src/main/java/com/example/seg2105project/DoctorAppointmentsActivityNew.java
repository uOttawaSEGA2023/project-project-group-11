package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Activity class displaying the list of appointments of a Doctor.
 */
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

    // toggle for approving appointments automatically
    Switch approveSwitch;

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


        // when an individual appointment is clicked, go to a new page with more info
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

        // Appointment approve all switch
        approveSwitch = findViewById(R.id.switch1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // display upcoming appointments
        displayUpcoming(false);

        //Creates the ability to switch between upcoming and past appointments.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                // upcoming tab
                if (position == 0) {
                    onList = 0;
                    displayUpcoming(false);

                // past tab
                } else if (position == 1) {
                    onList = 1;
                    // Display past appointments
                    databaseReference.child("users").child(mAuth.getUid()).child("pastAppointments")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    doctor.getPastAppointments().clear();
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

        // when the approve all switch is toggled
        approveSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){

                // redisplay data with updated status
                displayUpcoming(true);

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
            Intent signOut = new Intent(DoctorAppointmentsActivityNew.this, MainActivity.class);
            startActivity(signOut);
            Toast.makeText(DoctorAppointmentsActivityNew.this, "Logged out!", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(DoctorAppointmentsActivityNew.this, "Error logging out", Toast.LENGTH_SHORT).show();
            System.out.println(e);
        }
    }

    /**
     * Displays upcoming appointments as a ListView
     */
    private void displayUpcoming(boolean switchOn) {
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
                            // if toggle for automatic approval is on
                            if(switchOn){
                                status = "Accepted";
                            }
                            String date = ds.child("date").getValue(String.class);
                            String startTime = ds.child("startTime").getValue(String.class);
                            String endTime = ds.child("endTime").getValue(String.class);
                            Doctor newDoctor = new Doctor(doctor.getFirstName(), doctor.getLastName(),
                                    doctor.getEmail(), doctor.getAccountPassword(), doctor.getPhoneNumber(),
                                    doctor.getAddress(), doctor.getEmployeeNumber(), doctor.getSpecialties());
                            Appointment appointmentO = new Appointment(patient, newDoctor, status,
                                    date, startTime, endTime);
                            doctor.addUpcomingAppointment(appointmentO);
                            // update for patient
                            if(switchOn) {
                                // update patient status to accepted
                                Query emailQuery = databaseReference.child("users").orderByChild("email")
                                        .equalTo(appointmentO.getPatient().getEmail());
                                emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        // user object corresponding to the email
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            // upcoming appointments of the user
                                            DataSnapshot userAppointments = ds.child("upcomingAppointments");
                                            // loop through each appointment until the appointment is found
                                            for (DataSnapshot currentAppointment : userAppointments.getChildren()) {
                                                Patient p = new Patient(currentAppointment.child("patient").child("firstName").getValue().toString(),
                                                        currentAppointment.child("patient").child("lastName").getValue().toString(),
                                                        currentAppointment.child("patient").child("email").getValue().toString(),
                                                        currentAppointment.child("patient").child("accountPassword").getValue().toString(),
                                                        currentAppointment.child("patient").child("phoneNumber").getValue().toString(),
                                                        appointmentO.getPatient().getAddress(),
                                                        currentAppointment.child("patient").child("healthCardNumber").getValue().toString());
                                                Doctor d = new Doctor(currentAppointment.child("doctor").child("firstName").getValue().toString(),
                                                        currentAppointment.child("doctor").child("lastName").getValue().toString(),
                                                        currentAppointment.child("doctor").child("email").getValue().toString(),
                                                        currentAppointment.child("doctor").child("accountPassword").getValue().toString(),
                                                        currentAppointment.child("doctor").child("phoneNumber").getValue().toString(),
                                                        appointmentO.getDoctor().getAddress(),
                                                        currentAppointment.child("doctor").child("employeeNumber").getValue().toString(),
                                                        appointmentO.getDoctor().getSpecialties());
                                                Appointment app = new Appointment(p, d, currentAppointment.child("status").getValue().toString(),
                                                        currentAppointment.child("date").getValue().toString(),
                                                        currentAppointment.child("startTime").getValue().toString(),
                                                        currentAppointment.child("endTime").getValue().toString());
                                                // if the appointments in the database match
                                                if (app.equals(appointmentO)) {
                                                    // update the status to accepted
                                                    databaseReference.child("users").child(ds.getKey()).
                                                            child("upcomingAppointments").child(currentAppointment.getKey())
                                                            .child("status")
                                                            .setValue(DoctorAppointmentInfoDisplay_Activity.AppointmentStatus.ACCEPTED.getStatusText());
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        // update data in database if any changes occur
                        databaseReference.child("users").child(mAuth.getUid())
                                .child("upcomingAppointments")
                                .setValue(doctor.getUpcomingAppointments());

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
