package com.example.courswork.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {


    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                Launcher.class.getResource("/com.example.courswork/hello-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 900, 650);

        stage.setTitle("Malabe Tuk Tuk Spare Parts Management System");

        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}