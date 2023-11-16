package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
 * Activity class to display appointment information.
 */
public class DoctorAppointmentInfoDisplay_Activity extends AppCompatActivity {
    TextView patientName, address, healthCardNumber, appointmentDate, appointmentStartTime, appointmentEndTime, status;
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

        user = (User) getIntent().getExtras().getSerializable("User");

        // Retrieve appointment object from intent
        appointment = (Appointment) getIntent().getExtras().getSerializable("Appointment");

        index = getIntent().getExtras().getInt("Index");

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
        patientName.setText(appointment.getPatient().getFullName());
        address.setText(appointment.getPatient().getAddress().toString());
        healthCardNumber.setText(appointment.getPatient().getHealthCardNumber());
        appointmentDate.setText(appointment.getDate());
        appointmentStartTime.setText(appointment.getStartTime());
        appointmentEndTime.setText(appointment.getEndTime());
        status.setText(appointment.getStatus());

        // Check appointment status for button visibility
        if (AppointmentStatus.NOT_APPROVED_YET.getStatusText().equals(appointment.getStatus()) &&
                !isPastAppointment(appointment)) {
            // Display accept and reject buttons
            findViewById(R.id.acceptAppointmentButton).setVisibility(View.VISIBLE);
            findViewById(R.id.rejectAppointmentButton).setVisibility(View.VISIBLE);
        }else {
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
            handleAppointmentAction("User Accepted!", view);
        } else if (view.getId() == R.id.rejectAppointmentButton) {
            handleAppointmentAction("User Rejected!", view);
        }

        // Update the UI after handling the click event
        updateUI();
        navigateToDoctorInbox();
    }

    /**
     * Changes the status of an appointment based on the button clicked
     * @param toastMessage the message to be sent on the toast
     * @param view the button clicked
     */
    private void handleAppointmentAction(String toastMessage, View view) {

        // accepted
        if (view.getId() == R.id.acceptAppointmentButton) {

            // adding to respective databases based on appointment list (upcoming/past)
            if(!isPastAppointment(appointment)){
                databaseReference.child("users").child(mAuth.getUid()).
                        child("upcomingAppointments").child(String.valueOf(index))
                        .child("status")
                        .setValue(AppointmentStatus.ACCEPTED.getStatusText());
            } else {
                databaseReference.child("users").child(mAuth.getUid()).
                        child("pastAppointments").child(String.valueOf(index))
                        .child("status")
                        .setValue(AppointmentStatus.ACCEPTED.getStatusText());
            }
            appointment.setStatus(AppointmentStatus.ACCEPTED.getStatusText());

        // rejected
        } else if (view.getId() == R.id.rejectAppointmentButton) {
            databaseReference.child("users").child(mAuth.getUid()).
                    child("upcomingAppointments").child(String.valueOf(index))
                    .removeValue();
        }
        Toast.makeText(DoctorAppointmentInfoDisplay_Activity.this, toastMessage,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Moves screen to DoctorAppointmentsActivityNew
     */
    private void navigateToDoctorInbox() {
        Intent backtoInbox = new Intent(DoctorAppointmentInfoDisplay_Activity.this, DoctorAppointmentsActivityNew.class);
        backtoInbox.putExtra("User", user);
        startActivity(backtoInbox);
    }


    /**
     * Check if an appointment is in the past.
     * @param appointment The appointment to check.
     * @return True if the appointment is in the past, false otherwise.
     */
    public static boolean isPastAppointment (Appointment appointment){
        try {
            String dateTimeString = appointment.getDate() + " " + appointment.getStartTime();
            Date appointmentDateTime = timeFormat.parse(dateTimeString);

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
    private void updateUI () {
        // Update the UI to reflect the changes in the appointment status
        status.setText(appointment.getStatus().toString());

        // Update visibility of buttons based on the status
        findViewById(R.id.acceptAppointmentButton).setVisibility(View.GONE);
        findViewById(R.id.rejectAppointmentButton).setVisibility(View.GONE);
    }
}
