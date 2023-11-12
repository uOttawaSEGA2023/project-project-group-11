package com.example.seg2105project;


public class Appointment {

    private Patient patient;
    private Doctor doctor;
    private String status;
    private String date;
    private String startTime;
    private String endTime;

    public Appointment(Patient patient, Doctor doctor, String status,String date, String startTime, String endTime){
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
     * Returns the status associated with the appointment
     * @return String
     */
    public String getStatus() { return status; }

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
    public String getStartTime() {return startTime;}

    /**
     * Gets end time of appointment
     * @return String
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the status associated with the appointment.
     * @param status the status of the appointment
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Sets the date associated with the appointment.
     * @param date the date of the appointment
     */
    public void setDate(String date) { this.date = date; }

    /**
     * Sets the start-time associated with the appointment.
     * @param startTime the start-time of the appointment
     */
    public void setStartTime(String startTime) { this.startTime = startTime; }

    /**
     * Sets the end-time associated with the appointment.
     * @param endTime the end-time of the appointment
     */
    public void setEndTime(String endTime) { this.endTime = endTime; };

}
