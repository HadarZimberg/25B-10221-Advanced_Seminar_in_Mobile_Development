package com.example.mapdrawingsdk;

import java.util.List;

public class Polygon {
    private String id;
    private String label;
    private List<Point> points;

    public Polygon(String label, List<Point> points) {
        this.label = label;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public List<Point> getPoints() {
        return points;
    }
}
