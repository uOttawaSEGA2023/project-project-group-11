package com.example.seg2105project;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegistrationRequestManager {

    // Firebase Real-Time Database for holding database
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    // reference variable to database
    private static DatabaseReference databaseReference = database.getReference();

    // gets all the pending registration requests
    public static ArrayList<User> getPendingList(){
        // list to hold pending users
        ArrayList<User> pendingList = new ArrayList<User>();
        // reference to pending tag of database (containing all pending users)
        DatabaseReference pendingRef = databaseReference.child("pending");
        pendingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // loops through every user in the pending section of the database
                for(DataSnapshot ds : snapshot.getChildren()) {
                    // gets a User object to add to the pending list
                    User user = getUserObject(ds);
                    pendingList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return pendingList;
    }

    public ArrayList<String> getRejectedList(){
        return null;
    }

    private static User getUserObject(DataSnapshot ds){

        // set default user
        User user = new Patient(null, null, null, null,
                null, null, null);

        // create variables to create new user object based on
        // user data
        String firstName = ds.child("firstName")
                .getValue(String.class);
        String lastName = ds.child("lastName")
                .getValue(String.class);
        String email = ds.child("email")
                .getValue(String.class);
        String accountPassword = ds.child("accountPassword")
                .getValue(String.class);
        String phoneNumber = ds.child("phoneNumber")
                .getValue(String.class);

        // receiving for address class
        String postalAddress = ds.child("address")
                .child("postalAddress").getValue(String.class);
        String postalCode = ds.child("address")
                .child("postalCode").getValue(String.class);
        String city = ds.child("address")
                .child("city").getValue(String.class);
        String province = ds.child("address")
                .child("province").getValue(String.class);
        String country = ds.child("address")
                .child("country").getValue(String.class);
        Address address = new Address(postalAddress, postalCode, city, province, country);

        String type = ds.child("type").getValue(String.class);
        // creating new objects based on above data
        switch (type) {
            case "patient":
                String healthCardNumber = ds.child("healthCardNumber")
                        .getValue(String.class);
                user = new Patient(firstName, lastName, email, accountPassword,
                        phoneNumber, address, healthCardNumber);
                break;
            case "doctor":
                String employeeNumber = ds.child("employeeNumber")
                        .getValue(String.class);
                ArrayList<String> specialties = (ArrayList<String>)
                        ds.child("specialties").getValue();
                user = new Doctor(firstName, lastName, email, accountPassword,
                        phoneNumber, address, employeeNumber, specialties);
                break;
            case "admin":
                user = new Admin(firstName, lastName, email,
                        accountPassword, phoneNumber, address);
                break;
        }
        return user;
    }

}
