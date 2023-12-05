package com.example.seg2105project;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a Doctor user.
 */
public class Doctor extends User {
    private final String employeeNumber;
    private final ArrayList<String> specialties;

    private ArrayList<Appointment> upcomingAppointments = new ArrayList<>();
    private ArrayList<Appointment> pastAppointments = new ArrayList<>();

    private ArrayList<Shift> shifts = new ArrayList<>();


    /**
     * Represents a Doctor user in the application. It is a subclass of {@link User}.
     *
     * @param firstName      the first name of the user
     * @param lastName       the last name of the user
     * @param email          the email address of the user
     * @param accountPass    the password of the user
     * @param phoneNumber    the phone number of the user
     * @param address        the postal address of the user, represented as an {@link Address}
     * @param employeeNumber the employee number of the user
     * @param specialties    the specialties of the user
     * @see User
     */
    public Doctor(String firstName, String lastName, String email, String accountPass,
                  String phoneNumber, Address address, String employeeNumber,
                  ArrayList<String> specialties) {
        super(firstName, lastName, email, accountPass, phoneNumber, address);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;

    }
    /**
     * Returns the doctor's employee number.
     *
     * @return the employee number of the doctor
     */
    public String getEmployeeNumber() {
        return this.employeeNumber;
    }

    /**
     * Returns the doctor's specialties.
     *
     * @return the specialties of the doctor
     */
    public ArrayList<String> getSpecialties() {
        return this.specialties;
    }

    /**
     * Gets the list of all upcoming appointments this doctor has.
     *
     * @return the upcoming appointments
     */
    public ArrayList<Appointment> getUpcomingAppointments() {
        return upcomingAppointments;
    }

    /**
     * Gets the list of all past appointments this doctor has.
     * @return the past appointments
     */
    public ArrayList<Appointment> getPastAppointments() {
        return pastAppointments;
    }

    /**
     * Adds an appointment to the list of upcoming appointments.
     * @param appointment the upcoming appointment to be added
     */
    public void addUpcomingAppointment(Appointment appointment) {
        upcomingAppointments.add(appointment);
    }

    /**
     * Deletes an appointment from the list of upcoming appointments.
     * @param appointment the upcoming appointment to be removed
     */
    public void deleteUpcomingAppointment(Appointment appointment) {
        upcomingAppointments.remove(appointment);
    }

    /**
     * Adds an appointment to the list of past appointments.
     * @param appointment the past appointment to be added
     */
    public void addPastAppointment(Appointment appointment) {
        pastAppointments.add(appointment);
    }

    /**
     * Gets the list of shifts associated with the doctor.
     * @return the list of shifts
     */
    public ArrayList<Shift> getShifts() {
        return shifts;
    }

    /**
     * Adds a shift to the list of shifts.
     * @param shift the shift to be added
     */
    public void addShift(Shift shift) {
        shifts.add(shift);
    }

    /**
     * Deletes a shift from the list of shifts.
     * @param shift the shift to be deleted
     */
    public void deleteShift(Shift shift) {
        shifts.remove(shift);
    }

    /**
     * Gets the Doctor's full name.
     * @return the full name of the Doctor
     */
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Doctor))
            return false;
        Doctor doc = (Doctor) obj;
        return (this.getFirstName().equals(doc.getFirstName())) && (this.getLastName().equals(doc.getLastName()) )
                && (this.getEmail().equals(doc.getEmail())) && (this.getAccountPassword().equals(doc.getAccountPassword()))
                && (this.getPhoneNumber().equals(doc.getPhoneNumber()))
                && (this.getEmployeeNumber().equals(doc.getEmployeeNumber()))
                && (this.getSpecialties().equals(doc.getSpecialties()));
    }
  
}
