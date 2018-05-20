package application.socket.server;

import application.details.DishesDbDetails;
import application.details.IngredientsDbDetails;
import application.details.SpecialDbDetails;
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

        if(line.equals("login")) {
            System.out.println("Logging in...");

            String usr = inputFromClient.readUTF();
            String pwd = inputFromClient.readUTF();
            boolean isLogged = impl.funzLog(usr, pwd);
            outputToClient.writeBoolean(isLogged);
            return isLogged;
        }


        //CHILDREN -----------------------------------------------------------------------
        else if(line.equals("loadChild")){
            System.out.println("Loading children...");
            outputToClient.writeObject(impl.loadData());
            return true;
        }else if(line.equals("addChild")){
            System.out.println("Adding child...");
            String name = inputFromClient.readUTF();
            String surname = inputFromClient.readUTF();
            String cf = inputFromClient.readUTF();
            LocalDate bornOn = LocalDate.parse(inputFromClient.readUTF());
            String bornWhere = inputFromClient.readUTF();
            String residence = inputFromClient.readUTF();
            String address = inputFromClient.readUTF();
            String cap = inputFromClient.readUTF();
            String province = inputFromClient.readUTF();
            ArrayList<String> allergies = null;
            try {
                allergies = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String nameContact = inputFromClient.readUTF();
            String surnameContact = inputFromClient.readUTF();
            String cfContact = inputFromClient.readUTF();
            String mailContact = inputFromClient.readUTF();
            String telContact = inputFromClient.readUTF();
            LocalDate birthContact = LocalDate.parse(inputFromClient.readUTF());
            String bornWhereContact = inputFromClient.readUTF();
            String addressContact = inputFromClient.readUTF();
            String capContact = inputFromClient.readUTF();
            String provinceContact = inputFromClient.readUTF();
            Boolean isDoc = inputFromClient.readBoolean();
            Boolean isGuardian = inputFromClient.readBoolean();
            Boolean isContact = inputFromClient.readBoolean();
            Boolean add = impl.addData(name, surname, cf, bornOn, bornWhere, residence, address, cap, province, allergies, nameContact, surnameContact, cfContact, mailContact,telContact, birthContact,bornWhereContact, addressContact, capContact, provinceContact, isDoc, isGuardian, isContact);
            outputToClient.writeBoolean(add);
            return add;
        }else if(line.equals("updateChild")){
            System.out.println("Updating child...");
            String name = inputFromClient.readUTF();
            String surname = inputFromClient.readUTF();
            String oldCf = inputFromClient.readUTF();
            String cf = inputFromClient.readUTF();
            LocalDate bornOn = LocalDate.parse(inputFromClient.readUTF());
            String bornWhere = inputFromClient.readUTF();
            String residence = inputFromClient.readUTF();
            String address = inputFromClient.readUTF();
            String cap = inputFromClient.readUTF();
            String province = inputFromClient.readUTF();
            ArrayList<String> allergies = null;
            try {
                allergies = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Boolean update = impl.updateChild(name, surname,oldCf, cf, bornOn, bornWhere, residence, address, cap, province, allergies);
            outputToClient.writeBoolean(update);
            return update;
        }else if(line.equals("deleteChild")){
            System.out.println("Deleting child...");
            String cf = inputFromClient.readUTF();
            Boolean delete = impl.deleteChild(cf);
            outputToClient.writeBoolean(delete);
            return delete;
        }else if(line.equals("addContact")){
            System.out.println("Adding contact...");
            ArrayList<String> selectedChild = null;
            try{
                selectedChild = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String surname = inputFromClient.readUTF();
            String name = inputFromClient.readUTF();
            String cf = inputFromClient.readUTF();
            String mail = inputFromClient.readUTF();
            String tel = inputFromClient.readUTF();
            LocalDate birthday = LocalDate.parse(inputFromClient.readUTF());
            String bornWhere = inputFromClient.readUTF();
            String address = inputFromClient.readUTF();
            String cap = inputFromClient.readUTF();
            String province = inputFromClient.readUTF();
            Boolean isDoc = inputFromClient.readBoolean();
            Boolean isGuardian = inputFromClient.readBoolean();
            Boolean isContact = inputFromClient.readBoolean();
            Boolean add = impl.addContact(selectedChild, surname,name, cf, mail, tel, birthday, bornWhere, address,cap, province, isDoc, isGuardian, isContact);
            outputToClient.writeBoolean(add);
            return add;
        }else if(line.equals("deleteContact")){
            String oldcf = inputFromClient.readUTF();
            Boolean delete = impl.deleteContact(oldcf);
            outputToClient.writeBoolean(delete);
            return delete;
        }else if(line.equals("updateContact")){
            String name = inputFromClient.readUTF();
            String surname = inputFromClient.readUTF();
            String oldcf = inputFromClient.readUTF();
            String cf =inputFromClient.readUTF();
            String mail = inputFromClient.readUTF();
            String tel = inputFromClient.readUTF();
            LocalDate birthday = LocalDate.parse(inputFromClient.readUTF());
            String bornWhere = inputFromClient.readUTF();
            String address = inputFromClient.readUTF();
            String cap = inputFromClient.readUTF();
            String province = inputFromClient.readUTF();
            int isDoc = inputFromClient.read();
            int isGuardian = inputFromClient.read();
            int isContact = inputFromClient.read();
            Boolean update = impl.updateContact(name, surname, oldcf, cf, mail, tel, birthday, bornWhere, address, cap, province, isDoc, isGuardian, isContact);
            outputToClient.writeBoolean(update);
            return update;
        }else if(line.equals("loadDataContacts")){
            String rif = inputFromClient.readUTF();
            outputToClient.writeObject(impl.loadDataContacts(rif));
            return true;
        }


        //STAFF -------------------------------------------------------------------------
        else if(line.equals("loadDataStaff")){
            System.out.println("Loading staff...");
            outputToClient.writeObject(impl.loadDataStaff());
            return true;
        }else if(line.equals("addDataStaff")){
            System.out.println("Adding staff...");
            String name = inputFromClient.readUTF();
            String surname = inputFromClient.readUTF();
            String cf = inputFromClient.readUTF();
            String mail = inputFromClient.readUTF();
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            String bornWhere = inputFromClient.readUTF();
            String residece = inputFromClient.readUTF();
            String address = inputFromClient.readUTF();
            String cap = inputFromClient.readUTF();
            String province = inputFromClient.readUTF();
            ArrayList<String> selectedAllergies = null;
            try{
                selectedAllergies = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Boolean add = impl.addDataStaff(name, surname, cf, mail, date, bornWhere, residece, address, cap, province, selectedAllergies);
            outputToClient.writeBoolean(add);
            return add;
        }else if(line.equals("deleteStaff")){
            System.out.println("Deleting staff...");
            String cf = inputFromClient.readUTF();
            Boolean delete = impl.deleteStaff(cf);
            outputToClient.writeBoolean(delete);
            return delete;
        }else if(line.equals("updateStaff")){
            System.out.println("Updating staff...");
            String name = inputFromClient.readUTF();
            String surname = inputFromClient.readUTF();
            String oldcf = inputFromClient.readUTF();
            String cf = inputFromClient.readUTF();
            String mail = inputFromClient.readUTF();
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            String bornWhere = inputFromClient.readUTF();
            String residece = inputFromClient.readUTF();
            String address = inputFromClient.readUTF();
            String cap = inputFromClient.readUTF();
            String province = inputFromClient.readUTF();
            ArrayList<String> selectedAllergies = null;
            try{
                selectedAllergies = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Boolean update = impl.updateStaff(name, surname, oldcf, cf, mail, date, bornWhere, residece, address, cap, province, selectedAllergies);
            outputToClient.writeBoolean(update);
            return update;
        }


        //MENU -------------------------------------------------------------------------------
        else if(line.equals("loadmenu")){
            System.out.println("Loading menu...");
            outputToClient.writeObject(impl.loadMenu());
            return true;
        }else if(line.equals("deleteMenu")){
            System.out.println("Deleting menu...");
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            Boolean isDeleted = impl.deleteMenu(date);
            outputToClient.writeBoolean(isDeleted);
            return isDeleted;
        }else if(line.equals("loadIngredients")){
            System.out.println("Loading ingredients...");
            outputToClient.writeObject(impl.loadIngr());
            return true;
        }else if(line.equals("searchIngredients")){
            System.out.println("Looking for ingredients...");
            String dish = inputFromClient.readUTF();
            outputToClient.writeObject(impl.searchIngredients(dish));
        }else if(line.equals("controllDate")){
            System.out.println("Controlling the date...");
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            Boolean controll = impl.controllDate(date);
            outputToClient.writeBoolean(controll);
            return controll;
        }else if(line.equals("addMenu")){
            System.out.println("adding the mnu...");
            String num = inputFromClient.readUTF();
            String entree = inputFromClient.readUTF();
            String main = inputFromClient.readUTF();
            String dessert = inputFromClient.readUTF();
            String side = inputFromClient.readUTF();
            String drink = inputFromClient.readUTF();
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            Boolean add = impl.addMenu(num, entree, main, dessert, side, drink, date);
            outputToClient.writeBoolean(add);
            return add;
        }else if(line.equals("updateMenu")){
            System.out.println("updating the menu...");
            String num = inputFromClient.readUTF();
            String entree = inputFromClient.readUTF();
            String main = inputFromClient.readUTF();
            String dessert = inputFromClient.readUTF();
            String side = inputFromClient.readUTF();
            String drink = inputFromClient.readUTF();
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            LocalDate oldDate = LocalDate.parse(inputFromClient.readUTF());
            Boolean update = impl.updateMenu(num, entree, main, dessert, side, drink, date, oldDate);
            outputToClient.writeBoolean(update);
            return update;
        }else if(line.equals("saveIngredients")){
            System.out.println("saving ingredients...");
            String dish = inputFromClient.readUTF();
            ArrayList<String> selection = new ArrayList<>();
            try {
                selection = (ArrayList<String>)inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Boolean save = impl.saveIngredients(dish, selection);
            outputToClient.writeBoolean(save);
            return save;
        }


        //SUPPLIER ------------------------------------------------------------
        else if(line.equals("loadSuppliers")){
            System.out.println("Loading suppliers...");
            outputToClient.writeObject(impl.loadDataSuppliers());
            return true;
        }else if(line.equals("addSupplier")){
            System.out.println("Adding supplier...");
            String name = inputFromClient.readUTF();
            String piva = inputFromClient.readUTF();
            String mail = inputFromClient.readUTF();
            String tel = inputFromClient.readUTF();
            String address = inputFromClient.readUTF();
            String cap = inputFromClient.readUTF();
            String province = inputFromClient.readUTF();
            Boolean add = impl.addDataSupplier(name, piva,mail, tel, address, cap, province );
            outputToClient.writeBoolean(add);
            return add;
        }else if(line.equals("updateSupplier")) {
            System.out.println("Updating supplier...");
            String name = inputFromClient.readUTF();
            String oldPiva = inputFromClient.readUTF();
            String piva = inputFromClient.readUTF();
            String mail = inputFromClient.readUTF();
            String tel = inputFromClient.readUTF();
            String address = inputFromClient.readUTF();
            String cap = inputFromClient.readUTF();
            String province = inputFromClient.readUTF();
            Boolean update = impl.updateSupplier(name, oldPiva, piva, mail, tel, address, cap, province);
            outputToClient.writeBoolean(update);
            return update;
        }else if(line.equals("loadDataIngr")){
            System.out.println("Loading ingredients...");
            String selection = inputFromClient.readUTF();
            outputToClient.writeObject(impl.loadDataIngr(selection));
            return true;
        }else if(line.equals("addIngredientSupplier")){
            System.out.println("Adding ingredient...");
            String ingr = inputFromClient.readUTF();
            String selectedSupplier = inputFromClient.readUTF();
            Boolean add = impl.addIngrToDb(ingr, selectedSupplier);
            outputToClient.writeBoolean(add);
            return add;

        }else if(line.equals("loadMenuWithThisSupplier")){
            System.out.println("Loading these menus...");
            String selectedSupplier = inputFromClient.readUTF();
            outputToClient.writeObject(impl.loadMenuWithThisSupplier(selectedSupplier));
            return true;
        }else if(line.equals("loadNoIngr")){
            System.out.println("Loading no available ingredients...");
            String selectedSupplier = inputFromClient.readUTF();
            outputToClient.writeObject(impl.loadNoIngr(selectedSupplier));
            return true;
        }else if(line.equals("deleteSupplier")){
            System.out.println("Deleting supplier...");
            String piva = inputFromClient.readUTF();
            ArrayList<IngredientsDbDetails> ingredients = new ArrayList<>();
            try {
                ingredients = (ArrayList<IngredientsDbDetails>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Boolean delete = impl.deleteSupplier(piva, ingredients);
            outputToClient.writeBoolean(delete);
            return delete;
        }


        //SPECIAL MENU --------------------------------------------------------------
        else if(line.equals("addSpecialMenu")){
            System.out.println("Adding special menu...");
            String entree = inputFromClient.readUTF();
            String main = inputFromClient.readUTF();
            String dessert = inputFromClient.readUTF();
            String side = inputFromClient.readUTF();
            String drink = inputFromClient.readUTF();
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            SpecialDbDetails special = null;
            try {
                special = (SpecialDbDetails) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Boolean add = impl.addSpecialMenu(entree, main, dessert, side, drink, date, special);
            outputToClient.writeBoolean(add);
            return add;
        }else if(line.equals("loadAllergicalInterni")){
            System.out.println("Loading allergical interni...");
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            outputToClient.writeObject(impl.loadInterniWithAllergies(date));
            return true;
        }else if(line.equals("deleteSpecialMenu")){
            System.out.println("Deleting special menu....");
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            String FC = inputFromClient.readUTF();
            String allergies = inputFromClient.readUTF();
            Boolean delete = impl.deleteSpecialMenu(date, FC, allergies);
            outputToClient.writeBoolean(delete);
            return delete;
        }else if(line.equals("loadSpecialMenu")){
            System.out.println("Loading special menu...");
            outputToClient.writeObject(impl.loadSpecialMenu());
            return true;
        }else if(line.equals("updateSpecialMenu")){
            System.out.println("Updating special menu...");
            String entree = inputFromClient.readUTF();
            String main = inputFromClient.readUTF();
            String dessert = inputFromClient.readUTF();
            String side = inputFromClient.readUTF();
            String drink = inputFromClient.readUTF();
            LocalDate date = LocalDate.parse(inputFromClient.readUTF());
            SpecialDbDetails special = null;
            try{
                special = (SpecialDbDetails) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Boolean update = impl.updateSpecialMenu(entree, main, dessert,side, drink, date, special);
            outputToClient.writeBoolean(update);
            return update;
        }


        //TRIP -------------------------------------------
        else if(line.equals("loadDataTrip")){
            System.out.println("Loading data trip...");
            outputToClient.writeObject(impl.loadDataTrip());
            return true;
        } else if(line.equals("deleteTrip")){
            System.out.println("Deleting supplier...");
            String dep = inputFromClient.readUTF();
            String dateDep = inputFromClient.readUTF();
            String dateCom = inputFromClient.readUTF();
            String staying = inputFromClient.readUTF();
            String dateArr = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            Boolean delete = impl.deleteTrip(dep, dateDep, dateCom, staying, dateArr, arr);
            outputToClient.writeBoolean(delete);
            return delete;
        } else if(line.equals("loadChildTrip")){
            System.out.println("Loading child for trip...");
            outputToClient.writeObject(impl.loadChildTrip());
            return true;
        } else if(line.equals("loadStaffTrip")) {
            System.out.println("Loading staff for trip...");
            outputToClient.writeObject(impl.loadStaffTrip());
            return true;
        } else if (line.equals("addTrip")){
            System.out.println("Adding trip...");
            int[] returnedPart;
            //ArrayList<String> selectedChild, ArrayList<String> selectedStaff, String timeDep,
            //String timeArr, String timeCom, String departureFrom, String arrivalTo, String staying
            ArrayList<String> children = null;
            ArrayList<String> staff = null;
            try {
                children = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                staff = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String timeDep = inputFromClient.readUTF();
            String timeArr = inputFromClient.readUTF();
            String timeCom = inputFromClient.readUTF();
            String depFrom = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            returnedPart = impl.addTrip(children, staff, timeDep, timeArr, timeCom, depFrom, arrTo, arr);
            for(int i=0; i<2; i++){
                outputToClient.writeInt(returnedPart[i]);
            }
            return true;
        } else if(line.equals("loadTripSelectedChildren")){
            System.out.println("Loading effective child for trip...");
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            outputToClient.writeObject(impl.loadTripSelectedChildren(depFrom, dep, com, accomodation, arr, arrTo));
            return true;
        } else if(line.equals("loadTripSelectedStaff")) {
            System.out.println("Loading effective staff for trip...");
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            outputToClient.writeObject(impl.loadTripSelectedStaff(depFrom, dep, com, accomodation, arr, arrTo));
            return true;
        } else if(line.equals("findNotAvailableStaff")){
            System.out.println("Loading not available staff for trip...");
            ArrayList<String> staff = null;
            try {
                staff = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();

            outputToClient.writeObject(impl.findNotAvailableStaff(staff, dep, com));
            return true;
        } else if(line.equals("findNotAvailableChild")){
            System.out.println("Loading not available children for trip...");
            ArrayList<String> child = null;
            try {
                child = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();

            outputToClient.writeObject(impl.findNotAvailableStaff(child, dep, com));
            return true;
        } else if(line.equals("howManyActualParticipants")){
            System.out.println("Calculating number of participants...");
            int[] returnedPart;
            ArrayList<String> children = null;
            ArrayList<String> staff = null;
            try {
                children = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                staff = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            returnedPart = impl.howManyActualParticipants(children, staff);
            for(int i=0; i<2; i++){
                outputToClient.writeInt(returnedPart[i]);
            }
            return true;
        } else if(line.equals("loadWhoTrip")){
            System.out.println("Loading who participates to this trip...");
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            outputToClient.writeObject(impl.loadWhoTrip(depFrom, dep, com, accomodation, arr, arrTo));
            return true;
        } else if(line.equals("loadBusTrip")){
            System.out.println("Loading bus to this trip...");
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            outputToClient.writeObject(impl.loadBusTrip(depFrom, dep, com, accomodation, arr, arrTo));
            return true;
        } else if(line.equals("loadSolution")) {
            System.out.println("Loading solution...");
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            outputToClient.writeObject(impl.loadSolution(depFrom, dep, com, accomodation, arr, arrTo));
            return true;
        } else if(line.equals("findParticipantsOnWrongBus")) {
            System.out.println("Searching for participants on wrong bus...");
            ArrayList<String> participants = null;
            try {
                participants = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String selectedBus = inputFromClient.readUTF();
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            outputToClient.writeObject(impl.findParticipantOnWrongBus(participants, selectedBus, depFrom, dep, com, accomodation, arr, arrTo));
            return true;
        } else if(line.equals("findMissingParticipantsOnThisBus")) {
            System.out.println("Searching for missing participants on this bus...");
            ArrayList<String> peopleOnWrongBus = null;
            ArrayList<String> selectedChild = null;
            try {
                peopleOnWrongBus = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                selectedChild = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String selectedBus = inputFromClient.readUTF();
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            outputToClient.writeObject(impl.findMissingParticipantsOnThisBus(peopleOnWrongBus, selectedChild, selectedBus, depFrom, dep, com, accomodation, arr, arrTo));
            return true;
        } else if(line.equals("loadMissing")){
            System.out.println("Loading missing participants on this bus...");
            ArrayList<String> missing = null;
            try {
                missing = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String selectedBus = inputFromClient.readUTF();
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            outputToClient.writeObject(impl.loadMissing(missing, selectedBus, depFrom, dep, com, accomodation, arr, arrTo));
            return true;
        } else if(line.equals("associateBusToParticipants")){
            System.out.println("Associating participants to bus...");
            ArrayList<String> children = null;
            ArrayList<String> staff = null;
            try {
                children = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            int totChildren = inputFromClient.readInt();
            try {
                staff = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            int totStaff = inputFromClient.readInt();
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            outputToClient.writeObject(impl.associateBusToParticipants(children, totChildren, staff, totStaff, depFrom, dep, com, accomodation, arr, arrTo));
            return true;
        } else if(line.equals("makeIsHereTrue")){
            String selectedBus = inputFromClient.readUTF();
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            impl.makeIsHereTrue(selectedBus, depFrom, dep, com, accomodation, arr, arrTo);
            return true;
        } else if(line.equals("makeIsHereFalse")){
            String depFrom = inputFromClient.readUTF();
            String dep = inputFromClient.readUTF();
            String com = inputFromClient.readUTF();
            String accomodation = inputFromClient.readUTF();
            String arr = inputFromClient.readUTF();
            String arrTo = inputFromClient.readUTF();
            impl.makeIsHereFalse(depFrom, dep, com, accomodation, arr, arrTo);
            return true;
        }


        return false;
    }
}
