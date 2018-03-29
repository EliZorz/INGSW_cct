package application.rmi.server;

import application.Interfaces.UserRemote;
import application.socket.SocketThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Remote;
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
            UserRemote pippo = new ServerImpl();  //pippo è server (impl)

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Inter", pippo );
            System.out.println("Server set up completely.");
            /*
            Registry reg = LocateRegistry.getRegistry(1099);
            reg.rebind("ServerObject", (Remote) new Server());
            System.out.println("Server already installed.");
            */

        } catch (RemoteException e){

            e.printStackTrace();
        }


       /* IN UN ALTRO SERVER X SOCK
            try {
            ServerSocket sersock = new ServerSocket(1099);
            System.out.println("Server socket listening.");

            while(true){
                Socket sock = sersock.accept();
                System.out.println("Server socket connected");
                new Thread(new MultithServerSocket(sock)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        ServerSocket ss2 = null;

        try {
            ss2 = new ServerSocket(1092);
            System.out.println("Server connection ready");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }


    }


}
