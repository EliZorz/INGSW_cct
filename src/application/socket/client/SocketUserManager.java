package application.socket.client;

import application.Interfaces.UserRemote;
import application.details.*;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Spliterator;


public class SocketUserManager implements UserRemote {
    protected final Socket socket;  //socket del client
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;


    public SocketUserManager(Socket s) {
        this.socket = s;
        try{
            toServer = new ObjectOutputStream(s.getOutputStream());
            toServer.flush();
            fromServer = new ObjectInputStream(s.getInputStream());
            System.out.println("Created streams I/O for socket user manager");
        }catch(IOException e){
            System.out.println("IO error in server thread");
        }

    }


    //LOGIN --------------------------------------------------------------------------------------------------
    @Override
    public boolean funzLog(String usr, String pwd) throws RemoteException {
        boolean isLogged = false;
        try{
            toServer.writeObject("login");
            toServer.flush();
            toServer.writeObject(usr);
            toServer.flush();
            toServer.writeObject(pwd);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            isLogged = fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isLogged;
    }

    //CHILDREN ---------------------------------------------------------------------------
    @Override
    public ArrayList<ChildDbDetails> loadData() throws RemoteException {
        try{
            toServer.writeObject("loadChild");
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return (ArrayList<ChildDbDetails>) fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addData(String name, String surname, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy, String nameContact, String surnameContact, String cfContact, String mailContact, String telContact, LocalDate birthdayContact, String bornWhereContact, String addressContact, String capContact, String provinceContact, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        try{
            toServer.writeObject("addChild");
            toServer.flush();
            toServer.writeObject(name);
            toServer.flush();
            toServer.writeObject(surname);
            toServer.flush();
            toServer.writeObject(cf);
            toServer.flush();
            toServer.writeObject(String.valueOf(bornOn));
            toServer.flush();
            toServer.writeObject(bornWhere);
            toServer.flush();
            toServer.writeObject(residence);
            toServer.flush();
            toServer.writeObject(address);
            toServer.flush();
            toServer.writeObject(cap);
            toServer.flush();
            toServer.writeObject(province);
            toServer.flush();
            toServer.writeObject(selectedAllergy);
            toServer.flush();
            toServer.writeObject(nameContact);
            toServer.flush();
            toServer.writeObject(surnameContact);
            toServer.flush();
            toServer.writeObject(cfContact);
            toServer.flush();
            toServer.writeObject(mailContact);
            toServer.flush();
            toServer.writeObject(telContact);
            toServer.flush();
            toServer.writeObject(String.valueOf(birthdayContact));
            toServer.flush();
            toServer.writeObject(bornWhereContact);
            toServer.flush();
            toServer.writeObject(addressContact);
            toServer.flush();
            toServer.writeObject(capContact);
            toServer.flush();
            toServer.writeObject(provinceContact);
            toServer.flush();
            toServer.writeBoolean(isDoc);
            toServer.flush();
            toServer.writeBoolean(isGuardian);
            toServer.flush();
            toServer.writeBoolean(isContact);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean updateChild(String name, String surname, String oldcf, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException{
        try {
            toServer.writeObject("updateChild");
            toServer.flush();
            toServer.writeObject(name);
            toServer.flush();
            toServer.writeObject(surname);
            toServer.flush();
            toServer.writeObject(oldcf);
            toServer.flush();
            toServer.writeObject(cf);
            toServer.flush();
            toServer.writeObject(String.valueOf(bornOn));
            toServer.flush();
            toServer.writeObject(bornWhere);
            toServer.flush();
            toServer.writeObject(residence);
            toServer.flush();
            toServer.writeObject(address);
            toServer.flush();
            toServer.writeObject(cap);
            toServer.flush();
            toServer.writeObject(province);
            toServer.flush();
            toServer.writeObject(selectedAllergy);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteChild(String cf) throws RemoteException{
        try{
            toServer.writeObject("deleteChild");
            toServer.flush();
            toServer.writeObject(cf);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException {
        try{
            toServer.writeObject("loadIngredients");
            toServer.flush();
            return (ArrayList<IngredientsDbDetails>) fromServer.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    //CONTACT --------------------------------------------------------------------------------
    @Override
    public boolean addContact (ArrayList<String> selectedChild, String surname, String name, String cf, String mail, String tel, LocalDate birthday, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        try{
            toServer.writeObject("addContact");
            toServer.flush();
            toServer.writeObject(selectedChild);
            toServer.flush();
            toServer.writeObject(surname);
            toServer.flush();
            toServer.writeObject(name);
            toServer.flush();
            toServer.writeObject(cf);
            toServer.flush();
            toServer.writeObject(mail);
            toServer.flush();
            toServer.writeObject(tel);
            toServer.flush();
            toServer.writeObject(String.valueOf(birthday));
            toServer.flush();
            toServer.writeObject(bornWhere);
            toServer.flush();
            toServer.writeObject(address);
            toServer.flush();
            toServer.writeObject(cap);
            toServer.flush();
            toServer.writeObject(province);
            toServer.flush();
            toServer.writeBoolean(isDoc);
            toServer.flush();
            toServer.writeBoolean(isGuardian);
            toServer.flush();
            toServer.writeBoolean(isContact);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteContact (String oldcfContact) throws RemoteException{
        try{
            toServer.writeObject("deleteContact");
            toServer.flush();
            toServer.writeObject(oldcfContact);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean updateContact(String name, String surname, String oldcf, String cf, String mail, String tel, LocalDate bornOn, String bornWhere, String address, String cap, String province, int isDoc, int isGuardian, int isContact) throws RemoteException{
        try{
            toServer.writeObject("updateContact");
            toServer.flush();
            toServer.writeObject(name);
            toServer.flush();
            toServer.writeObject(surname);
            toServer.flush();
            toServer.writeObject(oldcf);
            toServer.flush();
            toServer.writeObject(cf);
            toServer.flush();
            toServer.writeObject(mail);
            toServer.flush();
            toServer.writeObject(tel);
            toServer.flush();
            toServer.writeObject(String.valueOf(bornOn));
            toServer.flush();
            toServer.writeObject(bornWhere);
            toServer.flush();
            toServer.writeObject(address);
            toServer.flush();
            toServer.writeObject(cap);
            toServer.flush();
            toServer.writeObject(province);
            toServer.flush();
            toServer.write(isDoc);
            toServer.flush();
            toServer.write(isGuardian);
            toServer.flush();
            toServer.write(isContact);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public ArrayList<ContactsDbDetails> loadDataContacts(String cfChild) throws RemoteException {
        try{
            toServer.writeObject("loadDataContacts");
            toServer.flush();
            toServer.writeObject(cfChild);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return (ArrayList<ContactsDbDetails>) fromServer.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    //STAFF ------------------------------------------------------------------------------------------
    @Override
    public ArrayList<StaffDbDetails> loadDataStaff() throws RemoteException {
        System.out.println("sending a message to open staff");
        try {
            toServer.writeObject("loadDataStaff");
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return (ArrayList<StaffDbDetails>) fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean addDataStaff(String name, String surname, String cf, String mail, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        try{
            toServer.writeObject("addDataStaff");
            toServer.flush();
            toServer.writeObject(name);
            toServer.flush();
            toServer.writeObject(surname);
            toServer.flush();
            toServer.writeObject(cf);
            toServer.flush();
            toServer.writeObject(mail);
            toServer.flush();
            toServer.writeObject(String.valueOf(birthday));
            toServer.flush();
            toServer.writeObject(bornWhere);
            toServer.flush();
            toServer.writeObject(residence);
            toServer.flush();
            toServer.writeObject(address);
            toServer.flush();
            toServer.writeObject(cap);
            toServer.flush();
            toServer.writeObject(province);
            toServer.flush();
            toServer.writeObject(selectedAllergy);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteStaff(String cf) throws RemoteException {
        try{
            toServer.writeObject("deleteStaff");
            toServer.flush();
            toServer.writeObject(cf);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean updateStaff(String name, String surname, String oldcf, String cf, String mail, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException{
        try{
            toServer.writeObject("updateStaff");
            toServer.flush();
            toServer.writeObject(name);
            toServer.flush();
            toServer.writeObject(surname);
            toServer.flush();
            toServer.writeObject(oldcf);
            toServer.flush();
            toServer.writeObject(cf);
            toServer.flush();
            toServer.writeObject(mail);
            toServer.flush();
            toServer.writeObject(String.valueOf(bornOn));
            toServer.flush();
            toServer.writeObject(bornWhere);
            toServer.flush();
            toServer.writeObject(residence);
            toServer.flush();
            toServer.writeObject(address);
            toServer.flush();
            toServer.writeObject(cap);
            toServer.flush();
            toServer.writeObject(province);
            toServer.flush();
            toServer.writeObject(selectedAllergy);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    //SUPPLIERS ------------------------------------------------------------------------
    @Override
    public ArrayList<SupplierDbDetails> loadDataSuppliers() throws RemoteException {
        System.out.println("sending a message to load suppliers");
        try {
            toServer.writeObject("loadSuppliers");
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<SupplierDbDetails>) fromServer.readObject();
        }catch(Exception e){
            System.out.println("OMG ERROR LISTENING");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean addDataSupplier(String name, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        System.out.println("sending a message to add supplier");
        try{
            toServer.writeObject("addSupplier");
            toServer.flush();
            toServer.writeObject(name);
            toServer.flush();
            toServer.writeObject(piva);
            toServer.flush();
            toServer.writeObject(mail);
            toServer.flush();
            toServer.writeObject(tel);
            toServer.flush();
            toServer.writeObject(address);
            toServer.flush();
            toServer.writeObject(cap);
            toServer.flush();
            toServer.writeObject(province);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateSupplier(String name, String oldPiva, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        System.out.println("sending a message to update supplier");
        try{
            toServer.writeObject("updateSupplier");
            toServer.flush();
            toServer.writeObject(name);
            toServer.flush();
            toServer.writeObject(oldPiva);
            toServer.flush();
            toServer.writeObject(piva);
            toServer.flush();
            toServer.writeObject(mail);
            toServer.flush();
            toServer.writeObject(tel);
            toServer.flush();
            toServer.writeObject(address);
            toServer.flush();
            toServer.writeObject(cap);
            toServer.flush();
            toServer.writeObject(province);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteSupplier(String piva, ArrayList<IngredientsDbDetails> ingrNO) throws RemoteException {
        System.out.println("Deleting supplier...");
        try{
            toServer.writeObject("deleteSupplier");
            toServer.flush();
            toServer.writeObject(piva);
            toServer.flush();
            toServer.writeObject(ingrNO);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



    @Override
    public ArrayList<CodRifChildDbDetails> loadDataIngr(String selectedSupplier) throws RemoteException {
        System.out.println("sending a message to load ingredients");
        try {
            toServer.writeObject("loadDataIngr");
            toServer.flush();
            toServer.writeObject(selectedSupplier);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<CodRifChildDbDetails>) fromServer.readObject();
        }catch(Exception e){
            System.out.println("OMG ERROR LISTENING");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean addIngrToDb(String ingr, String selectedSupplier) throws RemoteException {
        System.out.println("Adding ingredient to db...");
        try{
            toServer.writeObject("addIngredientSupplier");
            toServer.flush();
            toServer.writeObject(ingr);
            toServer.flush();
            toServer.writeObject(selectedSupplier);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<DishesDbDetails> loadMenuWithThisSupplier(String selectedSupplier) throws RemoteException {
        System.out.println("Loading menu with the ingredients of this supplier...");
        try{
            toServer.writeObject("loadMenuWithThisSupplier");
            toServer.flush();
            toServer.writeObject(selectedSupplier);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<DishesDbDetails>) fromServer.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadNoIngr(String selectedSupplier) throws RemoteException {
        System.out.println("Loading no available ingredients...");
        try{
            toServer.writeObject("loadNoIngr");
            toServer.flush();
            toServer.writeObject(selectedSupplier);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return (ArrayList<IngredientsDbDetails>) fromServer.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    //COACH OPERATOR -------------------------------------------------------------------

    @Override
    public ArrayList<SupplierDbDetails> loadDataCoachOperator() throws RemoteException {
        return null;
    }

    @Override
    public boolean addDataCoachOperator(String name, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        return false;
    }

    @Override
    public boolean updateCoachOperator(String name, String oldPiva, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteCoachOperator(String piva) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteCoachOperatorBus(String plate) throws RemoteException {
        return false;
    }

    //MENU -------------------------------------------------------------------------------
    @Override
    public DishesDbDetails loadThisMenu(LocalDate date) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<IngredientsDbDetails> searchIngredients(String dish) throws RemoteException {
        try{
            toServer.writeObject("searchIngredients");
            toServer.flush();
            toServer.writeObject(dish);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return (ArrayList<IngredientsDbDetails>) fromServer.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadIngr(LocalDate day) throws RemoteException {
        return null;
    }

    @Override
    public boolean updateMenu(String num, String entree, String main, String dessert, String side, String drink, LocalDate day, LocalDate oldDate) throws RemoteException {
        System.out.println("sendiang a message to update menu");
        try{
            toServer.writeObject("updateMenu");
            toServer.flush();
            toServer.writeObject(num);
            toServer.flush();
            toServer.writeObject(entree);
            toServer.flush();
            toServer.writeObject(main);
            toServer.flush();
            toServer.writeObject(dessert);
            toServer.flush();
            toServer.writeObject(side);
            toServer.flush();
            toServer.writeObject(drink);
            toServer.flush();
            toServer.writeObject(String.valueOf(day));
            toServer.flush();
            toServer.writeObject(String.valueOf(oldDate));
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public ArrayList<DishesDbDetails> loadMenu() throws RemoteException {
        System.out.println("sending a message to open menu");
        try {
            toServer.writeObject("loadmenu");
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<DishesDbDetails>) fromServer.readObject();
        }catch(Exception e){
            System.out.println("OMG ERROR LISTENING");
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public boolean addMenu(String num, String entree, String mainCourse, String dessert, String sideDish, String drink, LocalDate date) throws RemoteException {
        System.out.println("sending a message to add the menu");
        try{
            toServer.writeObject("addMenu");
            toServer.flush();
            toServer.writeObject(num);
            toServer.flush();
            toServer.writeObject(entree);
            toServer.flush();
            toServer.writeObject(mainCourse);
            toServer.flush();
            toServer.writeObject(dessert);
            toServer.flush();
            toServer.writeObject(sideDish);
            toServer.flush();
            toServer.writeObject(drink);
            toServer.flush();
            toServer.writeObject(String.valueOf(date));
            toServer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean controllDate(LocalDate d) throws RemoteException {
        System.out.println("sending a message to controll the date");
        try {
            toServer.writeObject("controllDate");
            toServer.flush();
            toServer.writeObject(String.valueOf(d));
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();

        }catch(Exception e){
            System.out.println("OMG ERROR LISTENING");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteMenu(LocalDate d) throws RemoteException {
        try{
            toServer.writeObject("deleteMenu");
            toServer.flush();
            toServer.writeObject(String.valueOf(d));
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<SpecialDbDetails> loadInterniWithAllergies(LocalDate date) throws RemoteException {
        System.out.println("Loading interni with allergies...");
        try{
            toServer.writeObject("loadAllergicalInterni");
            toServer.flush();
            toServer.writeObject(String.valueOf(date));
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return (ArrayList<SpecialDbDetails>) fromServer.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveIngredients(String dish, ArrayList<String> selectedIngredients) throws RemoteException {
        try{
            toServer.writeObject("saveIngredients");
            toServer.flush();
            toServer.writeObject(dish);
            toServer.flush();
            toServer.writeObject(selectedIngredients);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //SPECIAL MENU ------------------------------------------------------------------------------------
    @Override
    public ArrayList<SpecialMenuDbDetails> loadSpecialMenu() throws RemoteException {
        try{
            toServer.writeObject("loadSpecialMenu");
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<SpecialMenuDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteSpecialMenu(LocalDate date, String FC, String allergies) throws RemoteException {
        try{
            toServer.writeObject("deleteSpecialMenu");
            toServer.flush();
            toServer.writeObject(String.valueOf(date));
            toServer.flush();
            toServer.writeObject(FC);
            toServer.flush();
            toServer.writeObject(allergies);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addSpecialMenu(String entree, String main, String dessert, String side, String drink, LocalDate date, SpecialDbDetails special) throws RemoteException {
        try{
            toServer.writeObject("addSpecialMenu");
            toServer.flush();
            toServer.writeObject(entree);
            toServer.flush();
            toServer.writeObject(main);
            toServer.flush();
            toServer.writeObject(dessert);
            toServer.flush();
            toServer.writeObject(side);
            toServer.flush();
            toServer.writeObject(drink);
            toServer.flush();
            toServer.writeObject(String.valueOf(date));
            toServer.flush();
            toServer.writeObject(special);
            toServer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateSpecialMenu(String entree, String main, String dessert, String side, String drink, LocalDate date, SpecialDbDetails special) throws RemoteException {
        try{
            toServer.writeObject("updateSpecialMenu");
            toServer.flush();
            toServer.writeObject(entree);
            toServer.flush();
            toServer.writeObject(main);
            toServer.flush();
            toServer.writeObject(dessert);
            toServer.flush();
            toServer.writeObject(side);
            toServer.flush();
            toServer.writeObject(drink);
            toServer.flush();
            toServer.writeObject(String.valueOf(date));
            toServer.flush();
            toServer.writeObject(special);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    //TRIP -------------------------------------------------------------------------------
    @Override
    public boolean zeroActualParticipants(String plate) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteIsHere(String plate) throws RemoteException {
        return false;
    }

    @Override
    public void deleteFromGitaHasBus(String plate) throws RemoteException {

    }

    @Override
    public ArrayList<BusPlateCapacityDbDetails> loadDataBus(String selectedSupplier) throws RemoteException {
        return null;
    }

    @Override
    public boolean addBusToDb(String plate, int capacity, String selectedSupplier) throws RemoteException {
        return false;
    }


    @Override
    public ArrayList<TripTableDbDetails> loadDataTrip() throws RemoteException {
        try{
            toServer.writeObject("loadDataTrip");
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<TripTableDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteTrip(String dep, String dateDep, String dateCom, String alloggio, String dateArr, String arr) throws RemoteException {
        System.out.println("Deleting trip...");
        try{
            toServer.writeObject("deleteTrip");
            toServer.flush();
            //String dep, String dateDep, String dateCom, String staying, String dateArr, String arr
            toServer.writeObject(dep);
            toServer.flush();
            toServer.writeObject(dateDep);
            toServer.flush();
            toServer.writeObject(dateCom);
            toServer.flush();
            toServer.writeObject(alloggio);
            toServer.flush();
            toServer.writeObject(dateArr);
            toServer.flush();
            toServer.writeObject(arr);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public ArrayList<ChildTripDbDetails> loadChildTrip() throws RemoteException{
        try{
            toServer.writeObject("loadChildTrip");
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<ChildTripDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<StaffTripDbDetails> loadStaffTrip() throws RemoteException{
        try{
            toServer.writeObject("loadStaffTtrip");
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<StaffTripDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int[] addTrip(ArrayList<String> selectedChild, ArrayList<String> selectedStaff, String timeDep, String timeArr, String timeCom, String departureFrom, String arrivalTo, String staying) throws RemoteException {
        System.out.println("sending a message to add supplier");
        int[] participantsFromServer = new int[2];

        try{
            toServer.writeObject("addSupplier");
            toServer.flush();
            toServer.writeObject(selectedChild);
            toServer.flush();
            toServer.writeObject(selectedStaff);
            toServer.flush();
            toServer.writeObject(timeDep);
            toServer.flush();
            toServer.writeObject(timeArr);
            toServer.flush();
            toServer.writeObject(timeCom);
            toServer.flush();
            toServer.writeObject(departureFrom);
            toServer.flush();
            toServer.writeObject(arrivalTo);
            toServer.flush();
            toServer.writeObject(staying);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            for(int i = 0; i<2 ; i++){
                participantsFromServer[i] = fromServer.readInt();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return participantsFromServer;
    }

    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadTripSelectedChildren (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        try{
            toServer.writeObject("loadTripSelectedChildren");
            toServer.flush();
            toServer.writeObject(selectedDepFrom);
            toServer.flush();
            toServer.writeObject(selectedDep);
            toServer.flush();
            toServer.writeObject(selectedCom);
            toServer.flush();
            toServer.writeObject(selectedAccomodation);
            toServer.flush();
            toServer.writeObject(selectedArr);
            toServer.flush();
            toServer.writeObject(selectedArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<ChildSelectedTripDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<StaffSelectedTripDbDetails> loadTripSelectedStaff (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        try{
            toServer.writeObject("loadTripSelectedStaff");
            toServer.flush();
            toServer.writeObject(selectedDepFrom);
            toServer.flush();
            toServer.writeObject(selectedDep);
            toServer.flush();
            toServer.writeObject(selectedCom);
            toServer.flush();
            toServer.writeObject(selectedAccomodation);
            toServer.flush();
            toServer.writeObject(selectedArr);
            toServer.flush();
            toServer.writeObject(selectedArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<StaffSelectedTripDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> findNotAvailableStaff(ArrayList<String> selectedStaffCf, String selectedTripDep, String selectedTripCom) throws RemoteException {
        try{
            toServer.writeObject("findNotAvailableStaff");
            toServer.flush();
            toServer.writeObject(selectedStaffCf);
            toServer.flush();
            toServer.writeObject(selectedTripDep);
            toServer.flush();
            toServer.writeObject(selectedTripCom);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<CodRifChildDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> findNotAvailableChild(ArrayList<String> selectedChildCf, String selectedTripDep, String selectedTripCom) throws RemoteException {
        try{
            toServer.writeObject("findNotAvailableChild");
            toServer.flush();
            toServer.writeObject(selectedChildCf);
            toServer.flush();
            toServer.writeObject(selectedTripDep);
            toServer.flush();
            toServer.writeObject(selectedTripCom);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<CodRifChildDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int[] howManyActualParticipants(ArrayList<String> selectedChildCf, ArrayList<String> selectedStaffCf) throws RemoteException {
        int[] participants = new int[2];

        try{
            toServer.writeObject("howManyActualParticipants");
            toServer.flush();
            toServer.writeObject(selectedChildCf);
            toServer.flush();
            toServer.writeObject(selectedStaffCf);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            for(int i = 0; i<2 ; i++){
                participants[i] = fromServer.readInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return participants;
    }

    @Override
    public HashMap<String, ArrayList<String>> associateBusToParticipants(ArrayList<String> selectedChildCfArrayList, int totChildren, ArrayList<String> selectedStaffCfArrayList, int totStaff, String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        try{
            toServer.writeObject("associateBusToParticipants");
            toServer.flush();
            toServer.writeObject(selectedChildCfArrayList);
            toServer.writeInt(totChildren);
            toServer.flush();
            toServer.writeObject(selectedStaffCfArrayList);
            toServer.flush();
            toServer.writeInt(totStaff);
            toServer.flush();
            toServer.writeObject(selectedDepFrom);
            toServer.flush();
            toServer.writeObject(selectedDep);
            toServer.flush();
            toServer.writeObject(selectedCom);
            toServer.flush();
            toServer.writeObject(selectedAccomodation);
            toServer.flush();
            toServer.writeObject(selectedArr);
            toServer.flush();
            toServer.writeObject(selectedArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return (HashMap<String, ArrayList<String>>) fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadWhoTrip(String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        try{
            toServer.writeObject("loadWhoTrip");
            toServer.flush();
            toServer.writeObject(selectedDepFrom);
            toServer.flush();
            toServer.writeObject(selectedDep);
            toServer.flush();
            toServer.writeObject(selectedCom);
            toServer.flush();
            toServer.writeObject(selectedAccomodation);
            toServer.flush();
            toServer.writeObject(selectedArr);
            toServer.flush();
            toServer.writeObject(selectedArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<ChildSelectedTripDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> loadBusTrip(String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        try{
            toServer.writeObject("loadBusTrip");
            toServer.flush();
            toServer.writeObject(selectedDepFrom);
            toServer.flush();
            toServer.writeObject(selectedDep);
            toServer.flush();
            toServer.writeObject(selectedCom);
            toServer.flush();
            toServer.writeObject(selectedAccomodation);
            toServer.flush();
            toServer.writeObject(selectedArr);
            toServer.flush();
            toServer.writeObject(selectedArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<CodRifChildDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<SolutionDbDetails> loadSolution(String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        try{
            toServer.writeObject("loadSolution");
            toServer.flush();
            toServer.writeObject(selectedTripDepFrom);
            toServer.flush();
            toServer.writeObject(selectedTripDep);
            toServer.flush();
            toServer.writeObject(selectedTripCom);
            toServer.flush();
            toServer.writeObject(selectedTripAccomodation);
            toServer.flush();
            toServer.writeObject(selectedTripArr);
            toServer.flush();
            toServer.writeObject(selectedTripArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<SolutionDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> findParticipantOnWrongBus(ArrayList<String> selectedChildCfArrayList, String selectedBus, String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        try{
            toServer.writeObject("findParticipantOnWrongBus");
            toServer.flush();
            toServer.writeObject(selectedChildCfArrayList);
            toServer.flush();
            toServer.writeObject(selectedBus);
            toServer.flush();
            toServer.writeObject(selectedTripDepFrom);
            toServer.flush();
            toServer.writeObject(selectedTripDep);
            toServer.flush();
            toServer.writeObject(selectedTripCom);
            toServer.flush();
            toServer.writeObject(selectedTripAccomodation);
            toServer.flush();
            toServer.writeObject(selectedTripArr);
            toServer.flush();
            toServer.writeObject(selectedTripArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<String>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> findMissingParticipantsOnThisBus(ArrayList<String> peopleOnWrongBus, ArrayList<String> selectedChildCfArrayList, String selectedBus, String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        try{
            toServer.writeObject("findMissingParticipantsOnThisBus");
            toServer.flush();
            toServer.writeObject(peopleOnWrongBus);
            toServer.flush();
            toServer.writeObject(selectedChildCfArrayList);
            toServer.flush();
            toServer.writeObject(selectedBus);
            toServer.flush();
            toServer.writeObject(selectedTripDepFrom);
            toServer.flush();
            toServer.writeObject(selectedTripDep);
            toServer.flush();
            toServer.writeObject(selectedTripCom);
            toServer.flush();
            toServer.writeObject(selectedTripAccomodation);
            toServer.flush();
            toServer.writeObject(selectedTripArr);
            toServer.flush();
            toServer.writeObject(selectedTripArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<String>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void makeIsHereFalse(String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        try{
            toServer.writeObject("makeIsHereFalse");
            toServer.flush();
            toServer.writeObject(selectedTripDepFrom);
            toServer.flush();
            toServer.writeObject(selectedTripDep);
            toServer.flush();
            toServer.writeObject(selectedTripCom);
            toServer.flush();
            toServer.writeObject(selectedTripAccomodation);
            toServer.flush();
            toServer.writeObject(selectedTripArr);
            toServer.flush();
            toServer.writeObject(selectedTripArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void makeIsHereTrue(String selectedBus, String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        try{
            toServer.writeObject("makeIsHereTrue");
            toServer.flush();
            toServer.writeObject(selectedBus);
            toServer.flush();
            toServer.writeObject(selectedTripDepFrom);
            toServer.flush();
            toServer.writeObject(selectedTripDep);
            toServer.flush();
            toServer.writeObject(selectedTripCom);
            toServer.flush();
            toServer.writeObject(selectedTripAccomodation);
            toServer.flush();
            toServer.writeObject(selectedTripArr);
            toServer.flush();
            toServer.writeObject(selectedTripArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadMissing(ArrayList<String> missingArrayList, String selectedBus, String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        try{
            toServer.writeObject("loadMissing");
            toServer.flush();
            toServer.writeObject(missingArrayList);
            toServer.flush();
            toServer.writeObject(selectedBus);
            toServer.flush();
            toServer.writeObject(selectedTripDepFrom);
            toServer.flush();
            toServer.writeObject(selectedTripDep);
            toServer.flush();
            toServer.writeObject(selectedTripCom);
            toServer.flush();
            toServer.writeObject(selectedTripAccomodation);
            toServer.flush();
            toServer.writeObject(selectedTripArr);
            toServer.flush();
            toServer.writeObject(selectedTripArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            return (ArrayList<ChildSelectedTripDbDetails>)fromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}