package com.example.seg2105project;

import java.io.Serializable;

public class Address implements Serializable {
    private final String postalAddress;
    private final String postalCode;
    private final String city;
    private final String province;
    private final String country;

    Address(String postalAddress, String postalCode,String city, String province, String country){
        this.postalAddress=postalAddress;
        this.postalCode=postalCode;
        this.city =city;
        this.province=province;
        this.country=country;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }
}
