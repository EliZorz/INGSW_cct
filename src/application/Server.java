package application;

import application.rmi.server.ServerImpl;
import application.socket.server.SocketThread;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by ELISA on 15/03/2018.
 */
public class Server {

    public Server() throws RemoteException {}

    public static void main(String[] args) throws RemoteException {

        //RMI ---------------------------------------
        try{
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Inter", new ServerImpl() );
            System.out.println("Server RMI set up completely.");
        } catch (RemoteException e){
            e.printStackTrace();
        }


        //SOCKET ---------------------------------
        /*use N+1 threads for this : one for the ServerSocket, to avoid blocking the whole application waiting for a client to connect;
        and N threads to process the client's requests, N being the size of the thread pool
         */

        ServerSocket serverSocket = null;
        Socket socket = null;
        int counter = 0;

        try {
            serverSocket = new ServerSocket(1092);
            System.out.println("Server SOCKET listening...");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        while(true){
            counter ++;
            try{
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //new Thread for client
            System.out.println("Creating new socket thread for client number " +counter+" ...");
            new SocketThread(socket, new ServerImpl(), counter).start();  //new thread for client

        }


    }


}
