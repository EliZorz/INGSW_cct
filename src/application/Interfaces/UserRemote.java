package application.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserRemote extends Remote {
    //boolean handleLogin() throws RemoteException; //questa funzione controllerà se username e password sono corrtti
    void logOut() throws RemoteException; //gestisce il bottone quando decido di fare il logout
    void save() throws RemoteException; //per il bottone save per quando si fanno dei cambiamenti
    void add() throws RemoteException; //per aggiungere bambini, fornitori, gite, staff
    void delete() throws RemoteException; //per eliminare quello detto prima
    void update() throws RemoteException; //per modificare quello detto prima

}
