package application.Interfaces;

import netscape.javascript.JSObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserRemote extends Remote {
    JSObject handleLogin() throws RemoteException; //questa funzione controllerà se username e password sono corrtti
    JSObject logOut() throws RemoteException; //gestisce il bottone quando decido di fare il logout
    JSObject save() throws RemoteException; //per il bottone save per quando si fanno dei cambiamenti
    JSObject add() throws RemoteException; //per aggiungere bambini, fornitori, gite, staff
    JSObject delete() throws RemoteException; //per eliminare quello detto prima
    JSObject update() throws RemoteException; //per modificare quello detto prima

}
