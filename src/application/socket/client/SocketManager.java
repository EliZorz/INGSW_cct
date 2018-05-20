package application.socket.client;

import application.Interfaces.ServicesManager;
import application.Interfaces.UserRemote;

import java.net.Socket;

//FONDAMENTALE EQUIVALENTE DEL RMIMANAGER

public class SocketManager implements ServicesManager{
    //si occupa della creazione della socket per il client e fa richiesta al server
    private UserRemote user;

    public  SocketManager(){
            Socket s;
          try {
                System.out.println("opening socket");
                s = new Socket("localhost", 1092);
                System.out.println("socket opened");
                System.out.println("connection established from Socket Manager");
                //SocketThread st = new SocketThread(s); // crea un thread per la gestione dei vari client
                //st.start();
                System.out.println("creating the user manager");
                user = new SocketUserManager(s);  //implementazione della user remote per quella socket
                System.out.println("");

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Connection Error");

            }

    }
    @Override
    public UserRemote getUserService() throws Exception {
       return user; //ritorna l'interfaccia remota

    }


}


