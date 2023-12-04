package com.example.seg2105project;

import java.io.Serializable;

public class AppointmentAvailView implements Serializable {
    // the name of the doctor corresponding to the appointment
    private Doctor doctor;
    private String doctorName;

    // the specialty for the appointment
    private String specialty;

    // the day of the appointment
    private String date;

    // the time the appointment begins
    private String startTime;

    // the time the appointment ends (30 minutes after start)
    private String endTime;

    private Patient patient;

    private String status;

    public AppointmentAvailView(Doctor doc, String special, String day, String start, String end){
        doctor = doc;
        doctorName = doc.getFirstName() + " " + doc.getLastName();
        specialty = special;
        date = day;
        startTime = start;
        endTime = end;
    }

    public void setPatient(Patient p){
        patient = p;
        status = "Not Approved Yet";
    }
    public Doctor getDoctor(){
        return doctor;
    }

    public Patient getPatient(){
        return patient;
    }

    public String getStatus(){
        return status;
    }

    public String getDoctorName(){
        return doctorName;
    }
    public String getSpecialty(){
        return specialty;
    }

    public String getDate(){
        return date;
    }

    public String getStartTime(){
        return startTime;
    }

    public String getEndTime(){
        return endTime;
    }
}
