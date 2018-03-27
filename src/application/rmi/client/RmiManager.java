package application.rmi.client;

import application.Interfaces.ServicesManager;
import application.Interfaces.UserRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by ELISA on 27/03/2018.
 */
public class RmiManager implements ServicesManager {

    Registry registry = null;

    public RmiManager(){
        //LOOKUP
        try {
            registry = LocateRegistry.getRegistry();

            //String reply = pippo.sendMessage("Client hello");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public UserRemote getUserService() throws Exception {

            return (UserRemote) registry.lookup("UserRemote");
    }
}
