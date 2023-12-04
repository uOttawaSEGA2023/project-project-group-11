package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointmentBookingActivity extends AppCompatActivity {

    private static FirebaseDatabase database;
    // reference variable to database
    private static DatabaseReference databaseReference;

    private static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking);

        // Retrieving the information of the appointment and the user/patient wanting to view the
        // information of the appointment

        AppointmentAvailView appointment = (AppointmentAvailView) getIntent().getExtras().getSerializable("Appointment");
        Patient user = (Patient) getIntent().getExtras().getSerializable("User");

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
                appointment.setPatient(user);
                Appointment app = new Appointment(user,appointment.getDoctor(), appointment.getStatus(), appointment.getDate(), appointment.getStartTime(), appointment.getEndTime());
                user.addUpcomingAppointment(app);
                appointment.getDoctor().addUpcomingAppointment(app);

                updatePatientUpcomingAppointments(user);

                updateDoctorUpcomingAppointments(appointment.getDoctor());

            }
        });




    }
    private void updatePatientUpcomingAppointments(Patient patient) {
        // Initialize Firebase database and reference
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("patients");

        // Get the unique identifier (UID) of the patient
        String patientUid = mAuth.getCurrentUser().getUid();

        // Update the patient's upcoming appointments
        databaseReference.child(patientUid).child("upcomingAppointments").setValue(patient.getUpcomingAppointments());
    }

    private void updateDoctorUpcomingAppointments(Doctor doctor) {
        // Initialize Firebase database and reference
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        // Get the unique identifier (UID) of the doctor
        //String doctorUid = doctor.getUid();

        // Update the doctor's upcoming appointments in the "doctors" node
        //databaseReference.child(doctorUid).child("upcomingAppointments").setValue(doctor.getUpcomingAppointments());
    }

}