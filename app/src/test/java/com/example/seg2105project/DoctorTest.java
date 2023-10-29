package com.example.seg2105project;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

public class DoctorTest {
    @Test
    public void testGetEmployeeNumber() {
        Doctor doctor = new Doctor("TestDoctor", "One", "testdoctorone@example.com",
                "password123", "1234567890",
                new Address("123 Main St", "K1A 1A1",
                        "Ottawa", "Ontario", "Canada"),
                "EMP123", new ArrayList<>());

        assertEquals("EMP123", doctor.getEmployeeNumber());
    }

    @Test
    public void testGetSpecialties() {
        ArrayList<String> specialties = new ArrayList<>();
        specialties.add("Cardiology");
        specialties.add("Dermatology");

        Doctor doctor = new Doctor("TestDoctor", "Two", "testdoctortwo@example.com",
                "pass321", "9876543210",
                new Address("456 Elm St", "M2B 2B2",
                        "Toronto", "Ontario", "Canada"),
                "EMP456", specialties);

        assertEquals(specialties, doctor.getSpecialties());
    }
}
