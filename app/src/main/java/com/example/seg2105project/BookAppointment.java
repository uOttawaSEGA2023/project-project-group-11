package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.AdapterView;
import android.widget.SearchView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * This is an Activity class representing the list of searched appointments
 * corresponding to the specialty provided in the search bar.
 */
public class BookAppointment extends AppCompatActivity {
    public ArrayList<AppointmentAvailView> availableAppointments;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        InitializeFirebase();

        // get UI element for search view
        SearchView searchSpecialty = findViewById(R.id.specialtySearch);
        searchSpecialty.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                //method is called when user clicks enter
                //we handle the specialty search
                handleSearch(query);
                return true;
            }

            public boolean onQueryTextChange(String newText){

                return false;
            }
        });
    }

    /**
     * Handles the back button event to return to the welcome page.
     * @param view the button clicked
     */
    public void onClickBack(View view) {
        // Button to return to welcome page
        if (view.getId() == R.id.button4) {
            Intent back = new Intent(BookAppointment.this, WelcomePageActivity.class);
            back.putExtra("User", (Patient) getIntent().getExtras().getSerializable("User"));
            back.putExtra("Type", "patient");
            startActivity(back);
        }
    }

    /**
     * Handles the search for a specialty.
     * @param newText the search query.
     */
    private void handleSearch(String newText){

        // search through users to find where the specialty exists
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // loop through all users
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // if a user is a doctor
                    if (userSnapshot.child("type").getValue().equals("doctor")) {
                        // get specialties
                        DataSnapshot dr = userSnapshot.child("specialties");
                        // loop through specialties
                        for (DataSnapshot specialtySnapshot : dr.getChildren()) {
                            String specialityType = specialtySnapshot.getValue().toString();
                            // if specialty matches user query
                            if (specialityType.equals(newText)) {
                                // create doctor object
                                Doctor doctor = new Doctor(userSnapshot.child("firstName").getValue(String.class),
                                        userSnapshot.child("lastName").getValue(String.class),
                                        userSnapshot.child("email").getValue(String.class),
                                        userSnapshot.child("accountPassword").getValue(String.class),
                                        userSnapshot.child("phoneNumber").getValue(String.class), null,
                                        userSnapshot.child("employeeNumber").getValue(String.class),
                                        null);


                                // Gets all the possible shifts for the doctor
                                for (DataSnapshot d : userSnapshot.child("shifts").getChildren()) {
                                    Shift shift = new Shift(d.child("date").getValue(String.class),
                                            d.child("startTime").getValue(String.class),
                                            d.child("endTime").getValue(String.class));
                                    doctor.addShift(shift);
                                }

                                /** Checking to see if the appointment is already booked by another patient,
                                 i.e. in upcoming appointments of a doctor
                                 */
                                // list containing booked appointments
                                ArrayList<Appointment> bookedAppointments = new ArrayList<>();

                                // looping through users
                                for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                                    // if a user is a doctor
                                    if (usersSnapshot.child("type").getValue().equals("doctor")) {
                                        // get specialties of the doctor
                                        DataSnapshot dA = userSnapshot.child("specialties");
                                        // loop through specialties
                                        for (DataSnapshot sSnapshot : dA.getChildren()) {
                                            String sType = sSnapshot.getValue(String.class);
                                            // if specialty matches user query
                                            if (sType.equals(newText)) {
                                                // get upcoming appointments of the doctor and add to booked appointment list
                                                DataSnapshot appointmentsSnapshot = usersSnapshot.child("upcomingAppointments");
                                                // loop through upcoming appointments
                                                for (DataSnapshot appointmentSnap : appointmentsSnapshot.getChildren()) {
                                                    String day = appointmentSnap.child("date").getValue(String.class);
                                                    String starting = appointmentSnap.child("startTime").getValue(String.class);
                                                    String ending = appointmentSnap.child("endTime").getValue(String.class);
                                                    String doctorNum = appointmentSnap.child("doctor").child("employeeNumber").getValue(String.class);
                                                    String doctorFN = appointmentSnap.child("doctor").child("firstName").getValue(String.class);
                                                    String doctorLN = appointmentSnap.child("doctor").child("lastName").getValue(String.class);

                                                    // create the doctor object that is corresponding to the appointment
                                                    Doctor doc1 = new Doctor(doctorFN, doctorLN, null, null, null, null, doctorNum, null);

                                                    Appointment bookedAppointment = new Appointment(null, doc1, "booked", day, starting, ending);
                                                    bookedAppointments.add(bookedAppointment);
                                                }
                                            }
                                        }
                                    }
                                }

                                // create the appointment
                                makeAppointments(doctor, bookedAppointments, newText);

                            }
                        }
                    }
                }
            }
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    /**
     * Initializes the Firebase reference
     */
    private void InitializeFirebase(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Creates an appointment between a Doctor and a Patient.
     * @param doc the doctor associated with the appointment
     * @param bookedApps the booked appointments of every doctor associated with the specialty
     * @param specialT the specialty of the appointment
     */
    private void makeAppointments(Doctor doc, ArrayList<Appointment> bookedApps, String specialT) {

        // list of available appointments to display
        availableAppointments = new ArrayList<>();
        // if an appointment is booked
        boolean isBooked;

        // DateFormat representation of a time
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        // shifts of the doctor
        ArrayList<Shift> doctorShifts = doc.getShifts();
        // looping through each shift
        for (Shift findTimes : doctorShifts) {
            // interval of the shift
            String startTime = findTimes.getStartTime();
            String endTime = findTimes.getEndTime();

            try {
                // interval of the shift in Date format
                Date start = dateFormat.parse(startTime);
                Date end = dateFormat.parse(endTime);

                // appointment interval
                long interval = 30 * 60 * 1000; // 30 minutes in milliseconds

                // start of an appointment
                long startInterval = start.getTime();

                // create a new appointment for each 30 minute interval
                while (startInterval < end.getTime()) {
                    //converting the start interval to a date instance
                    Date startAT = new Date(startInterval);

                    isBooked = false;

                    // loops through every booked appointment and checks if any are in the current
                    // slot
                    for (Appointment appB : bookedApps) {

                        // booked appointment start time
                        Date appBStartTime = dateFormat.parse(appB.getStartTime());
                        // if the appointment interval, doctor, and date matches
                        // the booked interval, doctor, and date
                        if (startAT.equals(appBStartTime) && appB.getDoctor().getEmployeeNumber().equals(doc.getEmployeeNumber()) && appB.getDate().equals(findTimes.getDate())) {
                            // add 30 minutes to the start time
                            startInterval += interval;

                            // indicated that the time slot is not available for the appointment
                            isBooked = true;
                            break;
                        }
                    }
                    // goes to next interval if a booked appointment is already occurring
                    if(isBooked){
                        continue;
                    }

                    // if the appointment start time is available,
                    // we create an appointment with the start time and end time
                    Date startAppt = new Date(startInterval);
                    String formattedStart = dateFormat.format(startAppt);
                    Date endAppt = new Date(startInterval + interval);
                    String formattedEnd = dateFormat.format(endAppt);

                    availableAppointments.add(new AppointmentAvailView(doc, specialT, findTimes.getDate(), formattedStart, formattedEnd ));
                    startInterval += interval;

                }

                // displays available appointments
                initializeListView(availableAppointments);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Displays the list of available appointments as a ListView.
     * @param apps
     */
    private void initializeListView(ArrayList<AppointmentAvailView> apps){

        AppointmentAvailViewAdapter appointmentArrayAdapter = new AppointmentAvailViewAdapter(this, apps);

        ListView available = findViewById(R.id.potentialAppointment);
        available.setAdapter(appointmentArrayAdapter);

        // Handling the click events in the ListView
        available.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the appointment that was selected
                AppointmentAvailView selectedAppointment = apps.get(i);

                Patient patientClicking = (Patient) getIntent().getExtras().getSerializable("User");

                Intent selectingAppointment = new Intent(BookAppointment.this, AppointmentBookingActivity.class);

                selectingAppointment.putExtra("Appointment", selectedAppointment);
                selectingAppointment.putExtra("User", patientClicking);

                startActivity(selectingAppointment);

            }
        });
    }
}
