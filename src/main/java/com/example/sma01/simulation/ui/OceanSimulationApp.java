package com.example.sma01.simulation.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OceanSimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        OceanPane oceanPane = new OceanPane();
        StackPane root = new StackPane(oceanPane);
        Scene scene = new Scene(root, 1080, 720);

        primaryStage.setTitle("Fish Simulation - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        oceanPane.startSimulation();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
