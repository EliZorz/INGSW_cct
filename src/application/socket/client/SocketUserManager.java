package application.socket.client;

import application.Interfaces.UserRemote;
import application.details.*;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class SocketUserManager implements UserRemote {
    private final Socket socket;  //socket del client
    private BufferedReader in;
    private PrintWriter out;


    public SocketUserManager(Socket s) {
        this.socket = s;
        try{
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out =new PrintWriter(s.getOutputStream());

        }catch(IOException e){
            System.out.println("IO error in server thread");
        }

    }

    @Override
    public boolean funzLog(String usr, String pwd) throws RemoteException {

        String responce = null;
        System.out.println("sending : " + usr +" " + pwd);
        out.println("login "+ usr +" " + pwd);
        System.out.println(usr +" " + pwd);
        out.flush();
        try{
            responce = in.readLine();  //il client si mette in ascolto aspettando che il server dica se sono loggato o meno
        }catch (Exception e){
            System.out.println("Errore durante l'ascolto");
            e.printStackTrace();
        }

        System.out.println("recieved : " + responce);

        if (responce.equals("ok"))
            return true;  //da capire a chi
        else
            return false;
    }


    @Override
    public ArrayList<ChildDbDetails> loadData() throws RemoteException {
        return null;
    }

    @Override
    public boolean addData(String name, String surname, String cf, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        return true;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException {
        return null;
    }


    @Override
    public ArrayList<DishesDbDetails> loadMenu() throws RemoteException {
        ArrayList<DishesDbDetails> dish = new ArrayList<>(1);
        String responce = null ;
        DishesDbDetails dMenu;
        System.out.println("sending a message to open menu");
        out.println("loadmenu");
        out.flush();

        try{
            responce = in.readLine();


        }catch(Exception e){
            System.out.println("Errore durante l'ascolto");
            e.printStackTrace();
        }

        if(responce != null){
            String[] date = responce.split("\\s");
            dMenu = new DishesDbDetails(date[0], date[1], date[2], date[3], date[4],date[5],date[6]);
            dish.add(dMenu);
            return dish;
        }
        return null;
    }

    @Override
    public boolean addMenu(String num, String entree, String mainCourse, String dessert, String sideDish, String drink, LocalDate date) throws RemoteException {
        return false;
    }


}