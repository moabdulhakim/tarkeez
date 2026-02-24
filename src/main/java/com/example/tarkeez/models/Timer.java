package com.example.tarkeez.models;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

enum Status {WORK, BREAK};

public class Timer {
    public static int DEFAULT_DURATION = 5; //25 * 60;
    public static int DEFAULT_BREAK_DURATION = 3; // 5 * 60;

    private Label timerLabel;
    private Button startBtn;
    private Circle timerProgressCircle;
    private VBox timerCard;
    private Label sessionLabel;

    private RotateTransition rt;

    private Thread timerThread;
    private int timeRemainingInSeconds = DEFAULT_DURATION;
    private Status currentStatus = Status.WORK;
    private boolean isRunning = false;
    private int sessionsCount = 0;

    public Timer(Label timerLabel, Button startBtn, Circle timerProgressCircle, VBox timerCard, Label sessionLabel){
        this.timerLabel = timerLabel;
        this.startBtn = startBtn;
        this.timerProgressCircle = timerProgressCircle;
        this.timerCard = timerCard;
        this.sessionLabel = sessionLabel;

        updateLabel();

        rt = new RotateTransition(Duration.millis(10000), timerProgressCircle);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
    }

    public void startPause(){
        if(isRunning){
            reset();
            return;
        }

        isRunning = true;
        updateStartBtn();

        timerThread = new Thread(()->{
            while(timeRemainingInSeconds > 0 && isRunning){
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    break;
                }

                timeRemainingInSeconds--;
                updateLabel();

                if(timeRemainingInSeconds <= 0){
                    toggleStatus();
                }
            }
        });

        timerThread.start();
    }

    public void reset(){
        isRunning = false;
        timeRemainingInSeconds = DEFAULT_DURATION;

        updateLabel();
        updateStartBtn();

        sessionsCount--;
        currentStatus = Status.BREAK;
        toggleStatus();

        if(timerThread != null){
            timerThread.interrupt();
        }
    }

    public void toggleStatus(){
        if(currentStatus == Status.WORK){
            currentStatus = Status.BREAK;
            timeRemainingInSeconds = DEFAULT_BREAK_DURATION;
            rt.setRate(-1);
            rt.play();
        }else{
            currentStatus = Status.WORK;
            timeRemainingInSeconds = DEFAULT_DURATION;
            sessionsCount++;
            rt.setRate(1);
            rt.play();
        }

        Platform.runLater(()->{
            sessionLabel.setText("Session " + (sessionsCount+1));
            if(currentStatus == Status.BREAK){
                timerCard.getStyleClass().add("break");
                timerProgressCircle.getStyleClass().add("break");
            }else{
                timerCard.getStyleClass().remove("break");
                timerProgressCircle.getStyleClass().remove("break");
            }
        });
    }

    public void updateLabel(){
        int minutes = timeRemainingInSeconds / 60;
        int seconds = timeRemainingInSeconds % 60;
        String timeFormatted = String.format("%02d:%02d", minutes,seconds);

        Platform.runLater(()-> {
            timerLabel.setText(timeFormatted);
        });
    }

    public void updateStartBtn(){
        String btnText = isRunning? "Reset":"Start" ;
        startBtn.setText(btnText);

        Platform.runLater(()-> {
            if(isRunning){
                startBtn.getStyleClass().add("paused");
                rt.play();
            }else{
                startBtn.getStyleClass().remove("paused");
                rt.stop();
            }
        });
    }
}
