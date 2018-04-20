package application.socket.client;

import application.Interfaces.UserRemote;
import application.details.*;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

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
        ArrayList<ChildDbDetails> child = new ArrayList<>(1);
        String responce = null;
        ChildDbDetails dChild;
        System.out.println("sending a message to load the children");
        out.println("loadchild");
        out.flush();
        try{
            responce = in.readLine();
        }catch (Exception e){
            System.out.println("Problema durante l'ascolto");
            e.printStackTrace();
        }

        if(responce != null){
            String[] children = responce.split("\\*");
            dChild = new ChildDbDetails(children[0], children[1],children[2],children[3],children[4],children[5],children[6],children[7],children[8]);
            child.add(dChild);
            return child;
        }
        return null;
    }

    @Override
    public boolean addData(String name, String surname, String cf, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        String responce = null;
        String when = birthday.format(DateTimeFormatter.BASIC_ISO_DATE);
        String what = "addData*"+ name + "*" + surname +"*" + cf + "*" + when+"*"+ bornWhere +"*" + residence +"*" + address+"*"+ cap +"*"+ province+"*"+selectedAllergy;
        System.out.println("Sending the new menu to database....");
        out.println(what);
        out.flush();
        try{
            responce = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore durante l'ascolto");
        }

        if(responce.equals("Ok"))
            return true;
        return false;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException {
        ArrayList<IngredientsDbDetails> ingr = new ArrayList<>(1);
        String responce = null;
        IngredientsDbDetails dIngr;
        System.out.println("sending a message to load the ingredients");
        out.println("loadIngr");
        out.flush();
        try{
            in.readLine();
        } catch (IOException e) {
            System.out.println("problema nella lettura del messaggio");
            e.printStackTrace();
        }
        if(responce != null){

            dIngr = new IngredientsDbDetails(responce); // perché passa solo una parola se fossero più parole bisogna modificarlo
            ingr.add(dIngr);
            return ingr;
        }
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
    public ArrayList<ContactsDbDetails> loadDataContacts() throws RemoteException {
        return null;
    }

    @Override
    public boolean addContact(String surname, String name, String cf, String mail, String tel, LocalDate birthday, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteChild(String cf) throws RemoteException {
        return false;
    }

    @Override
    public ArrayList<StaffDbDetails> loadDataStaff() throws RemoteException {
        return null;
    }

    @Override
    public boolean addDataStaff(String name, String surname, String cf, String mail, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        return false;
    }

    @Override
    public boolean addMenu(String num, String entree, String mainCourse, String dessert, String sideDish, String drink, LocalDate date) throws RemoteException {
        String responce = null;
        String when = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        String what = "addMenu "+ num + " " + entree +" " + mainCourse + " " + dessert+" "+ sideDish +" " + drink +" " + when;
        System.out.println("Sending the new menu to database....");
        out.println(what);
        out.flush();
        try{
            responce = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore durante l'ascolto");
        }

         if(responce.equals("Ok"))
            return true;
        return false;
    }


}