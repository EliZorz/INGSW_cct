package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;


public class MenuIniziale extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

           Parent root = FXMLLoader.load(getClass().getResource("/application/MenuIniziale.fxml"));
           //Pane mainPane = (Pane)FXMLLoader.load(getClass().getResource("/application/MenuIniziale.fxml"));
           Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
           Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());


            //Scene scene = new Scene(mainPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Menu Iniziale");
            //primaryStage.sizeToScene();
            primaryStage.show();
            //primaryStage.setFullScreen(true); //visione a schermo intero
             //primaryStage.setResizable(false); per farsì che non si possa ridimensionare la finestra


    }

    public static void main(String[] args) {
       launch(args);


       /* JFrame Menu = new JFrame("Pagina iniziale");
        Container c = Menu.getContentPane();
        c.add(new JLabel("Benvenuto"));
        Menu.setSize(1200,1200);
        Menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Menu.setVisible(true);
        */
    }




}
