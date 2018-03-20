package application.Interfaces;



import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserRemote extends Remote {
    JSONObject handleLogin() throws RemoteException; //questa funzione controllerà se username e password sono corrtti
    JSONObject logOut() throws RemoteException; //gestisce il bottone quando decido di fare il logout
    JSONObject save() throws RemoteException; //per il bottone save per quando si fanno dei cambiamenti
    JSONObject add() throws RemoteException; //per aggiungere bambini, fornitori, gite, staff
    JSONObject delete() throws RemoteException; //per eliminare quello detto prima
    JSONObject update() throws RemoteException; //per modificare quello detto prima

}
