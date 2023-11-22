package com.example.seg2105project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShiftConstructorAndGetterMethods() {
        Shift shift = new Shift("2023-01-01", "09:00 AM", "05:00 PM");

        // Verify the constructor and getter methods
        assertEquals("2023-01-01", shift.getDate());
        assertEquals("09:00 AM", shift.getStartTime());
        assertEquals("05:00 PM", shift.getEndTime());
    }

    @Test
    public void testShiftSetterMethods() {
        Shift shift = new Shift("2023-01-01", "09:00 AM", "05:00 PM");

        shift.setDate("2023-01-02");
        shift.setStartTime("10:00 AM");
        shift.setEndTime("06:00 PM");

        // Verify the setter methods
        assertEquals("2023-01-02", shift.getDate());
        assertEquals("10:00 AM", shift.getStartTime());
        assertEquals("06:00 PM", shift.getEndTime());
    }

    @Test
    public void testShiftEquals() {
        Shift shift1 = new Shift("2023-01-01", "09:00 AM", "05:00 PM");
        Shift shift2 = new Shift("2023-01-01", "09:00 AM", "05:00 PM");

        Shift differentShift = new Shift("2023-01-02", "10:00 AM", "06:00 PM");

        // Verify the equals method
        assertEquals(shift1, shift2);
        assertNotEquals(shift1, differentShift);
    }

}

