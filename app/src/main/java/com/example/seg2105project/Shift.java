package com.example.seg2105project;

public class Shift {

    private Doctor doctor;
    private String date;
    private String startTime;
    private String endTime;

    Shift(Doctor doctor, String date, String startTime, String endTime){
        this.doctor = doctor;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Doctor getDoctor(){
        return this.doctor;
    }
    public String getDate(){
        return this.date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

}
