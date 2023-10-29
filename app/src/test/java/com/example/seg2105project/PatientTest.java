package com.example.seg2105project;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PatientTest {
    private final Patient patient = new Patient("Test", "Patient",
            "testpatient@test.com", "password123", "1234567890",
            new Address("123 Ottawa St", "K1N N1K",
                    "Toronto", "Ontario", "Canada"), "512");
    @Test
    public void testGetHealthCardNumber() {

        assertEquals("512", patient.getHealthCardNumber());
    }


}
