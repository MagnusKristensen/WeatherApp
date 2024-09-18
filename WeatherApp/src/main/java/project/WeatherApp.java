package project;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WeatherApp extends Application{
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Weather App, Powered by Tomorrow.io");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("WeatherApp.fxml"))));
        primaryStage.show();
    }
}
