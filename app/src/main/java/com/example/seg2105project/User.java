package com.example.seg2105project;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class User implements Serializable {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String accountPassword;
    private final String phoneNumber;
    private final Address address;

    /**
     * Represents the user of the application.
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address of the user
     * @param accountPassword the password of the user
     * @param phoneNumber the phone number of the user
     * @param address the postal address of the user, represented as an {@link Address}
     * @see Admin
     * @see Doctor
     * @see Patient
     * @see Address
     */
    public User(String firstName, String lastName, String email, String accountPassword, String phoneNumber, Address address){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.accountPassword=accountPassword;
        this.phoneNumber=phoneNumber;
        this.address=address;
    }

    /**
     * Returns the user's first name.
     * @return the first name of the user
     */
    public String getFirstName(){
        return this.firstName;
    }

    /**
     * Returns the user's last name.
     * @return the last name of the user
     */
    public String getLastName(){
        return this.lastName;
    }

    /**
     * Returns the user's email address.
     * @return the email address of the user
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * Returns the user's account password.
     * @return the account password of the user
     */
    public String getAccountPassword(){
        return this.accountPassword;
    }

    /**
     * Returns the user's phone number.
     * @return the phone number of the user
     */
    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    /**
     * Returns the user's postal address
     * @return the {@link Address} of the user
     */
    public Address getAddress(){
        return this.address;
    }


}
