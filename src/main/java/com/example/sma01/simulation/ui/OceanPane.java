package com.example.sma01.simulation.ui;

import com.example.sma01.simulation.AvoidZone;
import com.example.sma01.simulation.Fish;
import com.example.sma01.simulation.Ocean;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class OceanPane extends Canvas {
    private Ocean ocean;
    private AnimationTimer timer;

    public OceanPane() {
        this.setWidth(600);
        this.setHeight(400);
        this.setOnMouseClicked(this::handleMouseClicked);
    }

    public void startSimulation() {
        ocean = new Ocean(250, this.getWidth(), this.getHeight());

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                ocean.updateOcean();
                drawOcean();
            }
        };
        timer.start();
    }

    private void handleMouseClicked(MouseEvent event) {
        ocean.addObstacle(event.getX(), event.getY(), 10);
    }

    private void drawOcean() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());

        gc.setStroke(Color.BLACK);
        for (Fish fish : ocean.fishes) {
            drawFish(gc, fish);
        }

        gc.setStroke(Color.RED);
        for (AvoidZone zone : ocean.obstacles) {
            drawObstacle(gc, zone);
        }
    }

    private void drawFish(GraphicsContext gc, Fish fish) {
        double endX = fish.posX - 10 * fish.getVelocityX();
        double endY = fish.posY - 10 * fish.getVelocityY();
        gc.strokeLine(fish.posX, fish.posY, endX, endY);
    }

    private void drawObstacle(GraphicsContext gc, AvoidZone zone) {
        gc.strokeOval(zone.posX - zone.getRadius(), zone.posY - zone.getRadius(), zone.getRadius() * 2, zone.getRadius() * 2);
    }
}


