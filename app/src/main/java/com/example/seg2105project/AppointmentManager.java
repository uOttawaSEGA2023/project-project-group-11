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
        Query emailQueryDoctor = databaseReference.child("users").orderByChild("email").equalTo(appointment.getDoctor().getEmail());
        emailQueryDoctor.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            databaseReference.child("users").child(ds.getKey()).
                                    child("upcomingAppointments").child(currentAppointment.getKey())
                                    .child("status")
                                    .setValue(DoctorAppointmentInfoDisplay_Activity.AppointmentStatus.ACCEPTED.getStatusText());
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // update for the patient
        Query emailQueryPatient = databaseReference.child("users").orderByChild("email").equalTo(appointment.getPatient().getEmail());
        emailQueryPatient.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // patient object corresponding to the email
                for(DataSnapshot ds: snapshot.getChildren()) {
                    // upcoming appointments of the patient
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
                            databaseReference.child("users").child(ds.getKey()).
                                    child("upcomingAppointments").child(currentAppointment.getKey())
                                    .child("status")
                                    .setValue(DoctorAppointmentInfoDisplay_Activity.AppointmentStatus.ACCEPTED.getStatusText());
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * When the Doctor or Patient rejects/cancels an appointment, removes it from both users.
     */
    public static void removeAppointment(Appointment appointment) {
        // update for the doctor
        Query emailQueryDoctor = databaseReference.child("users").orderByChild("email").equalTo(appointment.getDoctor().getEmail());
        emailQueryDoctor.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            databaseReference.child("users").child(ds.getKey()).
                                    child("upcomingAppointments").child(currentAppointment.getKey())
                                    .removeValue();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // update for the patient
        Query emailQueryPatient = databaseReference.child("users").orderByChild("email").equalTo(appointment.getPatient().getEmail());
        emailQueryPatient.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // patient object corresponding to the email
                for(DataSnapshot ds: snapshot.getChildren()) {
                    // upcoming appointments of the patient
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
                            databaseReference.child("users").child(ds.getKey()).
                                    child("upcomingAppointments").child(currentAppointment.getKey())
                                    .removeValue();
                            return;
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
