package com.example.seg2105project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {

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

        validateInput(patient);

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

        validateInput(doctor);

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
                                       Address address) throws Exception {
        // create admin object
        User admin = new Admin(firstName, lastName, email, accountPassword, phoneNumber, address);

        validateInput(admin);



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


    // Validates input of user
    private static void validateInput(User user) throws Exception {

        // first name validation
        if (user.getFirstName().length() < 1) {
            throw new Exception("First Name not entered!");
        }

        // last name validation
        if (user.getLastName().length() < 1) {
            throw new Exception("Last Name not entered!");
        }

        // email validation
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher emailMatch = pattern.matcher(user.getEmail());
        if (!emailMatch.matches()) {
            throw new Exception("Email not valid!");
        }

        // password validation
        if (user.getAccountPassword().length() < 5) {
            throw new Exception("Password length must be at least 5!");
        }

        // phone number validation
        if (user.getPhoneNumber().length() < 6) {
            throw new Exception("Phone Number length must be at least 6!");
        }

        // address validation
        if (user.getAddress() != null) {
            // postal code postal code format - A1A 1A1
            String postalForm = "^(?!.*[DFIOQU])[A-VXYa-vxy][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]$";
            Pattern postal = Pattern.compile(postalForm);
            Matcher postalMatch = postal.matcher(user.getAddress().getPostalCode());

            // street address, city, province, country
            if (user.getAddress().getPostalAddress().length() < 1) {
                throw new Exception("Postal Address not entered!");
            }
            if (!postalMatch.matches()) {
                throw new Exception("Postal code format A1A 1A1");
            }
            if (user.getAddress().getCity().length() < 1) {
                throw new Exception("City not entered!");
            }
            if (user.getAddress().getProvince().length() < 1) {
                throw new Exception("Province not entered!");
            }
            if (user.getAddress().getCountry().length() < 1) {
                throw new Exception("Country not entered!");
            }
        }

        // patient health card number validation
        if (user instanceof Patient) {
            if (((Patient) user).getHealthCardNumber().length() < 1) {
                throw new Exception("Health card number not entered!");
            }
        }
        // specialization length validation
        if (user instanceof Doctor) {
            if (((Doctor) user).getSpecialties().size() < 1) {
                throw new Exception("Specialties not entered!");
            }
            if (((Doctor) user).getEmployeeNumber().length() < 1) {
                throw new Exception("Employee number not entered!");
            }
        }
    }
}
