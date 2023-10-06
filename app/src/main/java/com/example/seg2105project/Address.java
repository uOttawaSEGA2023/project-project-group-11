package com.example.seg2105project;

public class Address {
    private String postalAddress;
    private String postalCode;
    private String city;
    private String province;
    private String country;

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
