package com.example.seg2105project;

import java.io.Serializable;

/**
 * This class represents an Appointment to be Booked - separate from Appointment due to
 * the need for a specific specialty when searching.
 */
public class AppointmentAvailView implements Serializable {
    // the name of the doctor corresponding to the appointment
    private Doctor doctor;

    // full name of the doctor, used when displaying the appointment details
    private String doctorName;

    // the specialty for the appointment
    private String specialty;

    // the day of the appointment
    private String date;

    // the time the appointment begins
    private String startTime;

    // the time the appointment ends (30 minutes after start)
    private String endTime;

    // the patient corresponding to the appointment
    private Patient patient;

    // the status of the appointment
    private String status;

    public AppointmentAvailView(Doctor doc, String special, String day, String start, String end){
        doctor = doc;
        doctorName = doc.getFirstName() + " " + doc.getLastName();
        specialty = special;
        date = day;
        startTime = start;
        endTime = end;
    }

    // assigns the patient to the appointment when being booked
    public void setPatient(Patient p){
        patient = p;
        status = "Not Approved Yet";
    }

    // returns the doctor that is directing the appointment
    public Doctor getDoctor(){
        return doctor;
    }

    // returns the patient that booked the appointment
    public Patient getPatient(){
        return patient;
    }

    // returns the status of the appointment, if it has been accepted or rejected by the doctor
    public String getStatus(){
        return status;
    }

    // returns the name of the doctor
    public String getDoctorName(){
        return doctorName;
    }

    // returns the specialty the appointment is booked for, specified by the patient
    public String getSpecialty(){
        return specialty;
    }

    // returns the date for when the appointment is booked for
    public String getDate(){
        return date;
    }

    // returns at what time the appointment will start
    public String getStartTime(){
        return startTime;
    }

    // returns at what time the appointment will end
    public String getEndTime(){
        return endTime;
    }
}
