package application.socket;

import application.Interfaces.ServicesManager;
import application.Interfaces.UserRemote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;

//FONDAMENTALE EQUIVALENTE DEL RMIMANAGER

public class SocketManager implements ServicesManager{


    public  void SocketAccept(){
            Socket s = null;
          try {
                s = new Socket("localhost", 1092);
                System.out.println("connection Established");
                SocketThread st = new SocketThread(s); // crea un thread per la gestione dei vari client
                st.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Connection Error");

            }

    }
    @Override
    public UserRemote getUserService() throws Exception {
        return (UserRemote) new BufferedReader(new InputStreamReader(System.in));

    }

    //dovrebbe essere chiamata come la user manager da services manager come nel main controller simile a rmi chiamando la socket manager

    }


