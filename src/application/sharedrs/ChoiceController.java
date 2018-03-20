package application.sharedrs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.rmi.RemoteException;

/**
 * Created by ELISA on 20/03/2018.
 */


public class ChoiceController {

    @FXML
    private Button btnRmi;

    @FXML
    private Button btnSocket;


    //costruttore
    public void choice (ActionEvent event) throws RemoteException { //passo alla funzione la scelta RMI/SOCKET

        Button b = (Button) event.getSource();

        if(b.getId().equals(btnRmi.getId())){
            System.out.println("You chose rmi");

            RmiRemoteManager rmiobj = new RmiRemoteManager();

            rmiobj.getUserRemote().handleLogin();
        }
        else if(b.getId().equals(btnSocket.getId())){
            System.out.println("You chose socket");
        }
    }

}
