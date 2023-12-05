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

/**
 * This Activity class represents the list of appointments for a Patient.
 */
public class PatientAppointmentActivity extends AppCompatActivity {

    // Firebase Real-Time Database for holding database
    private static FirebaseDatabase database;
    // reference variable to database
    private static DatabaseReference databaseReference;
    // reference variable to Firebase Authentication
    private static FirebaseAuth mAuth;

    //ListView to display the list.
    ListView appointment;
    TabLayout tabLayout;

    // the index of the tab (upcoming/past)
    int onList;

    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment);

        // get database instances
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        // get object of user from previous page
        patient = (Patient) getIntent().getExtras().getSerializable("User");

        //Get tab layout to cycle between upcoming and past appointments
        tabLayout = findViewById(R.id.tabLayout);

        // get listview element for shifts
        appointment = findViewById(R.id.appointments);

        // Goes to view appointment information page
        appointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PatientAppointmentActivity.this,
                        PatientAppointmentInfoDisplay_Activity.class);
                intent.putExtra("User", patient);
                intent.putExtra("Index", i);

                // show upcoming appointments information
                if(onList == 0) {
                    intent.putExtra("Appointment", patient.getUpcomingAppointments().get(i));
                }
                // shows past appointment information
                else{
                    intent.putExtra("Appointment", patient.getPastAppointments().get(i));
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayUpcoming();
        // switching between upcoming and past appointments
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if(position == 0){
                    onList = 0;
                    displayUpcoming();
                }
                else if(position == 1){
                    onList = 1;
                    displayPastAppointment();

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
     * Handles returning to the Welcome Page.
     * @param view
     */
    public void OnClickReturn(View view){
        if(view.getId() == R.id.returnPatientPage){
            Intent intent = new Intent(PatientAppointmentActivity.this, WelcomePageActivity.class);
            intent.putExtra("User",patient);
            intent.putExtra("Type", "patient");
            startActivity(intent);
        }
    }

    /**
     * Displays past appointments from the database.
     */
    private void displayPastAppointment() {
        databaseReference.child("users").child(mAuth.getUid()).child("pastAppointments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        patient.getPastAppointments().clear();
                        ArrayList<String> doctorSpecialities = new ArrayList<>();

                        for(DataSnapshot ds : snapshot.getChildren()){
                            DataSnapshot dr = ds.child("doctor");
                            DataSnapshot doctorAddress = ds.child("address");
                            Address address = new Address(doctorAddress.child("postalAddress").getValue(String.class),
                                    doctorAddress.child("postalCode").getValue(String.class),
                                    doctorAddress.child("city").getValue(String.class),
                                    doctorAddress.child("province").getValue(String.class),
                                    doctorAddress.child("country").getValue(String.class));

                            // Iterates through
                            for(DataSnapshot d :dr.child("specialties").getChildren()){
                                doctorSpecialities.add(d.getValue(String.class));
                            }
                            // gets doctor associated with an appointment
                            Doctor doctor = new Doctor(dr.child("firstName").getValue(String.class),
                                    dr.child("lastName").getValue(String.class),
                                    dr.child("email").getValue(String.class),
                                    dr.child("accountPassword").getValue(String.class),
                                    dr.child("phoneNumber").getValue(String.class),address,
                                    dr.child("employeeNumber").getValue(String.class),
                                    doctorSpecialities);
                            String status = ds.child("status").getValue(String.class);
                            String date = ds.child("date").getValue(String.class);
                            String startTime = ds.child("startTime").getValue(String.class);
                            String endTime = ds.child("endTime").getValue(String.class);


                            Patient patient1 = new Patient(patient.getFirstName(),patient.getLastName(),patient.getEmail(),
                                    patient.getAccountPassword(),patient.getPhoneNumber(),patient.getAddress(),
                                    patient.getHealthCardNumber());
                            Appointment appointment1 = new Appointment(patient1,doctor,status,date,startTime,endTime);
                            patient.addPastAppointment(appointment1);




                        }
                        UpcomingAppointmentList appointmentAdapter = new UpcomingAppointmentList(PatientAppointmentActivity.this,
                                patient.getPastAppointments());
                        appointment.setAdapter(appointmentAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    /**
     * Displays upcoming appointments for the Patient.
     */
    private void displayUpcoming(){
        databaseReference.child("users").child(mAuth.getUid()).child("upcomingAppointments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        patient.getUpcomingAppointments().clear();
                        ArrayList<String> doctorSpecialities = new ArrayList<>();

                        for(DataSnapshot ds : snapshot.getChildren()){
                            DataSnapshot dr = ds.child("doctor");
                            DataSnapshot doctorAddress = ds.child("address");
                            Address address = new Address(doctorAddress.child("postalAddress").getValue(String.class),
                                    doctorAddress.child("postalCode").getValue(String.class),
                                    doctorAddress.child("city").getValue(String.class),
                                    doctorAddress.child("province").getValue(String.class),
                                    doctorAddress.child("country").getValue(String.class));

                            // Iterates through doctors specialties
                            for(DataSnapshot d :dr.child("specialties").getChildren()){
                                doctorSpecialities.add(d.getValue(String.class));
                            }
                            // gets doctor associated with an appointment
                            Doctor doctor = new Doctor(dr.child("firstName").getValue(String.class),
                                    dr.child("lastName").getValue(String.class),
                                    dr.child("email").getValue(String.class),
                                    dr.child("accountPassword").getValue(String.class),
                                    dr.child("phoneNumber").getValue(String.class),address,
                                    dr.child("employeeNumber").getValue(String.class),
                                    doctorSpecialities);
                            String status = ds.child("status").getValue(String.class);
                            String date = ds.child("date").getValue(String.class);
                            String startTime = ds.child("startTime").getValue(String.class);
                            String endTime = ds.child("endTime").getValue(String.class);


                            Patient patient1 = new Patient(patient.getFirstName(),patient.getLastName(),patient.getEmail(),
                                    patient.getAccountPassword(),patient.getPhoneNumber(),patient.getAddress(),
                                    patient.getHealthCardNumber());
                            Appointment appointment1 = new Appointment(patient1,doctor,status,date,startTime,endTime);
                            patient.addUpcomingAppointment(appointment1);

                            UpcomingAppointmentList appointmentAdapter = new UpcomingAppointmentList(PatientAppointmentActivity.this,
                                    patient.getUpcomingAppointments());
                            appointment.setAdapter(appointmentAdapter);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}