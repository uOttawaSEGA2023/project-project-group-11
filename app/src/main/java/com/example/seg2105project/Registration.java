package com.example.seg2105project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {

    private User user;

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public static void createUserPatient(String firstName, String lastName, String email,
                                         String accountPassword, String phoneNumber,
                                         Address address,
                                         String healthCardNumber) throws Exception {
        // create patient object
        User patient = new Patient(firstName, lastName, email, accountPassword, phoneNumber,
                address, healthCardNumber);

        if (!validateInput(patient)) {
            throw new Exception();
        }

        // use authentication ID as unique identifier for database
        // add sign-up information to database
        mAuth.createUserWithEmailAndPassword(email, accountPassword).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // when the user is created, add patient object to database
                        databaseReference.child("users").child(mAuth.getUid()).setValue(patient);
                        // add new "type" key to represent the type of User in the database
                        databaseReference.child("users").child(mAuth.getUid()).child(
                                "type").setValue("patient");
                        // sign user out so that they can re-login on login page
                        FirebaseAuth.getInstance().signOut();
                    }
                });
    }

    public static void createUserDoctor(String firstName, String lastName, String email,
                                        String accountPassword, String phoneNumber,
                                        Address address, String employeeNumber,
                                        ArrayList<String> specialties) throws Exception {
        User doctor = new Doctor(firstName, lastName, email, accountPassword, phoneNumber, address,
                employeeNumber, specialties);

        if (!validateInput(doctor)) {
            throw new Exception();
        }

        mAuth.createUserWithEmailAndPassword(email, accountPassword).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // when the user is created, add doctor object to database
                        databaseReference.child("users").child(mAuth.getUid()).setValue(doctor);
                        // add new "type" key to represent the type of User in the database
                        databaseReference.child("users").child(mAuth.getUid()).child(
                                "type").setValue("doctor");
                        // sign user out so that they can re-login on login page
                        FirebaseAuth.getInstance().signOut();
                    }
                });
    }

    public static void createUserAdmin(String firstName, String lastName, String email,
                                       String accountPassword, String phoneNumber,
                                       Address address) throws Exception{
        // create admin object
        User admin = new Admin(firstName, lastName, email, accountPassword, phoneNumber, address);

        if (!validateInput(admin)) {
            throw new Exception();
        }


        // use authentication ID as unique identifier for database
        // add sign-up information to database
        mAuth.createUserWithEmailAndPassword(email, accountPassword).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // when the user is created, add admin object to database
                        databaseReference.child("users").child(mAuth.getUid()).setValue(admin);
                        // add new "type" key to represent the type of User in the database
                        databaseReference.child("users").child(mAuth.getUid()).child(
                                "type").setValue("admin");
                        // sign user out so that they can re-login on login page
                        FirebaseAuth.getInstance().signOut();
                    }
                });
    }


    // TO BE IMPLEMENTED
    public static boolean validateInput(User user) {

        // first name validation
        if(user.getFirstName().length() < 1) {
            return false;
        }

        // last name validation
        if(user.getLastName().length() < 1) {
            return false;
        }

        // password validation
        if (user.getAccountPassword().length() < 5) {
            return false;
        }

        // phone number validation
        if (user.getPhoneNumber().length() < 6) {
            return false;
        }

        // address validation
        if (user.getAddress() != null) {

            //postal code format - A1B 2C3
//            // zip code
            String regex = "^[A-Za-z]\\d[A-Za-z] \\d[A-Za-z]\\d$";
            Pattern pattern = Pattern.compile(regex);
            Matcher zipMatch = pattern.matcher(user.getAddress().getPostalCode());

            // postal code
            String regex2 = "^[A-Za-z]\\d[A-Za-z] \\d[A-Za-z]\\d$";
            Pattern pattern2 = Pattern.compile(regex2);
            Matcher postalMatch = pattern2.matcher(user.getAddress().getPostalCode());

            // street address, city, province, country
            if (user.getAddress().getPostalAddress().length() < 1
                    ||!zipMatch.matches() || !postalMatch.matches()
                    || user.getAddress().getCity().length() < 1
                    || user.getAddress().getProvince().length() < 1
                    || user.getAddress().getCountry().length() < 1) {
                return false;
            }
        } else {
            return false;
        }

        // patient health card number validation
        if (user instanceof Patient) {
            if(((Patient) user).getHealthCardNumber().length() < 1) {
                return false;
            }
        }
        // specialization length validation
        if (user instanceof Doctor) {
            if (((Doctor) user).getSpecialties().size() < 1
            || ((Doctor) user).getEmployeeNumber().length() < 1) {
                return false;
            }
        }

        // email validation
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher emailMatch = pattern.matcher(user.getEmail());
        if (!emailMatch.matches()) {
            System.out.println("THIS FAILED");
            return false;
        }
        return true;
    }
}
