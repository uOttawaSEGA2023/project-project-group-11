package com.example.seg2105project;

import java.util.ArrayList;

public class Doctor extends User {
    private final String employeeNumber;
    private final ArrayList<String> specialties;

    public Doctor(String firstName, String lastName, String email, String accountPass,
                  String phoneNumber, Address address, String employeeNumber,
                  ArrayList<String> specialties) {
        super(firstName, lastName, email, accountPass, phoneNumber, address);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
    }

    public String getEmployeeNumber() {
        return this.employeeNumber;
    }

    public ArrayList<String> getSpecialties() {
        return this.specialties;
    }
}
