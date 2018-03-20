package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 * Created by ELISA on 15/03/2018.
 */
public class Client extends Application {

    public static void main(String[] args) {

        launch(args);
    }


    public void start(Stage primaryStage) throws RemoteException {

        try {
            Registry registry = LocateRegistry.getRegistry();
            Inter pippo = (Inter) registry.lookup("Inter");
            String reply = pippo.sendMessage("Client hello");


            System.out.print("RMI registry bindings: ");
            String[] e = registry.list();
            for (int i = 0; i < e.length; i++) {
                System.out.println(e[i]);
            }

        } catch (RemoteException re) {
            re.printStackTrace();
        } catch (NotBoundException nbe) {
            nbe.printStackTrace();
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginUser.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}