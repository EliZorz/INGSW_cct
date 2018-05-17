package application.socket.server;

import application.details.DishesDbDetails;
import application.details.StaffDbDetails;
import application.rmi.server.ServerImpl;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

//quello corretto
public class SocketThread extends Thread {

    private String line = null;
    private ObjectOutputStream outputToClient;
    private ObjectInputStream inputFromClient;
    private Socket s = null;
    private ServerImpl impl;  //mi serve per poter chiamare le funzioni  ESSENZIALE!!!!!

    //devo considerare che per chiamare  i metodi devo mandare dei messaggi

    public SocketThread(Socket s, ServerImpl impl) {
        this.impl = impl;
        this.s = s;
    }

    @Override
    public void run() {
        try {
            //creo in e out per la comunicazione con il client
            outputToClient = new ObjectOutputStream(s.getOutputStream());
            inputFromClient = new ObjectInputStream(s.getInputStream());

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {
            while (true) {
                System.out.println("Ready to receive a message");
                line = inputFromClient.readUTF();   //attende fino a quando arriva un messaggio

                System.out.println("Received " + line);

                boolean responce = doAction(line);  //passo il messaggio al doAction che decide cosa fare
                System.out.println("Sending back : " + responce);

                //outputToClient.writeBoolean(responce);  //manda al socket client la risposta
                //outputToClient.flush();
            }

        } catch (IOException e) {
            line = this.getName();  //salva in line il nome del thread che sta eseguendo
            System.out.println("IO Error/ Client " + line + " terminated abruptly");
        } catch (NullPointerException e) {
            line = this.getName();
            System.out.println("Client " + line + " closed");  //stampa nel server che tale client è chiuso
        } finally {
            try {
                System.out.println("Connection Closing..");
                if (inputFromClient != null) {
                    inputFromClient.close();
                    System.out.println("Socket Input Stream Closed");
                }
                if (outputToClient != null) {
                    outputToClient.close();
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

        //String[] credentials = line.split("\\s+");

        if (line.equals("login")){
            System.out.println("Logging in...");
            String usr = inputFromClient.readUTF();
            String pwd = inputFromClient.readUTF();

            boolean isLogged = impl.funzLog(usr, pwd);

            outputToClient.writeBoolean(isLogged);
            return isLogged;

        }


        //CHILDREN -----------------------------------------------------------------------
        else if(line.equals("loadchildren")) {
            System.out.println("Loading children table");

        } else if(line.equals("addchild")){
            System.out.println("Adding menu");

        } else if(line.equals("deletechild")){
            System.out.println("Deleting child");

        } else if(line.equals("editchild")){
            System.out.println("Editing child");
        }


        //STAFF -------------------------------------------------------------------------
        else if(line.equals("loadstaff")){
            System.out.println("Loading staff");
            ArrayList<StaffDbDetails> staff = impl.loadDataStaff();
            if(staff == null)
                return false;
            else
                outputToClient.writeObject(staff);
            return true;

        } else if(line.equals("addstaff")){
            // 11 par passati :
            // String name, String surname, String cf, String mail, LocalDate birthday, String bornWhere,
            // String residence, String address, String cap, String province, ArrayList<String> selectedAllergy
            // da serverImpl ritorna boolean

            System.out.println("Adding data...");
            String name = inputFromClient.readUTF();
            String surname = inputFromClient.readUTF();
            String cf = inputFromClient.readUTF();
            String mail = inputFromClient.readUTF();
            LocalDate birthday = LocalDate.parse(inputFromClient.readUTF());
            String bornWhere = inputFromClient.readUTF();
            String residence = inputFromClient.readUTF();
            String address = inputFromClient.readUTF();
            String cap = inputFromClient.readUTF();
            String province = inputFromClient.readUTF();
                ArrayList<String> allergy = null;
            try {
                allergy = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            boolean isAdded = impl.addDataStaff(name, surname, cf, mail, birthday, bornWhere, residence, address, cap, province, allergy);

            outputToClient.writeBoolean(isAdded);
            return isAdded;

        } else if(line.equals("deletestaff")){
            System.out.println("Deleting data...");
            String cf = inputFromClient.readUTF();
            boolean isDeleted = impl.deleteStaff(cf);

            outputToClient.writeBoolean(isDeleted);
            return isDeleted;

        } else if(line.equals("editstaff")){
            System.out.println("Adding data...");
            String name = inputFromClient.readUTF();
            String surname = inputFromClient.readUTF();
            String oldcf = inputFromClient.readUTF();
            String cf = inputFromClient.readUTF();
            String mail = inputFromClient.readUTF();
            LocalDate birthday = LocalDate.parse(inputFromClient.readUTF());
            String bornWhere = inputFromClient.readUTF();
            String residence = inputFromClient.readUTF();
            String address = inputFromClient.readUTF();
            String cap = inputFromClient.readUTF();
            String province = inputFromClient.readUTF();
            ArrayList<String> allergy = null;
            try {
                allergy = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            boolean isEdited = impl.updateStaff(name, surname, oldcf, cf, mail, birthday, bornWhere, residence, address, cap, province, allergy);

            outputToClient.writeBoolean(isEdited);
            return isEdited;

        }


        //MENU -------------------------------------------------------------------------------
        else if(line.equals("loadmenu")){
            System.out.println("Loading menu");
            ArrayList<DishesDbDetails> menu = impl.loadMenu();
            if(menu == null)
                return false;
            else
                outputToClient.writeObject(menu);
            return true;

        } else if(line.equals("addMenu")){
            System.out.println("Sending menu to database again");

        }

        return false;
    }
}
