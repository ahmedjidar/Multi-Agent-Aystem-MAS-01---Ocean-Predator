package com.example.sma01.simulation;

public class AvoidZone extends BaseObject {
    protected double radius;
    protected int remainingTime = 500;

    public AvoidZone(double x, double y, double radius) {
        super(x, y);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void update() {
        remainingTime--;
    }

    public boolean isExpired() {
        return remainingTime <= 0;
    }
}