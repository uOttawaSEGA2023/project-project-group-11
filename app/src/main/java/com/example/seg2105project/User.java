package com.example.seg2105project;

import java.io.Serializable;

public abstract class User implements Serializable {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String accountPassword;
    private final String phoneNumber;
    private final Address address;

    public User(String firstName, String lastName, String email, String accountPassword, String phoneNumber, Address address){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.accountPassword=accountPassword;
        this.phoneNumber=phoneNumber;
        this.address=address;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getEmail(){
        return this.email;
    }
    public String getAccountPassword(){
        return this.accountPassword;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public Address getAddress(){
        return this.address;
    }


}
