package application.socket.client;

import application.Interfaces.UserRemote;
import application.details.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public ArrayList<String> loadIngr(LocalDate day) throws RemoteException {
        return null;
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

        System.out.println("received : " + responce);

        if (responce.equals("ok"))
            return true;  //da capire a chi
        else
            return false;
    }


    @Override
    public ArrayList<ChildDbDetails> loadData() throws RemoteException {
        ArrayList<ChildDbDetails> child = new ArrayList<>(1);
        ArrayList<IngredientsDbDetails> allergy = new ArrayList<>(1);
        String responce = null ;
        ChildDbDetails dChild;
        IngredientsDbDetails dAllergy;
        System.out.println("sending a message to open table of children + table of allergies");
        out.println("loadchildren");
        out.flush();

        try{
            responce = in.readLine();


        }catch(Exception e){
            System.out.println("OMG ERROR LISTENING");
            e.printStackTrace();
        }

        if(responce != null){
            String[] date = responce.split("\\s");
            dChild = new ChildDbDetails(date[0], date[1], date[2], date[3], date[4],date[5],date[6], date[7], date[8]);
            dAllergy = new IngredientsDbDetails(date[9]);
            child.add(dChild);
            allergy.add(dAllergy);

            return child;
        }

        return null;
    }

    @Override
    public boolean addData(String name, String surname, String cf, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        String responce = null;
        String bornOn = birthday.format(DateTimeFormatter.BASIC_ISO_DATE);
        String what = "addChild "+ name + " " + surname +" "+ cf +" "+ bornOn +" "+ bornWhere +" "+ residence +" "+ address +" "+ cap +" "+ province+" "+ selectedAllergy.get(0);
        System.out.println("Sending the new child to database....");
        out.println(what);
        out.flush();
        try{
            responce = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("OMG ERROR LISTENING");
        }

        if(responce.equals("Ok"))
            return true;
        return false;
    }

    @Override
    public boolean updateChild(String name, String surname, String oldcf, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException{
        return true;
    }


    @Override
    public ArrayList<ContactsDbDetails> loadDataContacts() throws RemoteException {
        return null;
    }

    @Override
    public boolean addContact (String surname, String name, String cf, String mail, String tel, LocalDate birthday, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        return true;
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
    public boolean updateMenu(String num, String entree, String main, String dessert, String side, String drink, LocalDate day, ArrayList<String> selectedIngredients, LocalDate oldDate) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteChild(String cf) throws RemoteException{
        return true;
    }

    @Override
    public ArrayList<StaffDbDetails> loadDataStaff() throws RemoteException{
        ArrayList<StaffDbDetails> staff = new ArrayList<>(1);
        ArrayList<IngredientsDbDetails> allergy = new ArrayList<>(1);

        String responce = null ;

        StaffDbDetails dStaff;
        IngredientsDbDetails dAllergy;

        System.out.println("sending a message to open table of staff + table of allergies");
        out.println("loadstaff");
        out.flush();

        try{
            responce = in.readLine();


        }catch(Exception e){
            System.out.println("OMG ERROR LISTENING");
            e.printStackTrace();
        }

        if(responce != null){
            String[] date = responce.split("\\s");
            dStaff = new StaffDbDetails(date[0], date[1], date[2], date[3], date[4],date[5],date[6], date[7], date[8], date[9]);
            dAllergy = new IngredientsDbDetails(date[10]);
            staff.add(dStaff);
            allergy.add(dAllergy);

            return staff;
        }

        return null;
    }

    @Override
    public boolean addDataStaff(String name, String surname, String cf, String mail, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        return true;
    }

    @Override
    public boolean deleteStaff(String cf) throws RemoteException {
        return true;
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
            System.out.println("OMG ERROR LISTENING");
            e.printStackTrace();
        }

        if(responce != null){
            String[] date = responce.split("\\s");
            dMenu = new DishesDbDetails(date[0], date[1], date[2], date[3], date[4], date[5], date[6]);
            dish.add(dMenu);

            return dish;
        }
        return null;
    }



    @Override
    public boolean addMenu(String num, String entree, String mainCourse, String dessert, String sideDish, String drink, LocalDate date,ArrayList<String> selectedIngredients) throws RemoteException {
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
            System.out.println("OMG ERROR DURING LISTENING");
        }

         if(responce.equals("Ok"))
            return true;
        return false;
    }

    @Override
    public boolean controllDate(LocalDate d) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteMenu(LocalDate d) throws RemoteException {
        return false;
    }




}