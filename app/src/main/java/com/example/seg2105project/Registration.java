package com.example.seg2105project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration {

    private User user;

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference();

    public static void createUserPatient(String firstName, String lastName, String email, String accountPassword,
                                         String phoneNumber, String address, String healthCardNumber) {
        // create patient object
        User patient = new Patient(firstName, lastName, email, accountPassword, phoneNumber, address, healthCardNumber);
        // use email as unique identifier for database
        String[] username = email.split("@");
        // add sign-up information to database
        databaseReference.child("users").child("patients").child(username[0]).setValue(patient);
    }

    public static void createUserDoctor(String firstName, String lastName, String email, String accountPassword,
                                        String phoneNumber, String address, String employeeNumber, String specialties) {

    }

    public static void createUserAdmin(String firstName, String lastName, String email, String accountPassword,
                                       String phoneNumber, String address) {
        User admin = new Admin(firstName, lastName, email, accountPassword, phoneNumber, address);
        String[] username = email.split("@");
        databaseReference.child("users").child("administrators").child(username[0]).setValue(admin);
    }
}
