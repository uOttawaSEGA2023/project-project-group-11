package com.example.seg2105project;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AddressTest {
    private final Address goodAddress  = new Address("123 Main St",
            "K1A 1A1", "Ottawa", "Ontario", "Canada");

    @Test
    public void testGetPostalAddress() {
        assertEquals("123 Main St", goodAddress.getPostalAddress());
    }

    @Test
    public void testGetPostalCode() {
        assertEquals("K1A 1A1", goodAddress.getPostalCode());
    }

    @Test
    public void testGetCity(){
        assertEquals("Ottawa", goodAddress.getCity());
    }

    @Test
    public void testGetProvince(){
        assertEquals("Ontario", goodAddress.getProvince());
    }

    @Test
    public void testGetCountry(){
        assertEquals("Canada", goodAddress.getCountry());
    }
}
