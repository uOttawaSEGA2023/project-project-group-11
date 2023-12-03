package com.example.seg2105project;

public class AppointmentAvailView {
    // the name of the doctor corresponding to the appointment
    private String doctorName;

    // the specialty for the appointment
    private String specialty;

    // the day of the appointment
    private String date;

    // the time the appointment begins
    private String startTime;

    // the time the appointment ends (30 minutes after start)
    private String endTime;

    public AppointmentAvailView(String doc, String special, String day, String start, String end){
        doctorName = doc;
        specialty = special;
        date = day;
        startTime = start;
        endTime = end;
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
