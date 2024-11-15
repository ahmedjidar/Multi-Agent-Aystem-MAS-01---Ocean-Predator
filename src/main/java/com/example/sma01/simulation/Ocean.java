package com.example.sma01.simulation;

import java.util.ArrayList;
import java.util.Random;

public class Ocean {
    public Fish[] fishes;
    public ArrayList<AvoidZone> obstacles;
    protected Random generator;
    protected double width;
    protected double height;

    public Ocean(int fishCount, double width, double height) {
        this.width = width;
        this.height = height;
        generator = new Random();
        obstacles = new ArrayList<>();
        fishes = new Fish[fishCount];
        for (int i = 0; i < fishCount; i++) {
            fishes[i] = new Fish(generator.nextDouble() * width, generator.nextDouble() * height, generator.nextDouble() * 2 * Math.PI);
        }
    }

    public void addObstacle(double x, double y, double radius) {
        obstacles.add(new AvoidZone(x, y, radius));
    }

    protected void updateObstacles() {
        for (AvoidZone zone : obstacles) {
            zone.update();
        }
        obstacles.removeIf(AvoidZone::isExpired);
    }

    protected void updateFishes() {
        for (Fish fish : fishes) {
            fish.update(fishes, obstacles, width, height);
        }
    }

    public void updateOcean() {
        updateObstacles();
        updateFishes();
    }
}

