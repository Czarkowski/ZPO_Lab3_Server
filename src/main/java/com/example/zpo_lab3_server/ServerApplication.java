package com.example.zpo_lab3_server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerApplication extends Application {
    ServerController SC;
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResourceAsStream("Server.fxml"));
        SC = fxmlLoader.getController();
        System.out.println(SC);
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            SC.close();
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}