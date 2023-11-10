package com.example.seg2105project;

import java.util.ArrayList;

public class Doctor extends User {
    private final String employeeNumber;
    private final ArrayList<String> specialties;

    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    private ArrayList<Shift> shifts = new ArrayList<Shift>();



    /**
     * Represents a Doctor user in the application. It is a subclass of {@link User}.
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address of the user
     * @param accountPass the password of the user
     * @param phoneNumber the phone number of the user
     * @param address the postal address of the user, represented as an {@link Address}
     * @param employeeNumber the employee number of the user
     * @param specialties the specialties of the user
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
     * @return the employee number of the doctor
     */
    public String getEmployeeNumber() {
        return this.employeeNumber;
    }

    /**
     * Returns the doctor's specialties.
     * @return the specialties of the doctor.
     */
    public ArrayList<String> getSpecialties() {
        return this.specialties;
    }

    /**
     * Adds a new appointment to the list of appointments
     * @param newAppointment
     */
    public void setAppointments(Appointment newAppointment) {
        this.appointments.add(newAppointment);
    }

    /**
     * gets the list of all appointments this doctor have
     * @return ArrayList</Appointment>
     */
    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void deleteAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    public ArrayList<Shift> getShifts(){
        return shifts;
    }

    public void addShift(Shift shift) {
        shifts.add(shift);
    }

    public void deleteShift(Shift shift) {
        shifts.remove(shift);
    }
}
