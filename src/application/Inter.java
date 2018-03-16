package application;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Inter extends Remote {  //funzioni possibili che vengano chiamate
    void add (String description)throws RemoteException;
    void delete (String cod, String description) throws RemoteException;
    void update(String cod, String description)throws RemoteException;

}
