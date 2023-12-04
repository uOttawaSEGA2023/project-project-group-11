package com.example.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activity class to display appointment information for patients.
 */
public class PatientAppointmentInfoDisplay_Activity extends AppCompatActivity {
    TextView doctorName, appointmentDate, appointmentStartTime, appointmentEndTime, doctorSpecialities, appointmentStatus;
    Appointment appointment;

    Patient patient;

    int index; // index of appointment in list

    // Firebase Real-Time Database for holding database
    private static FirebaseDatabase database;
    // reference variable to database
    private static DatabaseReference databaseReference;

    private static FirebaseAuth mAuth;

    // SimpleDateFormat for parsing time
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_info_display);

        patient = (Patient) getIntent().getExtras().getSerializable("User");

        // Retrieve appointment object from intent
        appointment = (Appointment) getIntent().getExtras().getSerializable("Appointment");

        index = getIntent().getExtras().getInt("Index");

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        // Initialize TextViews
        doctorName = findViewById(R.id.doctor);
        doctorSpecialities = findViewById(R.id.specialties);
        appointmentDate = findViewById(R.id.appointmentDate);
        appointmentStartTime = findViewById(R.id.appointmentStartTime);
        appointmentEndTime = findViewById(R.id.appointmentEndTime);
        appointmentStatus = findViewById(R.id.status);

        // Set text for appointment details dynamically
        doctorName.setText("Doctor: " + appointment.getDoctor().getFullName());
        doctorSpecialities.setText("Specialities" + appointment.getDoctor().getSpecialties().toString());
        appointmentDate.setText("Date: " + appointment.getDate());
        appointmentStartTime.setText("Start Time: " + appointment.getStartTime());
        appointmentEndTime.setText("End Time: " + appointment.getEndTime());
        appointmentStatus.setText("Appointment Status: " + appointment.getStatus());

        Button backButton = findViewById(R.id.backButton);
        Button cancelAppointmentButton = findViewById(R.id.cancelAppointmentButton);
        Button rateDoctorButton = findViewById(R.id.rateDoctorButton);

        getAppointmentDetails();

        if(patient.getUpcomingAppointments().size() != 0){
            if (equals(appointment, patient.getUpcomingAppointments().get(index))) {
                rateDoctorButton.setVisibility(View.INVISIBLE);
            }
        }

        if(patient.getPastAppointments().size() != 0) {
            if (equals(appointment, patient.getPastAppointments().get(index))) {
                cancelAppointmentButton.setVisibility(View.INVISIBLE);
            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancelAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.cancelAppointmentButton) {
                    onCancelButtonClick(v);
                    cancelAppointmentButton.setVisibility(v.INVISIBLE);
                }

                onCancelButtonClick(v);

            }
        });

        rateDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.rateDoctorButton) {
                    Intent rateDoctorPage = new Intent(PatientAppointmentInfoDisplay_Activity.this, RateDoctorActivity.class);
                    rateDoctorPage.putExtra("Appointment", appointment);
                    startActivity(rateDoctorPage);
                }
            }
        });
    }

    /**
     * checks if the appointments are equal
     *
     * @param appoint1 first appointment
     * @param appoint2 second appointment
     * @return if appointments are equal
     */
    private boolean equals(Appointment appoint1, Appointment appoint2) {
        if (!appoint1.getDate().equals(appoint2.getDate())) {
            return false;
        }

        if (!appoint1.getStartTime().equals(appoint2.getStartTime())) {
            return false;
        }

        if (!appoint1.getEndTime().equals(appoint2.getEndTime())) {
            return false;
        }

        return true;
    }

    /**
     * gets the details of the appointment, such as the doctor doing the appointment, patients addres
     * and so on
     */
    private void getAppointmentDetails() {
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
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void onCancelButtonClick(View view) {
        if (isAppointmentWithin60Minutes(appointment)) {

            patient.deleteUpcomingAppointment(index);

            databaseReference.child("users").child(mAuth.getUid()).child("upcomingAppointments")
                    .setValue(patient.getUpcomingAppointments());

            Intent welcomePage = new Intent(PatientAppointmentInfoDisplay_Activity.this, WelcomePageActivity.class);
            startActivity(welcomePage);

            // Display a toast message
            Toast.makeText(this, "Appointment canceled!", Toast.LENGTH_SHORT).show();
        } else {
            // Display a toast message indicating that cancellation is not allowed
            Toast.makeText(this, "Cannot cancel appointment within 60 minutes!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * logic to determine if the appointment should be allowed to be cancelled or not
     *
     * @param appointment to be determined whether or not it can be cancelled based on its start time
     * @return true if you can remove the appointment, false otherwise
     */
    private boolean isAppointmentWithin60Minutes(Appointment appointment) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        Date currentDate = new Date();
        String date1 = dateFormat.format(currentDate);

        int result = date1.compareTo(appointment.getDate());

        if (result < 0) {
            return false;
        } else if (result > 0) {
            return true;
        }

        String timeOfAppointment = removeColon(appointment.getStartTime());
        String currentTime = removeColon(getCurrentTimeAsString());

        int time1 = Integer.parseInt(timeOfAppointment);
        int time2 = Integer.parseInt(currentTime);

        return Math.abs(time1 - time2) <= 60;
    }

    /**
     * removes colons from strings representing time
     *
     * @param inputString string with colon to be removed
     * @return string of numbers
     */
    private String removeColon(String inputString) {
        return inputString.replace(":", "");
    }

    /**
     * gets the current time as a string to be compared with the appointment start time
     *
     * @return string representation of the current time
     */
    private String getCurrentTimeAsString() {
        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // Get the current date and time
        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();

        // Format the current time as a string
        String formattedCurrentTime = timeFormat.format(currentDate);

        return formattedCurrentTime;

    }
}
