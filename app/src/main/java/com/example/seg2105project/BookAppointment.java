package com.example.seg2105project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.ListView;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;




public class BookAppointment extends AppCompatActivity {

    ArrayList<AppointmentAvailView> availableAppointments;
    private DatabaseReference databaseReference;
    private void InitializeFirebase(){
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InitializeFirebase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);


        SearchView searchSpecialty = findViewById(R.id.specialtySearch);
        searchSpecialty.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                //method is called when user clicks enter
                //we handle the specialty search
                Toast.makeText(BookAppointment.this, "pulling up list",
                        Toast.LENGTH_SHORT).show();
                handleSearch(query);

                return true;
            }

            public boolean onQueryTextChange(String newText){

                return false;
            }
        });
    }
    private void handleSearch(String newText){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.child("type").equals("doctor")) {
                        DataSnapshot dr = userSnapshot.child("specialties");
                        for (DataSnapshot specialtySnapshot : dr.getChildren()) {
                            String specialityType = specialtySnapshot.getValue(String.class);
                            if (specialityType.equals(newText)) {
                                Doctor doctor = new Doctor(userSnapshot.child("firstName").getValue(String.class),
                                        userSnapshot.child("lastName").getValue(String.class),
                                        userSnapshot.child("email").getValue(String.class),
                                        userSnapshot.child("accountPassword").getValue(String.class),
                                        userSnapshot.child("phoneNumber").getValue(String.class), null,
                                        userSnapshot.child("employeeNumber").getValue(String.class),
                                        null);

                                /**
                                 * Gets all the possible shifts for the doctor
                                 */
                                for (DataSnapshot d : userSnapshot.child("shifts").getChildren()) {
                                    Shift shift = new Shift(d.child("date").getValue(String.class),
                                            d.child("startTime").getValue(String.class),
                                            d.child("endTime").getValue(String.class));
                                    doctor.addShift(shift);
                                }

                                //Checking to see if the appointment is already booked, meaning in upcoming appointments of a doctor
                                ArrayList<Appointment> upcomingApp = new ArrayList<>();
                                for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                                    if (usersSnapshot.child("type").equals("doctor")) {
                                        DataSnapshot dA = userSnapshot.child("specialties");
                                        for (DataSnapshot sSnapshot : dA.getChildren()) {
                                            String sType = specialtySnapshot.getValue(String.class);
                                            if (sType.equals(newText)) {
                                                DataSnapshot appointmentsSnapshot = usersSnapshot.child("upcomingAppointments");
                                                for (DataSnapshot appointmentSnap : appointmentsSnapshot.getChildren()) {
                                                    Appointment bookedAppointment = appointmentSnap.getValue(Appointment.class);
                                                    upcomingApp.add(bookedAppointment);
                                                }
                                            }
                                        }
                                    }
                                }
                                makeAppointments(doctor, upcomingApp, newText);

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

    public void makeAppointments(Doctor doc, ArrayList<Appointment> bookedApps, String specialT) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        ArrayList<Shift> doctorShifts = doc.getShifts();
        for (Shift findTimes : doctorShifts) {
            String startTime = findTimes.getStartTime();
            String endTime = findTimes.getEndTime();

            try {
                Date start = dateFormat.parse(startTime);
                Date end = dateFormat.parse(endTime);

                long interval = 30 * 60 * 1000; // 30 minutes in milliseconds


                long startInterval = start.getTime();


                while (startInterval != end.getTime()) {
                    for (Appointment appB : bookedApps) {
                        if (startInterval == Long.parseLong(appB.getStartTime()) && appB.getDoctor() == doc && appB.getDate().equals(findTimes.getDate())) {
                            startInterval += interval;
                            break;
                        }
                    }
                    Date startAppt = new Date(startInterval);
                    String formattedStart = dateFormat.format(startAppt);
                    Date endAppt = new Date(startInterval + interval);
                    String formattedEnd = dateFormat.format(endAppt);


                    Appointment availableA = new Appointment(null, doc, "available", findTimes.getDate(), formattedStart, formattedEnd);
                    availableAppointments.add(new AppointmentAvailView(availableA.getDoctor().getFullName(), specialT, availableA.getDate(), availableA.getStartTime(), availableA.getEndTime() ));
                    startInterval += interval;
                }
                Toast.makeText(BookAppointment.this, "hello",
                        Toast.LENGTH_SHORT).show();
                initializeListView(availableAppointments);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public void initializeListView(ArrayList<AppointmentAvailView> apps){
        Toast.makeText(BookAppointment.this, "hi",
                Toast.LENGTH_SHORT).show();
        AppointmentAvailViewAdapter appointmentArrayAdapter = new AppointmentAvailViewAdapter(this, apps);

        ListView available = findViewById(R.id.potentialAppointment);
        available.setAdapter(appointmentArrayAdapter);
    }

    public void onClick(View view) {
        // Button to return to welcome page
        if (view.getId() == R.id.button4) {
            Intent back = new Intent(BookAppointment.this, WelcomePageActivity.class);
            startActivity(back);
        }
    }
}
