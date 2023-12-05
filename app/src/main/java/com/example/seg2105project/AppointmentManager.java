package com.example.seg2105project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AppointmentManager {

    static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    /**
     * When the Doctor accepts an appointment, changes appointment status for both users.
     *
     */
    public static void updateStatusToAccepted(Appointment appointment) {
        // update for the doctor
        updateStatusForUser(appointment.getDoctor().getEmail(), appointment, true);

        // update for the patient
        updateStatusForUser(appointment.getPatient().getEmail(), appointment, true);

    }

    /**
     * When the Doctor or Patient rejects/cancels an appointment, removes it from both users.
     */
    public static void removeAppointment(Appointment appointment) {
        // update for the doctor
        updateStatusForUser(appointment.getDoctor().getEmail(), appointment, false);

        // update for the patient
        updateStatusForUser(appointment.getPatient().getEmail(), appointment, false);


    }

    /**
     * Update database appointment for a given user.
     * @param user
     * @param appointment
     * @param accept if the update should be a status acceptance or removal of appointment
     */
    private static void updateStatusForUser(String user, Appointment appointment, boolean accept) {
        // update for the user
        Query emailQuery = databaseReference.child("users").orderByChild("email").equalTo(user);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // doctor object corresponding to the email
                for(DataSnapshot ds: snapshot.getChildren()) {
                    // upcoming appointments of the doctor
                    DataSnapshot doctorAppointments = ds.child("upcomingAppointments");
                    // loop through each appointment until the appointment is found
                    for(DataSnapshot currentAppointment : doctorAppointments.getChildren()){
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
                        Appointment app = new Appointment(p,d, currentAppointment.child("status").getValue().toString(),
                                currentAppointment.child("date").getValue().toString(),
                                currentAppointment.child("startTime").getValue().toString(),
                                currentAppointment.child("endTime").getValue().toString());
                        // if the appointments in the database match
                        if(app.equals(appointment)){
                            if(accept) {
                                databaseReference.child("users").child(ds.getKey()).
                                        child("upcomingAppointments").child(currentAppointment.getKey())
                                        .child("status")
                                        .setValue(DoctorAppointmentInfoDisplay_Activity.AppointmentStatus.ACCEPTED.getStatusText());
                                break;
                            } else {
                                databaseReference.child("users").child(ds.getKey()).
                                        child("upcomingAppointments").child(currentAppointment.getKey())
                                        .removeValue();
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
