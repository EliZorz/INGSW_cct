package application;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by ELISA on 15/03/2018.
 */
public class Server extends UnicastRemoteObject implements Inter {

    @Override
    public String sendMessage(String clientMessage) {
        return "Client Message".equals(clientMessage) ? "Server Message" : null;
    }

    public static void main(String[] args) throws RemoteException {

        try{
            Inter pippo = (Inter) new Server();  //pippo è server (impl)

            //Inter sun = (Inter) UnicastRemoteObject.exportObject((Inter) pippo, 5723);  //sun è stub
            //bind
            Registry registry = LocateRegistry.createRegistry(1099);


            registry.rebind("Inter", new Server());
            System.out.println("Server set up completely.");

        } catch (RemoteException e){
            Registry reg = LocateRegistry.getRegistry(1099);
            reg.rebind("ServerObject", new Server());
            System.out.println("Server already installed.");
            e.printStackTrace();
        }
    }

    public Server() throws RemoteException {}


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
