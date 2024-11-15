package com.example.sma01.simulation;

import java.util.ArrayList;

public class Shark extends BaseObject {
    public static final double STEP = 2;
    protected double velocityX;
    protected double velocityY;
    private int score = 0;

    public Shark(double x, double y, double direction) {
        super(x, y);
        velocityX = Math.cos(direction);
        velocityY = Math.sin(direction);
    }

    public int getScore() {
        return score;
    }

    protected void normalize() {
        double length = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        velocityX /= length;
        velocityY /= length;
    }

    private void updatePosition() {
        posX += STEP * velocityX;
        posY += STEP * velocityY;
    }

    private boolean chaseFish(Fish[] fishes) {
        Fish closestFish = null;
        double closestDistance = Double.MAX_VALUE;

        // Find the closest fish
        for (Fish fish : fishes) {
            if (fish != null) { // Skip null fish
                double distance = distanceTo(fish);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestFish = fish;
                }
            }
        }

        // If a fish is close enough, chase it
        if (closestFish != null) {
            double diffX = (closestFish.posX - posX);
            double diffY = (closestFish.posY - posY);

            // Adjust velocity to chase the fish
            velocityX += diffX / closestDistance * 0.1; // Scaled factor for smoother movement
            velocityY += diffY / closestDistance * 0.1;
            normalize(); // Ensure consistent speed
            updatePosition(); // Move the shark in the new direction

            // Check if the shark can eat the fish
            if (closestDistance < 10) { // Eating threshold
                score++;
                for (int i = 0; i < fishes.length; i++) {
                    if (fishes[i] == closestFish) {
                        fishes[i] = null; // Remove eaten fish
                        break;
                    }
                }
            }
            return true;
        }

        return false; // No fish to chase
    }

    private boolean avoidObstacles(ArrayList<AvoidZone> obstacles) {
        if (!obstacles.isEmpty()) {
            AvoidZone nearest = obstacles.get(0);
            double squaredDistance = squaredDistanceTo(nearest);
            for (AvoidZone zone : obstacles) {
                if (squaredDistanceTo(zone) < squaredDistance) {
                    nearest = zone;
                    squaredDistance = squaredDistanceTo(zone);
                }
            }

            if (squaredDistance < (nearest.getRadius() * nearest.getRadius())) {
                double distance = Math.sqrt(squaredDistance);
                double diffX = (nearest.posX - posX) / distance;
                double diffY = (nearest.posY - posY) / distance;
                velocityX = velocityX - diffX / 2;
                velocityY = velocityY - diffY / 2;
                normalize();
                return true;
            }
        }
        return false;
    }

    private void avoidWalls(double width, double height) {
        if (posX <= 0 || posX >= width) velocityX = -velocityX;
        if (posY <= 0 || posY >= height) velocityY = -velocityY;
        normalize();
    }

    public void update(Fish[] fishes, ArrayList<AvoidZone> obstacles, double width, double height) {
        boolean actionTaken = false;

        // Attempt to chase fish
        if (chaseFish(fishes)) {
            actionTaken = true;
        }

        // Avoid obstacles if necessary
        if (avoidObstacles(obstacles)) {
            actionTaken = true;
        }

        // Reflect from walls if at boundaries
        avoidWalls(width, height);

        // If no action was taken, continue moving in the current direction
        if (!actionTaken) {
            updatePosition();
        }
    }
}

