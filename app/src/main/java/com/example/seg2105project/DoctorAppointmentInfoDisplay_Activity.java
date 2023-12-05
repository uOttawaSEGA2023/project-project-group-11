package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity class to display individual appointment information for the Doctor.
 */
public class DoctorAppointmentInfoDisplay_Activity extends AppCompatActivity {

    // UI elements
    TextView patientName, address, healthCardNumber, appointmentDate, appointmentStartTime, appointmentEndTime, status;

    // the appointment to be displayed
    Appointment appointment;

    // the user instance
    User user;

    // index of appointment in list
    int index;

    // Firebase Real-Time Database for holding database
    private static FirebaseDatabase database;

    // reference variable to database
    private static DatabaseReference databaseReference;

    // Firebase Authentication
    private static FirebaseAuth mAuth;

    // SimpleDateFormat for parsing time
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    /**
     * Enum to represent different appointment statuses.
     */
    public enum AppointmentStatus {
        ACCEPTED("Accepted"),
        REJECTED("Rejected"),
        NOT_APPROVED_YET("Not Approved Yet");

        private final String statusText;

        AppointmentStatus(String statusText) {
            this.statusText = statusText;
        }

        public String getStatusText() {
            return statusText;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment_info_display);

        // Retrieve user object from intent
        user = (User) getIntent().getExtras().getSerializable("User");

        // Retrieve appointment object from intent
        appointment = (Appointment) getIntent().getExtras().getSerializable("Appointment");

        // get index of appointment from intent
        index = getIntent().getExtras().getInt("Index");

        // initialize Firebase instances
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        // Initialize TextViews
        patientName = findViewById(R.id.patient);
        address = findViewById(R.id.address);
        healthCardNumber = findViewById(R.id.healthCardNumber);
        appointmentDate = findViewById(R.id.appointmentDate);
        appointmentStartTime = findViewById(R.id.appointmentStartTime);
        appointmentEndTime = findViewById(R.id.appointmentEndTime);
        status = findViewById(R.id.status);

        // Set text for appointment details dynamically
        patientName.setText("Patient Name:" + appointment.getPatient().getFullName());
        address.setText("Address: " + appointment.getPatient().getAddress().toString());
        healthCardNumber.setText("Health Card Number: " + appointment.getPatient().getHealthCardNumber());
        appointmentDate.setText("Date: " + appointment.getDate());
        appointmentStartTime.setText("Start Time: " + appointment.getStartTime());
        appointmentEndTime.setText("End Time: " + appointment.getEndTime());
        status.setText("Status: " + appointment.getStatus());

        // Check appointment status for button visibility
        if (AppointmentStatus.NOT_APPROVED_YET.getStatusText().equals(appointment.getStatus()) &&
                !isPastAppointment(appointment)) { // for pending appointments
            // Display accept and reject buttons
            findViewById(R.id.acceptAppointmentButton).setVisibility(View.VISIBLE);
            findViewById(R.id.rejectAppointmentButton).setVisibility(View.VISIBLE);
        } else { // for rejected appointments
            findViewById(R.id.acceptAppointmentButton).setVisibility(View.INVISIBLE);
            findViewById(R.id.rejectAppointmentButton).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Handle the click event for accept/reject buttons.
     *
     * @param view The clicked view.
     */
    public void onAcceptRejectClick(View view) {
        if (view.getId() == R.id.acceptAppointmentButton) {
            // accept appointment
            handleAppointmentAction("User Accepted!", view);
        } else if (view.getId() == R.id.rejectAppointmentButton) {
            // rejected appointment
            handleAppointmentAction("User Rejected!", view);
        }

        // Update the UI after handling the click event
        updateUI();
        // go back to doctor inbox
        navigateToDoctorInbox(view);
    }

    /**
     * Changes the status of an appointment based on the button clicked
     *
     * @param toastMessage the message to be sent on the toast
     * @param view         the button clicked
     */
    public void handleAppointmentAction(String toastMessage, View view) {

        // accepted
        if (view.getId() == R.id.acceptAppointmentButton) {

            // updating the database with accepted status
            if (!isPastAppointment(appointment)) {
                // update doctor status to accepted
                databaseReference.child("users").child(mAuth.getUid()).
                        child("upcomingAppointments").child(String.valueOf(index))
                        .child("status")
                        .setValue(AppointmentStatus.ACCEPTED.getStatusText());

                // update patient status to accepted
                Query emailQuery = databaseReference.child("users").orderByChild("email")
                        .equalTo(appointment.getPatient().getEmail());
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
                                        appointment.getPatient().getAddress(),
                                        currentAppointment.child("patient").child("healthCardNumber").getValue().toString());
                                Doctor d = new Doctor(currentAppointment.child("doctor").child("firstName").getValue().toString(),
                                        currentAppointment.child("doctor").child("lastName").getValue().toString(),
                                        currentAppointment.child("doctor").child("email").getValue().toString(),
                                        currentAppointment.child("doctor").child("accountPassword").getValue().toString(),
                                        currentAppointment.child("doctor").child("phoneNumber").getValue().toString(),
                                        appointment.getDoctor().getAddress(),
                                        currentAppointment.child("doctor").child("employeeNumber").getValue().toString(),
                                        appointment.getDoctor().getSpecialties());
                                Appointment app = new Appointment(p, d, currentAppointment.child("status").getValue().toString(),
                                        currentAppointment.child("date").getValue().toString(),
                                        currentAppointment.child("startTime").getValue().toString(),
                                        currentAppointment.child("endTime").getValue().toString());
                                // if the appointments in the database match
                                if (app.equals(appointment)) {
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

                appointment.setStatus(AppointmentStatus.ACCEPTED.getStatusText());
            }

            // rejected
        } else if (view.getId() == R.id.rejectAppointmentButton) {
            // remove from database for doctor
            databaseReference.child("users").child(mAuth.getUid()).
                    child("upcomingAppointments").child(String.valueOf(index))
                    .removeValue();

            // remove from database for patient
            Query emailQuery = databaseReference.child("users").orderByChild("email")
                    .equalTo(appointment.getPatient().getEmail());
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
                                    appointment.getPatient().getAddress(),
                                    currentAppointment.child("patient").child("healthCardNumber").getValue().toString());
                            Doctor d = new Doctor(currentAppointment.child("doctor").child("firstName").getValue().toString(),
                                    currentAppointment.child("doctor").child("lastName").getValue().toString(),
                                    currentAppointment.child("doctor").child("email").getValue().toString(),
                                    currentAppointment.child("doctor").child("accountPassword").getValue().toString(),
                                    currentAppointment.child("doctor").child("phoneNumber").getValue().toString(),
                                    appointment.getDoctor().getAddress(),
                                    currentAppointment.child("doctor").child("employeeNumber").getValue().toString(),
                                    appointment.getDoctor().getSpecialties());
                            Appointment app = new Appointment(p, d, currentAppointment.child("status").getValue().toString(),
                                    currentAppointment.child("date").getValue().toString(),
                                    currentAppointment.child("startTime").getValue().toString(),
                                    currentAppointment.child("endTime").getValue().toString());
                            // if the appointments in the database match
                            if (app.equals(appointment)) {
                                // remove from database
                                databaseReference.child("users").child(ds.getKey()).
                                        child("upcomingAppointments").child(currentAppointment.getKey())
                                        .removeValue();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        Toast.makeText(DoctorAppointmentInfoDisplay_Activity.this, toastMessage,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Moves screen to DoctorAppointmentsActivityNew page
     */
    public void navigateToDoctorInbox(View view) {
        Intent backtoInbox = new Intent(DoctorAppointmentInfoDisplay_Activity.this, DoctorAppointmentsActivityNew.class);
        backtoInbox.putExtra("User", user);
        startActivity(backtoInbox);
    }


    /**
     * Check if an appointment is in the past.
     *
     * @param appointment The appointment to check.
     * @return True if the appointment is in the past, false otherwise.
     */
    private static boolean isPastAppointment(Appointment appointment) {
        try {
            // representation of the appointment date and time
            String dateTimeString = appointment.getDate() + " " + appointment.getStartTime();
            Date appointmentDateTime = timeFormat.parse(dateTimeString);

            // representation of the current date and time
            Calendar currentCalendar = Calendar.getInstance();
            Date currentDate = currentCalendar.getTime();

            return appointmentDateTime.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update the UI to reflect changes.
     */
    private void updateUI() {
        // Update the UI to reflect the changes in the appointment status
        status.setText(appointment.getStatus().toString());

        // Update visibility of buttons based on the status
        findViewById(R.id.acceptAppointmentButton).setVisibility(View.GONE);
        findViewById(R.id.rejectAppointmentButton).setVisibility(View.GONE);
    }
}
