package com.example.seg2105project;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Shift implements Serializable {

    private String date;
    private String startTime;
    private String endTime;

    public Shift(String date, String startTime, String endTime){
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Shift))
            return false;
        Shift shift = (Shift) obj;
        return (this.date.equals(shift.getDate())) && (this.startTime.equals(shift.getStartTime()) )
                && (this.endTime.equals(shift.getEndTime()));
    }
}
