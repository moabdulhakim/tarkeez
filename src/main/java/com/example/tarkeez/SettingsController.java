package com.example.tarkeez;

import com.example.tarkeez.models.Timer;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class SettingsController {
    Timer timerModel;

    @FXML
    private Spinner<Integer> pomodoroSpinner;

    @FXML
    private Spinner<Integer> breakSpinner;

    @FXML
    public void initialize(){
        pomodoroSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, 25));
        breakSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, 5));
    }

    public void setTimerModel(Timer timerModel){
        this.timerModel = timerModel;
    }

    @FXML
    public void handleSave(){
        if(timerModel != null){
            int newWorkDuration = pomodoroSpinner.getValue(); // minutes
            int newBreakDuration = breakSpinner.getValue(); // minutes

            timerModel.setWorkDuration(newWorkDuration);
            timerModel.setBreakDuration(newBreakDuration);
        }
        closeWindow();
    }

    @FXML
    public void handleCancel(){
        closeWindow();
    }

    @FXML
    public void closeWindow(){
        Stage stage = (Stage) pomodoroSpinner.getScene().getWindow();
        stage.close();
    }
}
