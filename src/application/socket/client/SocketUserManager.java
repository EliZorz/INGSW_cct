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
            fromServer = new ObjectInputStream(s.getInputStream());
        }catch(IOException e){
            System.out.println("IO error in server thread");
        }

    }


    //LOGIN --------------------------------------------------------------------------------------------------
    @Override
    public boolean funzLog(String usr, String pwd) throws RemoteException {
        boolean isLogged = false;
        try{
            toServer.writeUTF("login");
            toServer.flush();
            toServer.writeUTF(usr);
            toServer.flush();
            toServer.writeUTF(pwd);
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
        return null;
    }

    @Override
    public boolean addData(String name, String surname, String cf, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy,
                           String nameContact, String surnameContact, String cfContact, String mailContact, String telContact, LocalDate birthdayContact, String bornWhereContact, String addressContact, String capContact, String provinceContact,
                           boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        return true;
    }

    @Override
    public boolean updateChild(String name, String surname, String oldcf, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException{
        return true;
    }

    @Override
    public boolean deleteChild(String cf) throws RemoteException{
        return true;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException {
        try{
            toServer.writeUTF("loadIngredients");
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
    public ArrayList<ContactsDbDetails> loadDataContacts(String cfChild) throws RemoteException {
        return null;
    }


    //STAFF ------------------------------------------------------------------------------------------
    @Override
    public ArrayList<StaffDbDetails> loadDataStaff() throws RemoteException{
        ArrayList<StaffDbDetails> staff;
        System.out.println("sending a message to open staff");
        try {
            toServer.writeUTF("loadstaff");
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            staff = (ArrayList<StaffDbDetails>) fromServer.readObject();
            return staff;
        }catch(Exception e){
            System.out.println("OMG ERROR LISTENING");
            e.printStackTrace();
        }
        //MEGLIO NON SALVARE IN UNA VARIABILE ELEMENTI DI TIPO Stream

        return null;
    }

    @Override
    public boolean addDataStaff(String name, String surname, String cf, String mail, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        boolean isAdded = false;
        try{
            toServer.writeUTF("addstaff");
            toServer.flush();
            toServer.writeUTF(name);
            toServer.flush();
            toServer.writeUTF(surname);
            toServer.flush();
            toServer.writeUTF(cf);
            toServer.flush();
            toServer.writeUTF(mail);
            toServer.flush();
            toServer.writeUTF(String.valueOf(birthday));
            toServer.flush();
            toServer.writeUTF(bornWhere);
            toServer.flush();
            toServer.writeUTF(residence);
            toServer.flush();
            toServer.writeUTF(address);
            toServer.flush();
            toServer.writeUTF(cap);
            toServer.flush();
            toServer.writeUTF(province);
            toServer.flush();
            toServer.writeObject(selectedAllergy);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            isAdded = fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isAdded;
    }

    @Override
    public boolean deleteStaff(String cf) throws RemoteException {
        boolean isDeleted = false;
        try{
            toServer.writeUTF("deletestaff");
            toServer.flush();
            toServer.writeUTF(cf);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            isDeleted = fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    @Override
    public boolean updateStaff(String name, String surname, String oldcf, String cf, String mail, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException{
        boolean isEdited = false;
        try{
            toServer.writeUTF("updatestaff");
            toServer.flush();
            toServer.writeUTF(name);
            toServer.flush();
            toServer.writeUTF(surname);
            toServer.flush();
            toServer.writeUTF(oldcf);
            toServer.flush();
            toServer.writeUTF(cf);
            toServer.flush();
            toServer.writeUTF(mail);
            toServer.flush();
            toServer.writeUTF(String.valueOf(bornOn));
            toServer.flush();
            toServer.writeUTF(bornWhere);
            toServer.flush();
            toServer.writeUTF(residence);
            toServer.flush();
            toServer.writeUTF(address);
            toServer.flush();
            toServer.writeUTF(cap);
            toServer.flush();
            toServer.writeUTF(province);
            toServer.flush();
            toServer.writeObject(selectedAllergy);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            isEdited = fromServer.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isEdited;
    }

    //SUPPLIERS ------------------------------------------------------------------------
    @Override
    public ArrayList<SupplierDbDetails> loadDataSuppliers() throws RemoteException {
        System.out.println("sending a message to load suppliers");
        try {
            toServer.writeUTF("loadSuppliers");
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
            toServer.writeUTF("addSupplier");
            toServer.flush();
            toServer.writeUTF(name);
            toServer.flush();
            toServer.writeUTF(piva);
            toServer.flush();
            toServer.writeUTF(mail);
            toServer.flush();
            toServer.writeUTF(tel);
            toServer.flush();
            toServer.writeUTF(address);
            toServer.flush();
            toServer.writeUTF(cap);
            toServer.flush();
            toServer.writeUTF(province);
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
            toServer.writeUTF("updateSupplier");
            toServer.flush();
            toServer.writeUTF(name);
            toServer.flush();
            toServer.writeUTF(oldPiva);
            toServer.flush();
            toServer.writeUTF(piva);
            toServer.flush();
            toServer.writeUTF(mail);
            toServer.flush();
            toServer.writeUTF(tel);
            toServer.flush();
            toServer.writeUTF(address);
            toServer.flush();
            toServer.writeUTF(cap);
            toServer.flush();
            toServer.writeUTF(province);
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
            toServer.writeUTF("deleteSupplier");
            toServer.flush();
            toServer.writeUTF(piva);
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
            toServer.writeUTF("loadDataIngr");
            toServer.flush();
            toServer.writeUTF(selectedSupplier);
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
            toServer.writeUTF("addIngredientSupplier");
            toServer.flush();
            toServer.writeUTF(ingr);
            toServer.flush();
            toServer.writeUTF(selectedSupplier);
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
            toServer.writeUTF("loadMenuWithThisSupplier");
            toServer.flush();
            toServer.writeUTF(selectedSupplier);
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
            toServer.writeUTF("loadNoIngr");
            toServer.flush();
            toServer.writeUTF(selectedSupplier);
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
            toServer.writeUTF("searchIngredients");
            toServer.flush();
            toServer.writeUTF(dish);
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
            toServer.writeUTF("updateMenu");
            toServer.flush();
            toServer.writeUTF(num);
            toServer.flush();
            toServer.writeUTF(entree);
            toServer.flush();
            toServer.writeUTF(main);
            toServer.flush();
            toServer.writeUTF(dessert);
            toServer.flush();
            toServer.writeUTF(side);
            toServer.flush();
            toServer.writeUTF(drink);
            toServer.flush();
            toServer.writeUTF(String.valueOf(day));
            toServer.flush();
            toServer.writeUTF(String.valueOf(oldDate));
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
            toServer.writeUTF("loadmenu");
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
            toServer.writeUTF("addMenu");
            toServer.flush();
            toServer.writeUTF(num);
            toServer.flush();
            toServer.writeUTF(entree);
            toServer.flush();
            toServer.writeUTF(mainCourse);
            toServer.flush();
            toServer.writeUTF(dessert);
            toServer.flush();
            toServer.writeUTF(sideDish);
            toServer.flush();
            toServer.writeUTF(drink);
            toServer.flush();
            toServer.writeUTF(String.valueOf(date));
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
            toServer.writeUTF("controllDate");
            toServer.flush();
            toServer.writeUTF(String.valueOf(d));
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
            toServer.writeUTF("deleteMenu");
            toServer.flush();
            toServer.writeUTF(String.valueOf(d));
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
            toServer.writeUTF("loadAllergicalInterni");
            toServer.flush();
            toServer.writeUTF(String.valueOf(date));
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
            toServer.writeUTF("saveIngredients");
            toServer.flush();
            toServer.writeUTF(dish);
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
            toServer.writeUTF("loadSpecialMenu");
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
            toServer.writeUTF("deleteSpecialMenu");
            toServer.flush();
            toServer.writeUTF(String.valueOf(date));
            toServer.flush();
            toServer.writeUTF(FC);
            toServer.flush();
            toServer.writeUTF(allergies);
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
            toServer.writeUTF("addSpecialMenu");
            toServer.flush();
            toServer.writeUTF(entree);
            toServer.flush();
            toServer.writeUTF(main);
            toServer.flush();
            toServer.writeUTF(dessert);
            toServer.flush();
            toServer.writeUTF(side);
            toServer.flush();
            toServer.writeUTF(drink);
            toServer.flush();
            toServer.writeUTF(String.valueOf(date));
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
            toServer.writeUTF("updateSpecialMenu");
            toServer.flush();
            toServer.writeUTF(entree);
            toServer.flush();
            toServer.writeUTF(main);
            toServer.flush();
            toServer.writeUTF(dessert);
            toServer.flush();
            toServer.writeUTF(side);
            toServer.flush();
            toServer.writeUTF(drink);
            toServer.flush();
            toServer.writeUTF(String.valueOf(date));
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
            toServer.writeUTF("loadDataTrip");
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
            toServer.writeUTF("deleteTrip");
            toServer.flush();
            //String dep, String dateDep, String dateCom, String staying, String dateArr, String arr
            toServer.writeUTF(dep);
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
            toServer.writeUTF("loadChildTrip");
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
            toServer.writeUTF("loadStaffTtrip");
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
            toServer.writeUTF("addSupplier");
            toServer.flush();
            toServer.writeObject(selectedChild);
            toServer.flush();
            toServer.writeObject(selectedStaff);
            toServer.flush();
            toServer.writeUTF(timeDep);
            toServer.flush();
            toServer.writeUTF(timeArr);
            toServer.flush();
            toServer.writeUTF(timeCom);
            toServer.flush();
            toServer.writeUTF(departureFrom);
            toServer.flush();
            toServer.writeUTF(arrivalTo);
            toServer.flush();
            toServer.writeUTF(staying);
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
            toServer.writeUTF("loadTripSelectedChildren");
            toServer.flush();
            toServer.writeUTF(selectedDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedDep);
            toServer.flush();
            toServer.writeUTF(selectedCom);
            toServer.flush();
            toServer.writeUTF(selectedAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedArr);
            toServer.flush();
            toServer.writeUTF(selectedArrTo);
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
            toServer.writeUTF("loadTripSelectedStaff");
            toServer.flush();
            toServer.writeUTF(selectedDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedDep);
            toServer.flush();
            toServer.writeUTF(selectedCom);
            toServer.flush();
            toServer.writeUTF(selectedAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedArr);
            toServer.flush();
            toServer.writeUTF(selectedArrTo);
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
            toServer.writeUTF("findNotAvailableStaff");
            toServer.flush();
            toServer.writeObject(selectedStaffCf);
            toServer.flush();
            toServer.writeUTF(selectedTripDep);
            toServer.flush();
            toServer.writeUTF(selectedTripCom);
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
            toServer.writeUTF("findNotAvailableChild");
            toServer.flush();
            toServer.writeObject(selectedChildCf);
            toServer.flush();
            toServer.writeUTF(selectedTripDep);
            toServer.flush();
            toServer.writeUTF(selectedTripCom);
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
            toServer.writeUTF("howManyActualParticipants");
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
            toServer.writeUTF("associateBusToParticipants");
            toServer.flush();
            toServer.writeObject(selectedChildCfArrayList);
            toServer.writeInt(totChildren);
            toServer.flush();
            toServer.writeObject(selectedStaffCfArrayList);
            toServer.flush();
            toServer.writeInt(totStaff);
            toServer.flush();
            toServer.writeUTF(selectedDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedDep);
            toServer.flush();
            toServer.writeUTF(selectedCom);
            toServer.flush();
            toServer.writeUTF(selectedAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedArr);
            toServer.flush();
            toServer.writeUTF(selectedArrTo);
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
            toServer.writeUTF("loadWhoTrip");
            toServer.flush();
            toServer.writeUTF(selectedDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedDep);
            toServer.flush();
            toServer.writeUTF(selectedCom);
            toServer.flush();
            toServer.writeUTF(selectedAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedArr);
            toServer.flush();
            toServer.writeUTF(selectedArrTo);
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
            toServer.writeUTF("loadBusTrip");
            toServer.flush();
            toServer.writeUTF(selectedDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedDep);
            toServer.flush();
            toServer.writeUTF(selectedCom);
            toServer.flush();
            toServer.writeUTF(selectedAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedArr);
            toServer.flush();
            toServer.writeUTF(selectedArrTo);
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
            toServer.writeUTF("loadSolution");
            toServer.flush();
            toServer.writeUTF(selectedTripDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedTripDep);
            toServer.flush();
            toServer.writeUTF(selectedTripCom);
            toServer.flush();
            toServer.writeUTF(selectedTripAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedTripArr);
            toServer.flush();
            toServer.writeUTF(selectedTripArrTo);
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
            toServer.writeUTF("findParticipantOnWrongBus");
            toServer.flush();
            toServer.writeObject(selectedChildCfArrayList);
            toServer.flush();
            toServer.writeUTF(selectedBus);
            toServer.flush();
            toServer.writeUTF(selectedTripDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedTripDep);
            toServer.flush();
            toServer.writeUTF(selectedTripCom);
            toServer.flush();
            toServer.writeUTF(selectedTripAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedTripArr);
            toServer.flush();
            toServer.writeUTF(selectedTripArrTo);
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
            toServer.writeUTF("findMissingParticipantsOnThisBus");
            toServer.flush();
            toServer.writeObject(peopleOnWrongBus);
            toServer.flush();
            toServer.writeObject(selectedChildCfArrayList);
            toServer.flush();
            toServer.writeUTF(selectedBus);
            toServer.flush();
            toServer.writeUTF(selectedTripDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedTripDep);
            toServer.flush();
            toServer.writeUTF(selectedTripCom);
            toServer.flush();
            toServer.writeUTF(selectedTripAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedTripArr);
            toServer.flush();
            toServer.writeUTF(selectedTripArrTo);
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
            toServer.writeUTF("makeIsHereFalse");
            toServer.flush();
            toServer.writeUTF(selectedTripDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedTripDep);
            toServer.flush();
            toServer.writeUTF(selectedTripCom);
            toServer.flush();
            toServer.writeUTF(selectedTripAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedTripArr);
            toServer.flush();
            toServer.writeUTF(selectedTripArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void makeIsHereTrue(String selectedBus, String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        try{
            toServer.writeUTF("makeIsHereTrue");
            toServer.flush();
            toServer.writeUTF(selectedBus);
            toServer.flush();
            toServer.writeUTF(selectedTripDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedTripDep);
            toServer.flush();
            toServer.writeUTF(selectedTripCom);
            toServer.flush();
            toServer.writeUTF(selectedTripAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedTripArr);
            toServer.flush();
            toServer.writeUTF(selectedTripArrTo);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadMissing(ArrayList<String> missingArrayList, String selectedBus, String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        try{
            toServer.writeUTF("loadMissing");
            toServer.flush();
            toServer.writeObject(missingArrayList);
            toServer.flush();
            toServer.writeUTF(selectedBus);
            toServer.flush();
            toServer.writeUTF(selectedTripDepFrom);
            toServer.flush();
            toServer.writeUTF(selectedTripDep);
            toServer.flush();
            toServer.writeUTF(selectedTripCom);
            toServer.flush();
            toServer.writeUTF(selectedTripAccomodation);
            toServer.flush();
            toServer.writeUTF(selectedTripArr);
            toServer.flush();
            toServer.writeUTF(selectedTripArrTo);
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