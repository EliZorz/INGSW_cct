package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client extends Application {
    private Client()throws Exception {}
    @Override
    public void start(Stage primaryStage){}

    public static void main(String[] args) {  //si connette al server, risponde al server
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(5723);  //al posto dell'host gli metto la porta

            // Looking up the registry for the remote object
            Inter stub = (Inter) registry.lookup("Inter");

            // Calling the remote method using the obtained object


             System.out.println("Remote method invoked");
        } catch (Exception e) {
            System.out.println("Client exception: " + e.toString());
            e.printStackTrace();
        }


    }


}
