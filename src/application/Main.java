package application;


import java.awt.*;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
	
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("LoginUser.fxml"));
			
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
			primaryStage.setScene(scene);
			primaryStage.show();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}