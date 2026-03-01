package com.example.tarkeez.services;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileManager {
    private File currentFile = null;

    public File getCurrentFile(){
        return currentFile;
    }

    public void resetCurrentFile(){
        currentFile = null;
    }

    public String loadFile(Window ownerWindow) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Note");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html"));

        File file = fileChooser.showOpenDialog(ownerWindow);

        if(file != null){
            StringBuilder content = new StringBuilder();
            try(Scanner scanner = new Scanner(file)){
                while(scanner.hasNextLine()){
                    content.append(scanner.nextLine()).append('\n');
                }
                currentFile = file;
                return content.toString();
            }
        }
        return null;
    }

    private boolean writeToFile(String content, File file) throws IOException {
        try(PrintWriter writer = new PrintWriter(file)){
            writer.println(content);
            currentFile = file;
            IO.println("Saved to: " + file.getAbsolutePath());
            return true;
        }
    }

    public boolean saveAsFile(String content, Window ownerWindow) throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Note");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html"));

        fileChooser.setInitialFileName("Untitled.html");

        File file = fileChooser.showSaveDialog(ownerWindow);

        if(file != null){
            return writeToFile(content, file);
        }
        return false;
    }

    public boolean saveFile(String content, Window ownerWindow) throws IOException{
        if(currentFile != null){
            return writeToFile(content, currentFile);
        }else{
            return saveAsFile(content, ownerWindow);
        }
    }
}
