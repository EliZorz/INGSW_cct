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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


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
    public boolean addData(String name, String surname, String cf, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy,
                           String nameContact, String surnameContact, String cfContact, String mailContact, String telContact, LocalDate birthdayContact, String bornWhereContact, String addressContact, String capContact, String provinceContact,
                           boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
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
    public boolean addContact (ArrayList<String> selectedChild, String surname, String name, String cf, String mail, String tel, LocalDate birthday, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        return true;
    }

    @Override
    public boolean deleteContact (String oldcfContact) throws RemoteException{
        return true;
    }

    @Override
    public boolean updateContact(String name, String surname, String oldcf, String cf, String mail, String tel, LocalDate bornOn, String bornWhere, String address, String cap, String province, int isDoc, int isGuardian, int isContact) throws RemoteException{
        return true;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<ContactsDbDetails> loadDataContacts(String cfChild) throws RemoteException {
        return null;
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
    public boolean updateStaff(String name, String surname, String oldcf, String cf, String mail, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException{
        return true;
    }

    @Override
    public ArrayList<SupplierDbDetails> loadDataSuppliers() throws RemoteException {
        return null;
    }

    @Override
    public boolean addDataSupplier(String name, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        return false;
    }

    @Override
    public boolean updateSupplier(String name, String oldPiva, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteSupplier(String piva) throws RemoteException {
        return false;
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
            System.out.println("OMG ERROR DURING LISTENING");
        }

         if(responce.equals("Ok"))
            return true;
        return false;
    }

    @Override
    public ArrayList<TripTableDbDetails> loadDataTrip() throws RemoteException {
        return null;
    }

    @Override
    public boolean deleteTrip(String dep, LocalDateTime dateDep, LocalDateTime dateCom, String alloggio, LocalDateTime dateArr, String arr) throws RemoteException {
        return false;
    }


}