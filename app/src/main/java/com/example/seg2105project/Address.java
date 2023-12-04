package com.example.seg2105project;

import java.io.Serializable;

public class Address implements Serializable {
    private final String postalAddress;
    private final String postalCode;
    private final String city;
    private final String province;
    private final String country;

    /**
     * Represents the address of a {@link User}.
     * @param postalAddress the postal address of the user's address
     * @param postalCode the postal code of the user's address
     * @param city the city of the user's address
     * @param province the province of the user's address
     * @param country the country of the user's address
     * @see User
     */
    Address(String postalAddress, String postalCode,String city, String province, String country){
        this.postalAddress=postalAddress;
        this.postalCode=postalCode;
        this.city =city;
        this.province=province;
        this.country=country;
    }

    /**
     * Returns the user's postal address.
     * @return the postal address of the user
     */
    public String getPostalAddress() {
        return postalAddress;
    }

    /**
     * Returns the user's postal code.
     * @return the postal code of the user
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Returns the user's city.
     * @return the city of the user
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the user's province.
     * @return the province of the user
     */
    public String getProvince() {
        return province;
    }

    /**
     * Returns the user's country.
     * @return the country of the user
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @return a string representation of the address object
     */
    @Override
    public String toString() {
        String format = String.format("%s, %s, %s, %s", getPostalAddress(), getCity(), getProvince(), getPostalCode());
        return format;
    }

}
