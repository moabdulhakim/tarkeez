package com.example.tarkeez;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TarkeezApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TarkeezApp.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()) ;
        stage.setTitle("Tarkeez: Focus and Write notes!");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

}
