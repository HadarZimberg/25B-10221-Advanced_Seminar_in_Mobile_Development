package com.example.apiserver.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Point {
	
	@JsonProperty("lat")
    private double lat;

    @JsonProperty("lng")
    private double lng;

    public Point() {}

    public Point(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Point{lat=" + lat + ", lng=" + lng + "}";
    }
}
