package com.example.seg2105project;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Patient extends User {
    private final String healthCardNumber;

    private ArrayList<Appointment> upcomingAppointments = new ArrayList<>();
    private ArrayList<Appointment> pastAppointments = new ArrayList<>();

    private ArrayList<Rating> allRatings = new ArrayList<>();

    /**
     * Represents a Patient user in the application. It is a subclass of {@link User}.
     *
     * @param firstName     the first name of the user
     * @param lastName      the last name of the user
     * @param email         the email address of the user
     * @param accountPass   the password of the user
     * @param phoneNumber   the phone number of the user
     * @param address       the postal address of the user, represented as an {@link Address}
     * @param healthCardNum the health card number of the user
     * @see User
     */
    public Patient(String firstName, String lastName, String email, String accountPass, String phoneNumber, Address address, String healthCardNum) {
        super(firstName, lastName, email, accountPass, phoneNumber, address);
        this.healthCardNumber = healthCardNum;
    }

    /**
     * Returns the patient's health card number.
     *
     * @return the health card number of the patient
     */
    public String getHealthCardNumber() {
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

    /**
     * @return list of the upcoming appointments
     */
    public ArrayList<Appointment> getUpcomingAppointments() {
        return upcomingAppointments;
    }

    /**
     * @return list of past appointments
     */
    public ArrayList<Appointment> getPastAppointments() {
        return pastAppointments;
    }

    /**
     * @return list of ratings the patient has given
     */
    public ArrayList<Rating> getRatings() {
        return allRatings;
    }

    /**
     * adds an appointment to the list of upcoming appointments
     *
     * @param appointment to be added
     */
    public void addUpcomingAppointment(Appointment appointment) {
        upcomingAppointments.add(appointment);
    }

    /**
     * removes an appointment from the list of upcoming appoints
     *
     * @param index of the appointment to be removed
     */
    public void deleteUpcomingAppointment(int index) {
        upcomingAppointments.remove(index);
    }

    /**
     * adds an appointment to the list of past appointment
     *
     * @param appointment appointment to be added
     */
    public void addPastAppointment(Appointment appointment) {
        pastAppointments.add(appointment);
    }

    /**
     * adds rating to the list of patients ratings
     *
     * @param rating to be added
     */
    public void addRating(Rating rating) {
        allRatings.add(rating);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Patient))
            return false;
        Patient patient = (Patient) obj;
        return (this.getFirstName().equals(patient.getFirstName()))
                && (this.getLastName().equals(patient.getLastName()) )
                && (this.getEmail().equals(patient.getEmail()))
                && (this.getAccountPassword().equals(patient.getAccountPassword()))
                && (this.getPhoneNumber().equals(patient.getPhoneNumber()))
                && (this.getHealthCardNumber().equals(patient.getHealthCardNumber()));
    }
}
