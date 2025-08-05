package com.example.apiserver.model;

import java.util.List;

public class Polygon {
    private String id;  
    private String label;
    private List<Point> points;

    public Polygon() {
    }

    public Polygon(String id, String label, List<Point> points) {
        this.id = id;
        this.label = label;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
    
    @Override
    public String toString() {
        return "Polygon{id=" + id + ", label=" + label + ", points=" + points + "}";
    }
}
