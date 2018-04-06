package application.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//quello corretto
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
                System.out.println("connection Established");
                SocketThread st = new SocketThread(s); // crea un thread per la gestione dei vari client
                st.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");
                break;
            }
        }

        System.out.println("Putting down socket connection");
    }
}