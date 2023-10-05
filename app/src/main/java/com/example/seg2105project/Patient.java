package com.example.seg2105project;
public class Patient extends User {
    private String healthCardNumber;

    public Patient(String firstName, String lastName, String email, String accountPass, String phoneNumber, String address, String healthCardNum){
        super(firstName, lastName, email, accountPass, phoneNumber, address);
        this.healthCardNumber = healthCardNum;
    }

    public String getHealthCardNumber(){
        return this.healthCardNumber;
    }
}
