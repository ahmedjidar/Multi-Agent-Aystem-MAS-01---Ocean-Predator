package com.example.sma01.simulation;

import java.util.ArrayList;

public class Fish extends BaseObject {
    public static final double STEP = 3;
    public static final double MIN_DISTANCE = 5;
    public static final double MIN_SQUARED_DISTANCE = 25;
    public static final double MAX_DISTANCE = 40;
    public static final double MAX_SQUARED_DISTANCE = 1600;

    protected double velocityX;
    protected double velocityY;

    public Fish(double x, double y, double direction) {
        super(x, y);
        velocityX = Math.cos(direction);
        velocityY = Math.sin(direction);
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    protected void updatePosition() {
        posX += STEP * velocityX;
        posY += STEP * velocityY;
    }

    protected boolean isInAlignment(Fish other) {
        double squaredDistance = squaredDistanceTo(other);
        return (squaredDistance < MAX_SQUARED_DISTANCE && squaredDistance > MIN_SQUARED_DISTANCE);
    }

    protected void alignWithGroup(Fish[] fishes) {
        double avgVelocityX = 0;
        double avgVelocityY = 0;
        int count = 0;

        for (Fish fish : fishes) {
            if (fish != this && isInAlignment(fish)) {
                avgVelocityX += fish.getVelocityX();
                avgVelocityY += fish.getVelocityY();
                count++;
            }
        }

        if (count > 0) {
            velocityX = (velocityX + avgVelocityX / count) / 2;
            velocityY = (velocityY + avgVelocityY / count) / 2;
            normalize();
        }
    }

    protected boolean avoidOtherFish(Fish[] fishes) {
        for (Fish fish : fishes) {
            if (fish != this && squaredDistanceTo(fish) < MIN_SQUARED_DISTANCE) {
                double distance = distanceTo(fish);
                double diffX = (posX - fish.posX) / distance;
                double diffY = (posY - fish.posY) / distance;
                velocityX += diffX / 4; // Slight adjustment to move away
                velocityY += diffY / 4;
                normalize();
                return true;
            }
        }
        return false;
    }

    protected void normalize() {
        double length = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        velocityX /= length;
        velocityY /= length;
    }

    protected boolean avoidObstacles(ArrayList<AvoidZone> obstacles) {
        if (!obstacles.isEmpty()) {
            AvoidZone nearest = obstacles.get(0);
            double squaredDistance = squaredDistanceTo(nearest);
            for (AvoidZone zone : obstacles) {
                if (squaredDistanceTo(zone) < squaredDistance) {
                    nearest = zone;
                    squaredDistance = squaredDistanceTo(zone);
                }
            }

            if (squaredDistance < (nearest.radius * nearest.radius)) {
                double distance = Math.sqrt(squaredDistance);
                double diffX = (posX - nearest.posX) / distance;
                double diffY = (posY - nearest.posY) / distance;
                velocityX += diffX / 2; // Move away more assertively
                velocityY += diffY / 2;
                normalize();
                return true;
            }
        }
        return false;
    }

    protected void avoidWalls(double width, double height) {
        // Reflect off left or right wall
        if (posX <= MIN_DISTANCE || posX >= width - MIN_DISTANCE) {
            velocityX = -velocityX;
            posX = Math.max(MIN_DISTANCE, Math.min(posX, width - MIN_DISTANCE));
        }

        // Reflect off top or bottom wall
        if (posY <= MIN_DISTANCE || posY >= height - MIN_DISTANCE) {
            velocityY = -velocityY;
            posY = Math.max(MIN_DISTANCE, Math.min(posY, height - MIN_DISTANCE));
        }

        // Normalize after changes to maintain speed consistency
        normalize();
    }

    protected void update(Fish[] fishes, ArrayList<AvoidZone> obstacles, double width, double height) {
        if (!avoidObstacles(obstacles) && !avoidOtherFish(fishes)) {
            alignWithGroup(fishes);
        }
        avoidWalls(width, height);
        updatePosition();
    }
}


