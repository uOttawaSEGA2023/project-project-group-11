package com.example.seg2105project;

public class Shift {

    private String date;
    private String startTime;
    private String endTime;

    Shift(String date, String startTime, String endTime){
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Gets date of date in a string format
     * @return String
     */
    public String getDate(){
        return this.date;
    }

    /**
     * Gets start time of shift
     * @return String
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Gets end time of shift
     * @return String
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the date associated with the shift.
     * @param date the date of the shift
     */
    public void setDate(String date){ this.date = date; }

    /**
     * Sets the start-time associated with the shift.
     * @param startTime the start-time of the shift
     */
    public void setStartTime(String startTime){ this.startTime = startTime; }

    /**
     * Sets the end-time associated with the shift.
     * @param endTime the end-time of the shift
     */
    public void setEndTime(String endTime){ this.endTime = endTime; }

}
