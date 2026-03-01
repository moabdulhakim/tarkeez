package com.example.tarkeez;

import com.example.tarkeez.models.AudioPlayer;
import com.example.tarkeez.models.Timer;
import com.example.tarkeez.models.TimerStateChangeEvent;
import com.example.tarkeez.services.FileManager;
import com.example.tarkeez.utils.Toast;
import com.example.tarkeez.utils.ToastStatus;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TarkeezController {
    private Timer timer;
    private AudioPlayer audioPlayer;
    private FileManager fileManager;

    private RotateTransition rt;

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
        fileManager = new FileManager();
        // ----------------------------------

        timer = new Timer();

        rt = new RotateTransition(Duration.millis(10000), timerProgressCircle);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);

        startBtn.setOnAction(event-> timer.startPause());

        timer.setOnTickAction(this::timerTickAction);
        timer.setOnChangeAction(this::timerOnChangeAction);
        timer.updateLabel();
        //  ------------------------------------

        audioPlayer = new AudioPlayer();
        setupWaveformAnimations();
        audioTrackSelector.getItems().addAll("Rain", "Forest", "Ocean");
        audioVolumeSlider.valueProperty().addListener(this::handleVolumeChange);
    }

    private void showToast(String msg, ToastStatus ts){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        Toast.show(msg, ts, stage);
    }

    private void timerTickAction(String timeFormatted){
        Platform.runLater(()-> {
            timerLabel.setText(timeFormatted);
        });
    }
    private void timerOnChangeAction(boolean isRunning,boolean isBreak,int sessionsCount,TimerStateChangeEvent eventType ) {
        String btnText = isRunning? "Reset":"Start" ;

        Platform.runLater(()->{
            startBtn.setText(btnText);

            if(isRunning){
                if(!startBtn.getStyleClass().contains("paused"))
                    startBtn.getStyleClass().add("paused");
                rt.play();
            }else{
                startBtn.getStyleClass().remove("paused");
                rt.stop();
            }

            sessionLabel.setText("Session " + (sessionsCount+1));
            if(isBreak){
                timerCard.getStyleClass().add("break");
                timerProgressCircle.getStyleClass().add("break");
                rt.setRate(-1);
                rt.play();
            }else{
                timerCard.getStyleClass().remove("break");
                timerProgressCircle.getStyleClass().remove("break");
                rt.setRate(1);
            }
        });

        Platform.runLater(()->{
            switch (eventType){
                case TimerStateChangeEvent.START_SESSION:
                    showToast("Session Started!", ToastStatus.INFO);
                    break;
                case TimerStateChangeEvent.RESET:
                    showToast("Timer reset!", ToastStatus.INFO);
                    break;
                case TimerStateChangeEvent.BREAK_TIME:
                    showToast("Alhamdulillah , Your work time completed!", ToastStatus.SUCCESS);
                    break;
                case TimerStateChangeEvent.SESSION_INCREMENT:
                    String sessions = sessionsCount == 1? "one session":(sessionsCount + " sessions");
                    showToast(("Alhamdulillah , You have completed " + sessions + ", Keep going!"), ToastStatus.SUCCESS);
                    break;
            }
        });
    }

    @FXML
    private void handleNew(){
        String innerHtmlDark = "<html><body style='background-color: #1e2233; color: #f0f0f0;'><p>Start Writing Your Thoughts...</p></body></html>";
        String innerHtmlLight = "<html><body><p>Start Writing Your Thoughts...</p></body></html>";

        boolean isLightMode = themeToggleBtn.isSelected();
        htmlEditor.setHtmlText(isLightMode? innerHtmlLight:innerHtmlDark);
        fileManager.resetCurrentFile();

        showToast("New note has been loaded successfully!", ToastStatus.SUCCESS);
    }

    @FXML
    private void handleLoadFile(){
        try{
            String content = fileManager.loadFile(rootPane.getScene().getWindow());

            if(content != null){
                htmlEditor.setHtmlText(content);
                showToast((fileManager.getCurrentFile().getName() + " loaded successfully!"), ToastStatus.SUCCESS);
            }else{
                showToast("No note has been loaded!", ToastStatus.INFO);
            }
        }catch(FileNotFoundException e) {
            showToast("Something went wrong while loading your note!", ToastStatus.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave(){
        try{
            boolean success = fileManager.saveFile(htmlEditor.getHtmlText(), rootPane.getScene().getWindow());

            if(success){
                String fileName = fileManager.getCurrentFile().getName();
                showToast(("Your Note: " + fileName + " saved successfully!"), ToastStatus.SUCCESS);
            }else{
                showToast("Your note has not been saved!", ToastStatus.INFO);
            }
        }catch (java.io.IOException e){
            showToast("Something went wrong while saving your note!", ToastStatus.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveAs(){
        try{
            boolean success = fileManager.saveAsFile(htmlEditor.getHtmlText(), rootPane.getScene().getWindow());

            if(success){
                String fileName = fileManager.getCurrentFile().getName();
                showToast(("Your Note: " + fileName + " saved successfully!"), ToastStatus.SUCCESS);
            }else{
                showToast("Your note has not been saved!", ToastStatus.INFO);
            }
        }catch(IOException e){
            showToast("Something went wrong while saving your note!", ToastStatus.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void openSettings(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            SettingsController settingsController = loader.getController();
            settingsController.setTimerModel(this.timer);

            Stage settingsStage = new Stage();
            settingsStage.setTitle("Tarkeez: Settings");

            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.setResizable(false);

            if(themeToggleBtn.isSelected()){
                root.getStyleClass().add("light-mode");
            }

            settingsStage.setScene(scene);
            settingsStage.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSelectAudio(){
        String newVal = audioTrackSelector.getValue();

        if(newVal != null){
            audioPlayer.loadTrack(newVal);
            playButton.setText("▶");
            audioVolumeSlider.setValue(50.0);
            toggleWaves(false);
        }
    }

    @FXML
    private void handleAudioPlayPause(){
        if(!audioPlayer.isThereMediaPlayer()) showToast("Please Choose an audio track to play.", ToastStatus.INFO);
        audioPlayer.togglePlayPause();
        boolean isPlaying = audioPlayer.isPlaying();

        playButton.setText(isPlaying? "⏸":"▶");
        toggleWaves(isPlaying);
    }

    private void handleVolumeChange(ObservableValue<? extends Number> obs, Number oldVal,Number newVal){
        audioPlayer.setVolume(newVal.doubleValue() / 100.0);
    }

    @FXML
    private void toggleTheme(ActionEvent event){
        boolean isLightMode = themeToggleBtn.isSelected();
        if(isLightMode){
            rootPane.getStyleClass().add("light-mode");
            themeToggleBtn.setText("Dark");
            showToast("Light mode applied!", ToastStatus.SUCCESS);
        } else {
            rootPane.getStyleClass().remove("light-mode");
            themeToggleBtn.setText("Light");
            showToast("Dark mode applied!", ToastStatus.SUCCESS);
        }

        refreshWebView();
    }

    // - AI GENERATED -------------------------------------
    private void refreshWebView(){
        boolean isLightMode = themeToggleBtn.isSelected();
        // 2. السطرين دول هما الحل: تغيير لون مساحة الكتابة الداخلية (HTML)
        WebView webView = (WebView) htmlEditor.lookup("WebView");
        IO.println(isLightMode);
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
