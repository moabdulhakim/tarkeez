package com.example.tarkeez.models;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class AudioPlayer {
    MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    public void loadTrack(String trackName){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        String fileName = "/audio/" + trackName + ".wav";
        URL resource = getClass().getResource(fileName);

        if(resource == null){
            IO.println("Error: File not found, " + fileName);
            IO.println(resource);
            return;
        }

        Media media = new Media(resource.toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        mediaPlayer.setVolume(0.5);
        isPlaying = false;
    }

    public void togglePlayPause(){
        if(mediaPlayer == null) return;

        if(isPlaying)
            mediaPlayer.pause();
        else
            mediaPlayer.play();

        isPlaying = !isPlaying;
    }

    public void setVolume(double volume){
        if(mediaPlayer != null){
            mediaPlayer.setVolume(volume);
        }
    }

    public boolean isPlaying() {
        return  isPlaying;
    }

    public boolean isThereMediaPlayer(){
        return mediaPlayer != null;
    }
}
