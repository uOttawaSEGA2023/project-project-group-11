package com.example.seg2105project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AppointmentBookingActivity extends AppCompatActivity {

    private static FirebaseDatabase database;
    // reference variable to database
    private static DatabaseReference databaseReference;

    private static FirebaseAuth mAuth;

    Patient user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        // Retrieving the information of the appointment and the user/patient wanting to view the
        // information of the appointment

        AppointmentAvailView appointment = (AppointmentAvailView) getIntent().getExtras().getSerializable("Appointment");
        user = (Patient) getIntent().getExtras().getSerializable("User");
        // get current appointments for the patient
        updateUserAppointments();
        // get current appointments for the doctor
        getCurrentDoctorAppointments(appointment);

        // Displaying the information on the layout
        TextView docName = findViewById(R.id.doctor);
        docName.setText("Doctor : "  + appointment.getDoctorName());

        TextView specialty = findViewById(R.id.specialties);
        specialty.setText("Specialty Requested : "  + appointment.getSpecialty());

        TextView date = findViewById(R.id.appointmentDate);
        date.setText("Day of appointment : "  + appointment.getDate());

        TextView startT = findViewById(R.id.appointmentStartTime);
        startT.setText("Appointment start time : "  + appointment.getStartTime());

        TextView endT = findViewById(R.id.appointmentEndTime);
        endT.setText("Appointment end time : "  + appointment.getEndTime());

        // book an appointment
        Button bookB = findViewById(R.id.BookButton);

        bookB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Patient p = new Patient(user.getFirstName(), user.getLastName(), user.getEmail(),
                        user.getAccountPassword(), user.getPhoneNumber(), user.getAddress(), user.getHealthCardNumber());

                appointment.setPatient(p);

                Doctor d = new Doctor(appointment.getDoctor().getFirstName(), appointment.getDoctor().getLastName(),
                        appointment.getDoctor().getEmail(), appointment.getDoctor().getAccountPassword(),
                        appointment.getDoctor().getPhoneNumber(), appointment.getDoctor().getAddress(),
                        appointment.getDoctor().getEmployeeNumber(), appointment.getDoctor().getSpecialties());

                Appointment app = new Appointment(p,d, appointment.getStatus(), appointment.getDate(),
                        appointment.getStartTime(), appointment.getEndTime());


                // add appointment to patient and doctor
                user.addUpcomingAppointment(app);

                appointment.getDoctor().addUpcomingAppointment(app);

                updatePatientUpcomingAppointments(user);

                updateDoctorUpcomingAppointments(appointment.getDoctor());

                Toast.makeText(AppointmentBookingActivity.this, "Appointment Booked!", Toast.LENGTH_SHORT).show();
                goBackToAppointments();
            }
        });




    }
    private void updatePatientUpcomingAppointments(Patient patient) {

        // Update the patient's upcoming appointments
        databaseReference.child("users").child(mAuth.getUid()).child("upcomingAppointments")
                .setValue(patient.getUpcomingAppointments());
    }

    private void updateDoctorUpcomingAppointments(Doctor doctor) {

        // find the doctor in the database by email
        Query emailQuery = databaseReference.child("users").orderByChild("email").equalTo(doctor.getEmail());

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // update doctor data in database
                for(DataSnapshot ds: snapshot.getChildren()) {

                    databaseReference.child("users").child(ds.getKey()).child("upcomingAppointments").setValue(doctor.getUpcomingAppointments());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Go back to Appointment Search
     */
    public void goBackToSearch(View view) {
        Intent i = new Intent(AppointmentBookingActivity.this, BookAppointment.class);
        i.putExtra("User", user);
        i.putExtra("Type", "patient");
        startActivity(i);
    }


    /**
     * Go back to Patient Appointments
     */
    private void goBackToAppointments() {
        Intent i = new Intent(AppointmentBookingActivity.this, PatientAppointmentActivity.class);
        i.putExtra("User", user);
        startActivity(i);
    }

    /**
     * Get the list of appointments that the patient currently has from the Firebase
     */
    private void updateUserAppointments() {
        databaseReference.child("users").child(mAuth.getUid()).child("upcomingAppointments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // getting each individual appointment
                for(DataSnapshot ds: snapshot.getChildren()) {
                    Patient p = new Patient(user.getFirstName(), user.getLastName(), user.getEmail(),
                            user.getAccountPassword(), user.getPhoneNumber(), user.getAddress(), user.getHealthCardNumber());
                    Doctor d = new Doctor(ds.child("doctor").child("firstName").getValue().toString(),
                            ds.child("doctor").child("lastName").getValue().toString(),
                            ds.child("doctor").child("email").getValue().toString(),
                            ds.child("doctor").child("accountPassword").getValue().toString(),
                            ds.child("doctor").child("phoneNumber").getValue().toString(), null,
                            ds.child("doctor").child("employeeNumber").getValue().toString(), null);
                    Appointment app = new Appointment(p,d, ds.child("status").getValue().toString(),
                            ds.child("date").getValue().toString(),
                            ds.child("startTime").getValue().toString(),
                            ds.child("endTime").getValue().toString());
                    user.addUpcomingAppointment(app);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Get the list of appointments that the doctor currently has in the database
     */
    private void getCurrentDoctorAppointments(AppointmentAvailView appointment) {
        // find the doctor in the database by email
        Query emailQuery = databaseReference.child("users").orderByChild("email").equalTo(appointment.getDoctor().getEmail());

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
                       Doctor d = new Doctor(appointment.getDoctor().getFirstName(), appointment.getDoctor().getLastName(),
                               appointment.getDoctor().getEmail(), appointment.getDoctor().getAccountPassword(),
                               appointment.getDoctor().getPhoneNumber(), appointment.getDoctor().getAddress(),
                               appointment.getDoctor().getEmployeeNumber(), appointment.getDoctor().getSpecialties());
                       Appointment app = new Appointment(p,d, currentAppointment.child("status").getValue().toString(),
                               currentAppointment.child("date").getValue().toString(),
                               currentAppointment.child("startTime").getValue().toString(),
                               currentAppointment.child("endTime").getValue().toString());
                       appointment.getDoctor().addUpcomingAppointment(app);
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}