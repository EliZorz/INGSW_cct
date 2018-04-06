package application;


import javafx.scene.control.MenuItem;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by ELISA on 15/03/2018.
 */
public interface Inter extends Remote {

    //GESTITE DA SERVER
    String sendMessage(String clientMessage) throws RemoteException;
    //metodo login vd ChatServer

    //public Vector getConnected() throws RemoteException;

    public void add (String who) throws RemoteException;

    public void upd (String who, String cod) throws RemoteException;

    public void del (String who, String cod) throws RemoteException;



}
