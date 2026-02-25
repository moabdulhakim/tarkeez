package com.example.tarkeez.models;
import java.util.function.Consumer;

public class Timer {
    public static int DEFAULT_DURATION = 25 * 60;
    public static int DEFAULT_BREAK_DURATION = 5 * 60;

    public int workDuration = DEFAULT_DURATION;
    public int breakDuration = DEFAULT_BREAK_DURATION;

    private Thread timerThread;
    private int timeRemainingInSeconds = workDuration;
    private boolean isBreak = false;
    private boolean isRunning = false;
    private int sessionsCount = 0;

    private Consumer<String> onTickAction;
    private StateChangeListener onChangeAction;

    public interface StateChangeListener {
        void onChange(boolean isRunning, boolean isBreak, int sessionsCount, TimerStateChangeEvent eventType);
    }

    public void setOnTickAction(Consumer<String> onTickAction){
        this.onTickAction = onTickAction;
    }

    public void setOnChangeAction(StateChangeListener onChangeAction){
        this.onChangeAction = onChangeAction;
    }

    public void setWorkDuration(int minutes){
        this.workDuration = minutes * 60;

        if(!isRunning && !isBreak){
            this.timeRemainingInSeconds = this.workDuration;
            updateLabel();
        }
    }

    public void setBreakDuration(int minutes){
        this.breakDuration = minutes * 60;

        if(!isRunning && isBreak){
            this.timeRemainingInSeconds = this.breakDuration;
            updateLabel();
        }

    }

    public void startPause(){
        if(isRunning){
            reset();
            return;
        }

        isRunning = true;
        if(onChangeAction != null)
            onChangeAction.onChange(isRunning, isBreak, sessionsCount, TimerStateChangeEvent.START_SESSION);

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
        isBreak = false;
        timeRemainingInSeconds = workDuration;

        updateLabel();

        onChangeAction.onChange(isRunning, isBreak, sessionsCount, TimerStateChangeEvent.RESET);

        if(timerThread != null){
            timerThread.interrupt();
        }
    }

    public void toggleStatus(){
        TimerStateChangeEvent eventType = TimerStateChangeEvent.BREAK_TIME;
        if(!isBreak){
            isBreak = true;
            timeRemainingInSeconds = breakDuration;
        }else{
            isBreak = false;
            timeRemainingInSeconds = workDuration;
            sessionsCount++;
            eventType = TimerStateChangeEvent.SESSION_INCREMENT;
        }

        onChangeAction.onChange(isRunning, isBreak, sessionsCount, eventType);
    }

    public void updateLabel(){
        int minutes = timeRemainingInSeconds / 60;
        int seconds = timeRemainingInSeconds % 60;
        String timeFormatted = String.format("%02d:%02d", minutes,seconds);

        if(onTickAction != null)
            onTickAction.accept(timeFormatted);
    }
}
