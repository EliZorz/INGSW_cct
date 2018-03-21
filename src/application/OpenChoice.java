package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class OpenChoice extends Application {

    public static void main(String[] args){
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/application/Choice.fxml"));

        Scene scene = new Scene(root);



        primaryStage.setScene(scene);
        primaryStage.setTitle("Choice");

        primaryStage.show();
       


    }
}
