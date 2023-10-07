package com.example.seg2105project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration {

    private User user;

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public static void createUserPatient(String firstName, String lastName, String email, String accountPassword,
                                         String phoneNumber, Address address, String healthCardNumber) throws Exception {
        // validating that email is not empty
        // NOTE: remove when input validations are implemented
        if(email.equals("")){
            throw new Exception();
        }
        // create patient object
        User patient = new Patient(firstName, lastName, email, accountPassword, phoneNumber, address, healthCardNumber);
        // use authentication ID as unique identifier for database
        // add sign-up information to database
        mAuth.createUserWithEmailAndPassword(email, accountPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                databaseReference.child("users").child(mAuth.getUid()).setValue(patient);
                databaseReference.child("users").child(mAuth.getUid()).child("type").setValue("patient");
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    public static void createUserDoctor(String firstName, String lastName, String email, String accountPassword,
                                        String phoneNumber, String address, String employeeNumber, String[] specialties) {
        //create doctor object
    }

    public static void createUserAdmin(String firstName, String lastName, String email, String accountPassword,
                                       String phoneNumber, Address address) {
        User admin = new Admin(firstName, lastName, email, accountPassword, phoneNumber, address);
        mAuth.createUserWithEmailAndPassword(email, accountPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                databaseReference.child("users").child(mAuth.getUid()).setValue(admin);
                databaseReference.child("users").child(mAuth.getUid()).child("type").setValue("admin");
                FirebaseAuth.getInstance().signOut();
            }
        });
    }
}
