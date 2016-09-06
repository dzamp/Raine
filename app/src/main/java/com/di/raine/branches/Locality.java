package com.di.raine.branches;

/**
 * Created by jim on 5/9/2016.
 */

public class Locality {
    private String city;
    private String postalCode;
    private String address;
    private Point point;

    public Locality() {
    }

    public Locality(String city, String postalCode, String address, Point point) {
        this.city = city;
        this.postalCode = postalCode;
        this.address = address;
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
