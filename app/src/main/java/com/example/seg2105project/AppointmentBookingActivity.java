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
        updateUserAppointments();

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

        Button bookB = findViewById(R.id.BookButton);
        Button backB = findViewById(R.id.backButton);

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
    public void goBackToSearch() {
        Intent i = new Intent(AppointmentBookingActivity.this, BookAppointment.class);
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


}