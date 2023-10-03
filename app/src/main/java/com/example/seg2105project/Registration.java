package com.example.seg2105project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration {

    private User user;

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference();

    public static void createUserPatient(String firstName, String lastName, String email, String accountPassword,
                                         int phoneNumber, String address, String healthCardNumber) {
        User patient = new Patient(firstName, lastName, email, accountPassword, phoneNumber, address, healthCardNumber);
        databaseReference.child("users").child("patients").setValue(patient);
    }

    public static void createUserDoctor(String firstName, String lastName, String email, String accountPassword,
                                        int phoneNumber, String address, int employeeNumber, String specialties) {

    }

    public static void createUserAdmin(String firstName, String lastName, String email, String accountPassword,
                                       int phoneNumber, String address) {

    }
}
