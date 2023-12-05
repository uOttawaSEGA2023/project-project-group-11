package com.example.seg2105project;

/**
 * This class represents a rating that a Patient gives a Doctor.
 */
public class Rating {
    private Doctor doctor;

    private float rating;

    public Rating(Doctor doctor, float rating) {
        this.doctor = doctor;
        this.rating = rating;
    }


}
