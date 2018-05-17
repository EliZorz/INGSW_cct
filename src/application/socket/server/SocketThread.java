package application.socket.server;

import application.details.DishesDbDetails;
import application.details.IngredientsDbDetails;
import application.rmi.server.ServerImpl;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//quello corretto
public class SocketThread extends Thread {

    private String line = null;
    private BufferedReader in = null;  //uso questo e non Scanner perché è synchronized
    private PrintWriter out = null;
    private Socket s = null;
    private ObjectOutputStream outputToClient;
    private ObjectInputStream inputFromClient;
    private ServerImpl impl;  //mi serve per poter chiamare le funzioni  ESSENZIALE!!!!!

    //devo considerare che per chiamare  i metodi devo mandare dei messaggi

    public SocketThread(Socket s, ServerImpl impl) throws RemoteException {
        this.impl = impl;
        this.s = s;

    }

    @Override
    public void run() {
        try {
            //creo in e out per la comunicazione con il client
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            out = new PrintWriter(s.getOutputStream());
            outputToClient = new ObjectOutputStream(s.getOutputStream());
            inputFromClient = new ObjectInputStream(s.getInputStream());

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {

            while (true) {
                System.out.println("Ready to recieve a message");
                line = inputFromClient.readUTF();

                System.out.println("recieved " + line);

                boolean responce = doAction(line);  //passo il messaggio al doAction che decide cosa fare

                System.out.println("sending back : " + responce);
                outputToClient.writeBoolean(responce);
                outputToClient.flush();
            }


        } catch (IOException e) {

            line = this.getName();  //salva in line il nome del thread che sta eseguendo
            System.out.println("IO Error/ Client " + line + " terminated abruptly");
        } catch (NullPointerException e) {
            line = this.getName();
            System.out.println("Client " + line + " Closed");  //stampa nel server che tale client è chiuso
        } finally {
            try {
                System.out.println("Connection Closing..");
                if (in != null) {
                    in.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if (out != null) {
                    out.close();
                    System.out.println("Socket Out Closed");
                }
                if (s != null) {
                    s.close();  //fa terminare la accept di quel solo client
                    //System.out.println(s.isClosed()); //serve per controllare che la socket sia chiusa
                    System.out.println("Socket Closed");
                }

            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }
    }

    private boolean doAction(String line) throws IOException {
        if(line.equals("login")){
            System.out.println("Logging in...");
            String usr = inputFromClient.readUTF();
            String pwd = inputFromClient.readUTF();
            boolean isLogged = impl.funzLog(usr, pwd);
            outputToClient.writeBoolean(isLogged);
            return isLogged;
        }else if(line.equals("loadmenu")){
            System.out.println("Loading menu...");
            outputToClient.writeObject(impl.loadMenu());
            return true;
        }else if(line.equals("deleteMenu")){
            System.out.println("Deleting menu...");
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            Boolean isDeleted = impl.deleteMenu(date);
            outputToClient.writeBoolean(isDeleted);
            return isDeleted;
        }
        return false;

    }
}
