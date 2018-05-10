package application.socket.client;

import application.Interfaces.UserRemote;
import application.details.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


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


    //LOGIN --------------------------------------------------------------------------------------------------
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


    //CHILDREN ---------------------------------------------------------------------------
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
    public boolean deleteChild(String cf) throws RemoteException{
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

    //SUPPLIERS ------------------------------------------------------------------------
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
    public ArrayList<CodRifChildDbDetails> loadDataIngr(String selectedSupplier) throws RemoteException {
        return null;
    }

    @Override
    public boolean addIngrToDb(String ingr, String selectedSupplier) throws RemoteException {
        return false;
    }

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
    public ArrayList<BusPlateCapacityDbDetails> loadDataBus(String selectedSupplier) throws RemoteException {
        return null;
    }

    @Override
    public boolean addBusToDb(String plate, int capacity, String selectedSupplier) throws RemoteException {
        return false;
    }

    //MENU -------------------------------------------------------------------------------
    @Override
    public DishesDbDetails loadThisMenu(LocalDate date) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<IngredientsDbDetails> searchIngredients(String dish) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadIngr(LocalDate day) throws RemoteException {
        return null;
    }

    @Override
    public boolean updateMenu(String num, String entree, String main, String dessert, String side, String drink, LocalDate day, LocalDate oldDate) throws RemoteException {
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
    public boolean controllDate(LocalDate d) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteMenu(LocalDate d) throws RemoteException {
        return false;
    }

    @Override
    public ArrayList<SpecialDbDetails> loadInterniWithAllergies(LocalDate date) throws RemoteException {
        return null;
    }

    @Override
    public boolean saveIngredients(String dish, ArrayList<String> selectedIngredients) throws RemoteException {
        return false;
    }

    //SPECIAL MENU ------------------------------------------------------------------------------------
    @Override
    public ArrayList<SpecialMenuDbDetails> loadSpecialMenu() throws RemoteException {
        return null;
    }

    @Override
    public boolean deleteSpecialMenu(LocalDate date, String FC, String allergies) throws RemoteException {
        return false;
    }

    @Override
    public boolean addSpecialMenu(String entree, String main, String dessert, String side, String drink, LocalDate date, SpecialDbDetails special) throws RemoteException {
        return false;
    }

    @Override
    public boolean updateSpecialMenu(String entree, String main, String dessert, String side, String drink, LocalDate date, SpecialDbDetails special) throws RemoteException {
        return false;
    }

    //TRIP -------------------------------------------------------------------------------------
    @Override
    public ArrayList<TripTableDbDetails> loadDataTrip() throws RemoteException {
        return null;
    }

    @Override
    public boolean deleteTrip(String dep, String dateDep, String dateCom, String alloggio, String dateArr, String arr) throws RemoteException {
        return false;
    }


    @Override
    public ArrayList<ChildTripDbDetails> loadChildTrip() throws RemoteException{
        return  null;
    }

    @Override
    public ArrayList<StaffTripDbDetails> loadStaffTrip() throws RemoteException{
        return null;
    }

    @Override
    public int[] addTrip(ArrayList<String> selectedChild, ArrayList<String> selectedStaff, String timeDep, String timeArr, String timeCom, String departureFrom, String ArrivalTo, String staying) throws RemoteException {
        return new int[0];
    }

    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadTripSelectedChildren (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<StaffSelectedTripDbDetails> loadTripSelectedStaff (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> findNotAvailableStaff(ArrayList<String> selectedStaffCf, String selectedTripDep, String selectedTripCom) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> findNotAvailableChild(ArrayList<String> selectedChildCf, String selectedTripDep, String selectedTripCom) throws RemoteException {
        return null;
    }

    @Override
    public int[] howManyActualParticipants(ArrayList<String> selectedChildCf, ArrayList<String> selectedStaffCf) throws RemoteException {
        return new int[0];
    }

    @Override
    public HashMap<String, ArrayList<String>> associateBusToParticipants(ArrayList<String> selectedChildCfArrayList, int totChildren, ArrayList<String> selectedStaffCfArrayList, int totStaff, String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadWhoTrip(String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> loadBusTrip(String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<SolutionDbDetails> loadSolution(String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        return null;
    }


}