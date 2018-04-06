package application.Interfaces;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;

public interface UserRemote extends Remote {

    boolean funzLog(String usr, String pwd) throws RemoteException; //questa funzione controllerà se username e password sono corretti
    boolean logOut() throws RemoteException; //gestisce il bottone quando decido di fare il logout
    boolean save() throws RemoteException; //per il bottone save per quando si fanno dei cambiamenti
    void add() throws RemoteException; //per aggiungere bambini, fornitori, gite, staff
    void delete() throws RemoteException; //per eliminare quello detto prima
    void update() throws RemoteException; //per modificare quello detto prima





}
