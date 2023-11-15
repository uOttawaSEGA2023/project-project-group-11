package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity class to display appointment information.
 */
public class AppointmentInfoDisplay_Activity extends AppCompatActivity {
    TextView patientName, address, healthCardNumber, appointmentDate, appointmentStartTime, appointmentEndTime, status;
    Appointment appointment;

    User user;

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
        setContentView(R.layout.activity_appointment_info_display);

        user = (User) getIntent().getExtras().getSerializable("User");

        // Retrieve appointment object from intent
        appointment = (Appointment) getIntent().getExtras().getSerializable("Appointment");

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
        }
    }

    /**
     * Handle the click event for accept/reject buttons.
     *
     * @param view The clicked view.
     */
    public void onAcceptRejectClick(View view) {
        if (view.getId() == R.id.acceptAppointmentButton) {
            handleAppointmentAction("accepted", "User Accepted!", view);
        } else if (view.getId() == R.id.rejectAppointmentButton) {
            handleAppointmentAction("rejected", "User Rejected!", view);
        }

        // Update the UI after handling the click event
        updateUI();
    }

    private void handleAppointmentAction(String action, String toastMessage, View view) {
        RegistrationRequestManager.transferData(user, action, "users", new SimpleCallback<String>() {
            @Override
            public void callback(String data) {
                Toast.makeText(AppointmentInfoDisplay_Activity.this, toastMessage, Toast.LENGTH_SHORT).show();
                navigateToAdminInbox();
            }
        });

        if (view.getId() == R.id.acceptAppointmentButton) {
            appointment.setStatus(AppointmentStatus.ACCEPTED.getStatusText());
        } else if (view.getId() == R.id.rejectAppointmentButton) {
            appointment.setStatus(AppointmentStatus.REJECTED.getStatusText());
        }
    }

    private void navigateToAdminInbox() {
        Intent backtoInbox = new Intent(AppointmentInfoDisplay_Activity.this, AdminInboxActivity.class);
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
