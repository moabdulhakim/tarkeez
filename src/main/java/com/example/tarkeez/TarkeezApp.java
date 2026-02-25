package com.example.tarkeez;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class TarkeezApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TarkeezApp.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()) ;

        Image logo = new Image(getClass().getResourceAsStream("/images/icon.png"));
        stage.getIcons().add(logo);

        stage.setTitle("Tarkeez: Focus and Write notes!");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

}
