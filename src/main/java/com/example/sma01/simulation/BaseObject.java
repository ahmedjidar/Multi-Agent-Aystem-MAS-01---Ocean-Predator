package com.example.sma01.simulation;

public class BaseObject {
    public double posX;
    public double posY;

    public BaseObject() { }

    public BaseObject(double x, double y) {
        posX = x;
        posY = y;
    }

    public double distanceTo(BaseObject other) {
        return Math.sqrt((other.posX - posX) * (other.posX - posX) + (other.posY - posY) * (other.posY - posY));
    }

    public double squaredDistanceTo(BaseObject other) {
        return (other.posX - posX) * (other.posX - posX) + (other.posY - posY) * (other.posY - posY);
    }
}


