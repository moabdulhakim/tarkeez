package com.example.tarkeez;

import com.example.tarkeez.models.Timer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

public class EditorController {
    private Timer timer;
    @FXML
    private ToggleButton themeToggleBtn;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Circle timerProgressCircle;

    @FXML
    private Label timerLabel;

    @FXML
    private Button startBtn;

    @FXML
    private VBox timerCard;

    @FXML
    private Label sessionLabel;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    public void initialize(){
        timer = new Timer(timerLabel, startBtn, timerProgressCircle, timerCard, sessionLabel);

        startBtn.setOnAction(event-> timer.startPause());

        themeToggleBtn.setOnAction(this::toggleTheme);

    }

    void toggleTheme(ActionEvent event){
        boolean isLightMode = themeToggleBtn.isSelected();
        if(isLightMode){
            rootPane.getStyleClass().add("light-mode");
            themeToggleBtn.setText("Dark");
        } else {
            rootPane.getStyleClass().remove("light-mode");
            themeToggleBtn.setText("Light");
        }

        // - AI GENERATED -------------------------------------
        // 2. السطرين دول هما الحل: تغيير لون مساحة الكتابة الداخلية (HTML)
        WebView webView = (WebView) htmlEditor.lookup("WebView");
        if (webView != null) {
            if (isLightMode) {
                // تحويل مساحة الكتابة للوضع الفاتح
                webView.getEngine().executeScript(
                        "document.body.style.backgroundColor = '#ffffff';" +
                                "document.body.style.color = '#2d3436';"
                );
            } else {
                // إعادتها للوضع الغامق
                webView.getEngine().executeScript(
                        "document.body.style.backgroundColor = '#1e2233';" +
                                "document.body.style.color = '#f0f0f0';"
                );
            }
        }
        // -------------------------------------------
    }
}
