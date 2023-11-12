package com.example.seg2105project;
public class Patient extends User {
    private final String healthCardNumber;

    /**
     * Represents a Patient user in the application. It is a subclass of {@link User}.
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address of the user
     * @param accountPass the password of the user
     * @param phoneNumber the phone number of the user
     * @param address the postal address of the user, represented as an {@link Address}
     * @param healthCardNum the health card number of the user
     * @see User
     */
    public Patient(String firstName, String lastName, String email, String accountPass, String phoneNumber, Address address, String healthCardNum){
        super(firstName, lastName, email, accountPass, phoneNumber, address);
        this.healthCardNumber = healthCardNum;
    }

    /**
     * Returns the patient's health card number.
     * @return the health card number of the patient
     */
    public String getHealthCardNumber(){
        return this.healthCardNumber;
    }

    /**
     * gets the patients full name
     *
     * @return string of the patients full name
     */
    public String getFullName() {
        return super.getFirstName() + " " + super.getLastName();
    }
}
