package com.example.seg2105project;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * The RegistrationRequestManager class provides methods for managing
 * registration requests in the Firebase Real-Time Database.
 */
public class RegistrationRequestManager {

    // Firebase Real-Time Database for holding database
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    // reference variable to database
    private static DatabaseReference databaseReference = database.getReference();
    // reference variable to Firebase Authentication
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * Retrieves all registration requests for a given type of category.
     * For example, the type can be "pending," "rejected," or "users."
     *
     * @param type             The type of registration requests to retrieve.
     * @param finishedCallback Callback to receive the list of users.
     * @return An ArrayList of User objects matching the given type.
     */
    public static ArrayList<User> getList(String type, @NonNull SimpleCallback<ArrayList<User>> finishedCallback){
        // list to hold users matching given type
        ArrayList<User> list = new ArrayList<User>();
        // reference to given tag of database
        DatabaseReference listRef = databaseReference.child(type);
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // loops through every user in the pending section of the database
                for(DataSnapshot ds : snapshot.getChildren()) {
                    // gets a User object to add to the pending list
                    User user = getUserObject(ds);
                    list.add(user);
                }
                finishedCallback.callback(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return list;
    }

    /**
     * Transfers data of a user from a current category to a new category.
     * For example, from "pending" to "accepted/rejected."
     *
     * @param user        The User object to transfer.
     * @param oldCategory The old category to transfer from.
     * @param newCategory The new category to transfer to.
     */
    public static void transferData(User user, String oldCategory, String newCategory){
        // reference to old category
        DatabaseReference oldCategoryRef = databaseReference.child(oldCategory);
        // getting reference to ID of user
        DatabaseReference userID = oldCategoryRef.child(mAuth.getUid());
        userID.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // remove user object from current category
                snapshot.getRef().removeValue();

                // add data to new category
                DatabaseReference newCategoryRef = databaseReference.child(newCategory);
                newCategoryRef.child(mAuth.getUid()).setValue(user);
                newCategoryRef.child(mAuth.getUid()).child("type")
                        .setValue(snapshot.child("type").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Gets a User object from a DataSnapshot.
     *
     * @param ds The DataSnapshot containing user data.
     * @return A User object created from the DataSnapshot.
     */
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
