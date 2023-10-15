package com.example.seg2105project;

public class Admin extends User {

    /**
     * Represents an Admin user in the application. It is a subclass of {@link User}.
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address of the user
     * @param accountPass the password of the user
     * @param phoneNumber the phone number of the user
     * @param address the postal address of the user, represented as an {@link Address}
     * @see User
     */
    public Admin(String firstName, String lastName, String email, String accountPass, String phoneNumber, Address address) {
        super(firstName, lastName, email, accountPass, phoneNumber, address);
    }
}
