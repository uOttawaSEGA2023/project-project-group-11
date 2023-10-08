package com.example.seg2105project;

public class Doctor extends User {
    private String employeeNumber;
    private String[] specialities;

    public Doctor(String firstName, String lastName, String email, String accountPass,
                  String phoneNumber, Address address, String employeeNumber,
                  String... specialities) {
        super(firstName, lastName, email, accountPass, phoneNumber, address);
        this.employeeNumber = employeeNumber;
        this.specialities = specialities;
    }

    public String getHealthCardNumber() {
        return this.employeeNumber;
    }

    public String[] getSpecialities() {
        return this.specialities;
    }
}
