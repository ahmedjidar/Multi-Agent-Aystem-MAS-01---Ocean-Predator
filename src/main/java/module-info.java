module com.example.sma01 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sma01 to javafx.fxml;
    exports com.example.sma01.simulation.ui;
    exports com.example.sma01;
}