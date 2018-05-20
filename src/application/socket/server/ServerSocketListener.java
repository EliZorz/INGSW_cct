package application.socket.server;

import application.rmi.server.ServerImpl;
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
        // client socket
        Socket s;

        while (true) {
            try {
                s = serverSocket.accept();  //gestione i vari client
                System.out.println("Connection Established from Server Socket Listener");
                SocketThread st = new SocketThread(s, new ServerImpl()); // crea un thread per la gestione dei vari client
                st.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");
                break;
            }
        }

        System.out.println("Putting down socket connection");  //quando termina la connessione col client per qualche motivo
    }
}