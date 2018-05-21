package application.socket.server;

import application.rmi.server.ServerImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//fondamentale!!! è il server che si occupa dell'accettare i diversi client
public class ServerSocketListener extends Thread{

    private final ServerSocket serverSocket;

    public ServerSocketListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.start();

    }



    @Override
    public void run(){
        Socket serverClient;
        int  counter = 0;
        try {
            while (true) {
                counter ++;
                serverClient = serverSocket.accept();  //gestione i vari client
                System.out.println("Connection established from Server Socket Listener with client # '"+ counter +"'");
                SocketThread st = new SocketThread(serverClient, new ServerImpl(), counter); // crea un thread per la gestione dei vari client
                st.start();
            }
        } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

        }

        System.out.println("Putting down socket connection");  //quando termina la connessione col client per qualche motivo

    }


}