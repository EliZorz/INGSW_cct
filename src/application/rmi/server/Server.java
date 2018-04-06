package application.rmi.server;

import application.Interfaces.UserRemote;
import application.socket.server.ServerSocketListener;


import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by ELISA on 15/03/2018.
 */
public class Server {

    public Server() throws RemoteException {}

    public static void main(String[] args) throws RemoteException {

        try{


            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Inter", new ServerImpl() );
            System.out.println("Server set up completely.");




        } catch (RemoteException e){

            e.printStackTrace();
        }


        ServerSocket ss2 = null;

        try {
            ss2 = new ServerSocket(1092);

            ServerSocketListener ss = new ServerSocketListener(ss2);  //creo il server listener che si occupa dell'accettare i client e del gestirli come thread
            System.out.println("Server connection ready");




        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }


    }


}
