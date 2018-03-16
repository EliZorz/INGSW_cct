package application;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;

public class Server extends UnicastRemoteObject implements Inter{  //apre la connessione
    public Server() throws Exception {}
    public static void main(String args[]) {
        try {
            // Instantiating the implementation class
            Inter pippo = (Inter) new Server();  //crea una nuova implementazione dell'interfaccia

            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
           // Inter sun = (Inter) UnicastRemoteObject.exportObject(pippo, 5723); //la nostra porta è 5723

            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(5723);

            registry.rebind("Inter", new Server());
            System.out.println("Server ready");
        } catch (Exception e) {
            System.out.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void add(String description) {

    }

    @Override
    public void delete(String cod, String description) {

    }

    @Override
    public void update(String cod, String description) {

    }
}
