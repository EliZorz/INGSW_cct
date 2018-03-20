package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by ELISA on 20/03/2018.
 */
public class ImplemLog extends Application implements Inter {
    @Override
    public void start(Stage primaryStage) throws RemoteException {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginUser.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String sendMessage(String clientMessage) throws RemoteException {
        return null;
    }

    public void anim(){
        launch();
    }

    @Override
    public void add(String who) throws RemoteException {

    }

    @Override
    public void upd(String who, String cod) throws RemoteException {

    }

    @Override
    public void del(String who, String cod) throws RemoteException {

    }
}
