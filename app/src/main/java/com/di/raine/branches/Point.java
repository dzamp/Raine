package com.di.raine.branches;

/**
 * Created by jim on 5/9/2016.
 */

public class Point {
    private double latitude;
    private double longitude;

    public Point() {
    }

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
