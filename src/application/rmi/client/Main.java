package application.rmi.client;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class Main extends Application {


	@FXML
	private ChoiceBox select;
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
	
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../../gui/LoginUser.fxml"));
			Scene scene = new Scene(root, 410, 502);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Login");
			primaryStage.setScene(scene);
			primaryStage.show();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


}