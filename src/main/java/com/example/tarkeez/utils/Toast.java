package com.example.tarkeez.utils;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Toast {
    public static void show(String msg, ToastStatus toastStatus, Stage stage){
        Popup popup = new Popup();
        popup.setAutoFix(true);

        String status =
                toastStatus == ToastStatus.SUCCESS
                        ? "success"
                        : toastStatus == ToastStatus.ERROR
                        ? "error"
                        : toastStatus == ToastStatus.INFO
                        ? "info"
                        :"";

        Label label = new Label(msg);
        label.getStyleClass().addAll("toast-pill", status);

        popup.getContent().add(label);

        popup.show(stage);

        popup.setX(stage.getX() + (stage.getWidth()/2) - (popup.getWidth()/2));
        popup.setY(stage.getY() + stage.getHeight() - 100);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), label);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished((e)->{
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), label);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished((e2)-> {
                popup.hide();
            });
            fadeOut.play();
        });
        delay.play();
    }
}
