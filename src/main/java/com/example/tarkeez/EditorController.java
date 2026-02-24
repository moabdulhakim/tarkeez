package com.example.tarkeez;

import com.example.tarkeez.models.AudioPlayer;
import com.example.tarkeez.models.Timer;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class EditorController {
    private Timer timer;
    private AudioPlayer audioPlayer;

    @FXML
    private HTMLEditor htmlEditor;

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

    // -- Audio Player Attributes --------------------
    @FXML
    private ComboBox<String> audioTrackSelector;

    @FXML
    private Button playButton;

    @FXML
    private Slider audioVolumeSlider;

    @FXML private HBox waveformContainer;
    private List<ScaleTransition> waveAnimations = new ArrayList<>();


    @FXML
    public void initialize(){
        themeToggleBtn.setOnAction(this::toggleTheme);

        timer = new Timer(timerLabel, startBtn, timerProgressCircle, timerCard, sessionLabel);
        startBtn.setOnAction(event-> timer.startPause());

        audioPlayer = new AudioPlayer();
        setupWaveformAnimations();
        audioTrackSelector.getItems().addAll("Rain", "Forest", "Ocean");
        audioTrackSelector.getSelectionModel().selectedItemProperty().addListener(this::handleSelectAudio);
        playButton.setOnAction(this::handleAudioPlayPause);
        audioVolumeSlider.valueProperty().addListener(this::handleVolumeChange);
    }

    private void handleSelectAudio(ObservableValue<?extends  String> obs, String oldVal, String newVal){
        if(newVal != null){
            audioPlayer.loadTrack(newVal);
            playButton.setText("▶");
            audioVolumeSlider.setValue(50.0);
            toggleWaves(false);
        }
    }

    private void handleAudioPlayPause(ActionEvent e){
        audioPlayer.togglePlayPause();
        boolean isPlaying = audioPlayer.isPlaying();

        playButton.setText(isPlaying? "⏸":"▶");
        toggleWaves(isPlaying);
    }

    private void handleVolumeChange(ObservableValue<? extends Number> obs, Number oldVal,Number newVal){
        audioPlayer.setVolume(newVal.doubleValue() / 100.0);
    }

    private void toggleTheme(ActionEvent event){
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

    // - AI GENERATED -------------------------------------
    private void setupWaveformAnimations() {
        int i = 0;
        int totalBars = waveformContainer.getChildren().size();

        for (Node node : waveformContainer.getChildren()) {
            Region bar = (Region) node;

            // 1. تحديد درجة اللون بناءً على الطول أو الترتيب
            // هنا بنستخدم الـ prefHeight عشان نغير الشفافية أو درجة اللون
            double height = bar.getPrefHeight();
            double opacity = 0.4 + (height / 40.0); // كل ما كان العمود أطول، كان لونه أوضح

            // تدرج لوني بناءً على ترتيب العمود (من الأزرق إلى الأخضر المزرق)
            double hue = 200 + ( (double)i / totalBars * 40); // درجات الأزرق في الـ HSB
            bar.setStyle("-fx-background-color: hsb(" + hue + ", 80%, 90%);");

            // 2. إعداد الأنيميشن (نفس الكود السابق)
            double randomDuration = 300 + (Math.random() * 400);
            ScaleTransition st = new ScaleTransition(Duration.millis(randomDuration), bar);

            st.setFromY(0.6);
            st.setToY(1.4);
            st.setAutoReverse(true);
            st.setCycleCount(Animation.INDEFINITE);

            waveAnimations.add(st);
            i++;
        }
    }

    // - AI GENERATED -------------------------------------
    private void toggleWaves(boolean isPlaying) {
        if (isPlaying) {
            for (ScaleTransition st : waveAnimations) {
                st.play();
            }
        } else {
            for (ScaleTransition st : waveAnimations) {
                st.pause();
                // إرجاع الأعمدة لحجمها الطبيعي عند الإيقاف
                ((Node) st.getNode()).setScaleY(1.0);
            }
        }
    }
}
