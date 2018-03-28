package application.socket;

import java.io.IOException;
import java.net.Socket;
//quello corretto
public class ServerSocket {
    public static void main(String args[]) {


        Socket s = null;
        java.net.ServerSocket ss2 = null;

        try {
            ss2 = new java.net.ServerSocket(4445); // creo un SocketThread con porta d'ascolto 4445
            System.out.println("Server connection ready");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        while (true) {
            try {
                s = ss2.accept();  //gestione i vari client
                System.out.println("connection Established");
                SocketThread st = new SocketThread(s); // crea un thread per la gestione dei vari client
                st.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }
    }
}