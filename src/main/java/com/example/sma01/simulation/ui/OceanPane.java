package com.example.sma01.simulation.ui;

import com.example.sma01.simulation.AvoidZone;
import com.example.sma01.simulation.Fish;
import com.example.sma01.simulation.Ocean;
import com.example.sma01.simulation.Shark;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class OceanPane extends Canvas {
    private Ocean ocean;
    private AnimationTimer timer;

    private Image fishImage;
    private Image sharkImage;
    private Image seaweedImage;
    private Image treasureChestImage;

    public OceanPane() {
        this.setWidth(1080);
        this.setHeight(720);
        this.setOnMouseClicked(this::handleMouseClicked);

        // Load images
        fishImage = new Image(getClass().getResourceAsStream("/images/fish.png"));
        sharkImage = new Image(getClass().getResourceAsStream("/images/shark.png"));
        seaweedImage = new Image(getClass().getResourceAsStream("/images/seaweed.png"));
        treasureChestImage = new Image(getClass().getResourceAsStream("/images/treasure-chest.png"));
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

    private void drawOceanFloor(GraphicsContext gc) {
        double canvasHeight = this.getHeight();
        double canvasWidth = this.getWidth();

        // Draw seaweed (two instances)
        gc.drawImage(seaweedImage, canvasWidth * 0.2 - 20, canvasHeight - 80, 40, 60); // Left seaweed
        gc.drawImage(seaweedImage, canvasWidth * 0.8 - 20, canvasHeight - 80, 40, 60); // Right seaweed

        // Draw treasure chest
        gc.drawImage(treasureChestImage, canvasWidth * 0.5 - 30, canvasHeight - 60, 60, 40); // Centered chest
    }

    private void drawOcean() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());

        gc.setStroke(Color.BLACK);
        for (Fish fish : ocean.fishes) {
            if (fish != null) {
                drawFish(gc, fish);
            }
        }

        gc.setStroke(Color.RED);
        for (AvoidZone zone : ocean.obstacles) {
            drawObstacle(gc, zone);
        }

        gc.setFill(Color.DARKBLUE);
        drawShark(gc, ocean.shark);

        gc.setFill(Color.DARKBLUE);
        gc.setFont(new Font("Arial", 20));
        gc.fillText("🔥 Shark Feast! Score: " + ocean.shark.getScore() + " 🦈", 10, 30);

        drawOceanFloor(gc);
    }

    private void drawFish(GraphicsContext gc, Fish fish) {
        gc.drawImage(fishImage, fish.posX - 10, fish.posY - 10, 20, 20); // Center and scale
    }

    private void drawShark(GraphicsContext gc, Shark shark) {
        gc.drawImage(sharkImage, shark.posX - 15, shark.posY - 15, 50, 50); // Larger size for shark
    }

    private void drawObstacle(GraphicsContext gc, AvoidZone zone) {
        gc.strokeOval(zone.posX - zone.getRadius(), zone.posY - zone.getRadius(), zone.getRadius() * 2, zone.getRadius() * 2);
    }
}


