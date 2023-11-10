package com.example.seg2105project;

import android.icu.util.GregorianCalendar;


public class Appointment {
    private Patient patient;
    private Doctor doctor;
    private String status;
    private String date;

    private String startTime;
    private String endTime;
    Appointment(Patient patient, Doctor doctor, String status,String date, String startTime, String endTime){
        this.patient = patient;
        this.doctor=doctor;
        this.status=status;
        this.date=date;
        this.startTime=startTime;
        this.endTime=endTime;
    }

    /**
     * Returns the patient who has the appointment
     * @return Patient Doctor
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the doctor associated with the appointment
     * @return Doctor
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Gets date of appointment in a string format
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets start time of appointment
     * @return String
     */
    public String getStartTime() {
        return startTime;

    }

    /**
     * Gets end time of appointment
     * @return String
     */

    public String getEndTime() {
        return endTime;
    }
}
