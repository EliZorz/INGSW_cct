package application.Interfaces;



import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserRemote extends Remote {
    boolean logOut() throws RemoteException; //gestisce il bottone quando decido di fare il logout
    boolean save() throws RemoteException; //per il bottone save per quando si fanno dei cambiamenti
    void add() throws RemoteException; //per aggiungere bambini, fornitori, gite, staff
    void delete() throws RemoteException; //per eliminare quello detto prima
    void update() throws RemoteException; //per modificare quello detto prima

}
