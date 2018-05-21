package application.socket.server;

import application.details.IngredientsDbDetails;
import application.details.SpecialDbDetails;
import application.rmi.server.ServerImpl;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

//quello corretto
public class SocketThread extends Thread implements Runnable {

    private String line = "";
    private ObjectOutputStream outputToClient;
    private ObjectInputStream inputFromClient;
    private Socket s = null;
    private ServerImpl impl;  //mi serve per poter chiamare le funzioni  ESSENZIALE!!!!!
    private int counter;
    //devo considerare che per chiamare  i metodi devo mandare dei messaggi

    public SocketThread(Socket s, ServerImpl impl, int counter) {
        this.impl = impl;
        this.s = s;
        this.counter = counter;
    }

    @Override
    public void run() {
        try {
            outputToClient = new ObjectOutputStream(s.getOutputStream());
            outputToClient.flush();
            inputFromClient = new ObjectInputStream(s.getInputStream());

        } catch (IOException e) {
            System.out.println("IO error in server thread");
            e.printStackTrace();
        }

        try {

            while ( !line.equals("bye")) {
                System.out.println("Ready to receive a message");
                line = (String)inputFromClient.readObject();

                System.out.println("Received " + line + " from client # " + counter + "");

                boolean responce = doAction(line);  //passo il messaggio al doAction che decide cosa fare

                System.out.println("sending back : " + responce);
                outputToClient.writeBoolean(responce);
                outputToClient.flush();
            }

        } catch (Exception e) {
            line = this.getName();  //salva in line il nome del thread che sta eseguendo
            System.out.println("IO Error/ Client " + line + " terminated abruptly");
            e.printStackTrace();
        } finally{
            try {
                s.close();
                inputFromClient.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        /* LOGOUT -----------------------------------------------------------------------------------------------
        finally {
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
                ie.printStackTrace();
            }
        } ------------------------------------------------------------------------------------------------------------
        */
    }

    private boolean doAction(String line) throws IOException {

        //String[] credentials = line.split("\\s+");

        if(line.equals("login")) {
            System.out.println("Logging in...");
            boolean isLogged = false;
            try {
                String usr = (String)inputFromClient.readObject();
                String pwd = (String)inputFromClient.readObject();
                isLogged = impl.funzLog(usr, pwd);
                outputToClient.writeBoolean(isLogged);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return isLogged;
        }


        //CHILDREN -----------------------------------------------------------------------
        else if(line.equals("loadChild")){
            System.out.println("Loading children...");
            outputToClient.writeObject(impl.loadData());
            return true;
        }else if(line.equals("addChild")){
            System.out.println("Adding child...");
            boolean add = false;
            try {
                String name = (String)inputFromClient.readObject();
                String surname = (String)inputFromClient.readObject();
                String cf = (String)inputFromClient.readObject();
                LocalDate bornOn = LocalDate.parse((CharSequence) inputFromClient.readObject());
                String bornWhere = (String)inputFromClient.readObject();
                String residence = (String)inputFromClient.readObject();
                String address = (String)inputFromClient.readObject();
                String cap = (String)inputFromClient.readObject();
                String province = (String)inputFromClient.readObject();
                ArrayList<String> allergies = null;
                try {
                    allergies = (ArrayList<String>) inputFromClient.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String nameContact = (String)inputFromClient.readObject();
                String surnameContact = (String)inputFromClient.readObject();
                String cfContact = (String)inputFromClient.readObject();
                String mailContact = (String)inputFromClient.readObject();
                String telContact = (String)inputFromClient.readObject();
                LocalDate birthContact = LocalDate.parse((CharSequence)inputFromClient.readObject());
                String bornWhereContact = (String)inputFromClient.readObject();
                String addressContact = (String)inputFromClient.readObject();
                String capContact = (String)inputFromClient.readObject();
                String provinceContact = (String)inputFromClient.readObject();
                boolean isDoc = inputFromClient.readBoolean();
                boolean isGuardian = inputFromClient.readBoolean();
                boolean isContact = inputFromClient.readBoolean();
                
                add = impl.addData(name, surname, cf, bornOn, bornWhere, residence, address, cap, province, allergies, nameContact, surnameContact, cfContact, mailContact,telContact, birthContact,bornWhereContact, addressContact, capContact, provinceContact, isDoc, isGuardian, isContact);
                outputToClient.writeBoolean(add);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return add;
        }else if(line.equals("updateChild")){
            System.out.println("Updating child...");
            boolean update = false;
            try {
                String name = (String)inputFromClient.readObject();
                String surname = (String)inputFromClient.readObject();
                String oldCf = (String)inputFromClient.readObject();
                String cf = (String)inputFromClient.readObject();
                LocalDate bornOn = LocalDate.parse((CharSequence)inputFromClient.readObject());
                String bornWhere = (String)inputFromClient.readObject();
                String residence = (String)inputFromClient.readObject();
                String address = (String)inputFromClient.readObject();
                String cap = (String)inputFromClient.readObject();
                String province = (String)inputFromClient.readObject();
                ArrayList<String> allergies = null;
                try {
                    allergies = (ArrayList<String>) inputFromClient.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                update = impl.updateChild(name, surname,oldCf, cf, bornOn, bornWhere, residence, address, cap, province, allergies);
                outputToClient.writeBoolean(update);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return update;
        }else if(line.equals("deleteChild")){
            System.out.println("Deleting child...");
            boolean delete = false;
            try {
                String cf = (String)inputFromClient.readObject();
                delete = impl.deleteChild(cf);
                outputToClient.writeBoolean(delete);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return delete;
        }else if(line.equals("addContact")){
            System.out.println("Adding contact...");
            boolean add = false;
            try{
                ArrayList<String> selectedChild = (ArrayList<String>) inputFromClient.readObject();
                String surname = (String)inputFromClient.readObject();
                String name = (String)inputFromClient.readObject();
                String cf = (String)inputFromClient.readObject();
                String mail = (String)inputFromClient.readObject();
                String tel = (String)inputFromClient.readObject();
                LocalDate birthday = LocalDate.parse((CharSequence)inputFromClient.readObject());
                String bornWhere = (String)inputFromClient.readObject();
                String address = (String)inputFromClient.readObject();
                String cap = (String)inputFromClient.readObject();
                String province = (String)inputFromClient.readObject();
                boolean isDoc = inputFromClient.readBoolean();
                boolean isGuardian = inputFromClient.readBoolean();
                boolean isContact = inputFromClient.readBoolean();
                add = impl.addContact(selectedChild, surname,name, cf, mail, tel, birthday, bornWhere, address,cap, province, isDoc, isGuardian, isContact);
                outputToClient.writeBoolean(add);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return add;
        }else if(line.equals("deleteContact")){
            boolean delete = false;
            try {
                String oldcf = (String)inputFromClient.readObject();
                delete = impl.deleteContact(oldcf);
                outputToClient.writeBoolean(delete);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return delete;
        }else if(line.equals("updateContact")){
            boolean update = false;
            try {
                String name = (String)inputFromClient.readObject();
                String surname = (String)inputFromClient.readObject();
                String oldcf = (String)inputFromClient.readObject();
                String cf = (String)inputFromClient.readObject();
                String mail = (String)inputFromClient.readObject();
                String tel = (String)inputFromClient.readObject();
                LocalDate birthday = LocalDate.parse((CharSequence)inputFromClient.readObject());
                String bornWhere = (String)inputFromClient.readObject();
                String address = (String)inputFromClient.readObject();
                String cap = (String)inputFromClient.readObject();
                String province = (String)inputFromClient.readObject();
                int isDoc = inputFromClient.read();
                int isGuardian = inputFromClient.read();
                int isContact = inputFromClient.read();
                update = impl.updateContact(name, surname, oldcf, cf, mail, tel, birthday, bornWhere, address, cap, province, isDoc, isGuardian, isContact);
                outputToClient.writeBoolean(update);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return update;
        }else if(line.equals("loadDataContacts")){
            String rif = null;
            try {
                rif = (String)inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
            boolean add = false;
            try {
                String name = (String)inputFromClient.readObject();
                String surname = (String)inputFromClient.readObject();
                String cf = (String)inputFromClient.readObject();
                String mail = (String)inputFromClient.readObject();
                LocalDate date = LocalDate.parse((CharSequence)inputFromClient.readObject());
                String bornWhere = (String)inputFromClient.readObject();
                String residece = (String)inputFromClient.readObject();
                String address = (String)inputFromClient.readObject();
                String cap = (String)inputFromClient.readObject();
                String province = (String)inputFromClient.readObject();
                ArrayList<String> selectedAllergies = null;
                try{
                    selectedAllergies = (ArrayList<String>) inputFromClient.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                add = impl.addDataStaff(name, surname, cf, mail, date, bornWhere, residece, address, cap, province, selectedAllergies);
                outputToClient.writeBoolean(add);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return add;
        }else if(line.equals("deleteStaff")){
            System.out.println("Deleting staff...");
            boolean delete = false;
            try {
                String cf = (String)inputFromClient.readObject();
                delete = impl.deleteStaff(cf);
                outputToClient.writeBoolean(delete);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return delete;
        }else if(line.equals("updateStaff")){
            System.out.println("Updating staff...");
            boolean update = false;
            try {
                String name = (String)inputFromClient.readObject();
                String surname = (String)inputFromClient.readObject();
                String oldcf = (String)inputFromClient.readObject();
                String cf = (String)inputFromClient.readObject();
                String mail = (String)inputFromClient.readObject();
                LocalDate date = LocalDate.parse((CharSequence)inputFromClient.readObject());
                String bornWhere = (String)inputFromClient.readObject();
                String residece = (String)inputFromClient.readObject();
                String address = (String)inputFromClient.readObject();
                String cap = (String)inputFromClient.readObject();
                String province = (String)inputFromClient.readObject();
                ArrayList<String> selectedAllergies = null;
                try{
                    selectedAllergies = (ArrayList<String>) inputFromClient.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                update = impl.updateStaff(name, surname, oldcf, cf, mail, date, bornWhere, residece, address, cap, province, selectedAllergies);
                outputToClient.writeBoolean(update);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return update;
        }


        //MENU -------------------------------------------------------------------------------
        else if(line.equals("loadmenu")){
            System.out.println("Loading menu...");
            outputToClient.writeObject(impl.loadMenu());
            return true;
        }else if(line.equals("deleteMenu")){
            System.out.println("Deleting menu...");
            boolean isDeleted = false;
            try {
                LocalDate date = LocalDate.parse((CharSequence)inputFromClient.readObject());
                isDeleted = impl.deleteMenu(date);
                outputToClient.writeBoolean(isDeleted);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return isDeleted;
        }else if(line.equals("loadIngredients")){
            System.out.println("Loading ingredients...");
            outputToClient.writeObject(impl.loadIngr());
            return true;
        }else if(line.equals("searchIngredients")){
            System.out.println("Looking for ingredients...");
            try {
                String dish = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.searchIngredients(dish));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if(line.equals("controllDate")){
            System.out.println("Controlling the date...");
            boolean controll = false;
            try {
                LocalDate date = LocalDate.parse((CharSequence)inputFromClient.readObject());
                controll = impl.controllDate(date);
                outputToClient.writeBoolean(controll);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return controll;
        }else if(line.equals("addMenu")){
            System.out.println("adding the menu...");
            boolean add = false;
            try {
                String num = (String)inputFromClient.readObject();
                String entree = (String)inputFromClient.readObject();
                String main = (String)inputFromClient.readObject();
                String dessert = (String)inputFromClient.readObject();
                String side = (String)inputFromClient.readObject();
                String drink = (String)inputFromClient.readObject();
                LocalDate date = LocalDate.parse((CharSequence)inputFromClient.readObject());
                add = impl.addMenu(num, entree, main, dessert, side, drink, date);
                outputToClient.writeBoolean(add);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return add;
        }else if(line.equals("updateMenu")){
            System.out.println("updating the menu...");
            boolean update = false;
            try {
                String num = (String)inputFromClient.readObject();
                String entree = (String)inputFromClient.readObject();
                String main = (String)inputFromClient.readObject();
                String dessert = (String)inputFromClient.readObject();
                String side = (String)inputFromClient.readObject();
                String drink = (String)inputFromClient.readObject();
                LocalDate date = LocalDate.parse((CharSequence)inputFromClient.readObject());
                LocalDate oldDate = LocalDate.parse((CharSequence)inputFromClient.readObject());
                update = impl.updateMenu(num, entree, main, dessert, side, drink, date, oldDate);
                outputToClient.writeBoolean(update);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return update;
        }else if(line.equals("saveIngredients")){
            System.out.println("saving ingredients...");
            ArrayList<String> selection = new ArrayList<>();
            boolean save = false;
            try {
                String dish = (String)inputFromClient.readObject();
                selection = (ArrayList<String>)inputFromClient.readObject();
                save = impl.saveIngredients(dish, selection);
                outputToClient.writeBoolean(save);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return save;
        }


        //SUPPLIER ------------------------------------------------------------
        else if(line.equals("loadSuppliers")){
            System.out.println("Loading suppliers...");
            outputToClient.writeObject(impl.loadDataSuppliers());
            return true;
        }else if(line.equals("addSupplier")){
            System.out.println("Adding supplier...");
            boolean add = false;
            try {
                String name = (String)inputFromClient.readObject();
                String piva = (String)inputFromClient.readObject();
                String mail = (String)inputFromClient.readObject();
                String tel = (String)inputFromClient.readObject();
                String address = (String)inputFromClient.readObject();
                String cap = (String)inputFromClient.readObject();
                String province = (String)inputFromClient.readObject();
                add = impl.addDataSupplier(name, piva,mail, tel, address, cap, province );
                outputToClient.writeBoolean(add);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return add;
        }else if(line.equals("updateSupplier")) {
            System.out.println("Updating supplier...");
            boolean update = false;
            try {
                String name = (String)inputFromClient.readObject();
                String oldPiva = (String)inputFromClient.readObject();
                String piva = (String)inputFromClient.readObject();
                String mail = (String)inputFromClient.readObject();
                String tel = (String)inputFromClient.readObject();
                String address = (String)inputFromClient.readObject();
                String cap = (String)inputFromClient.readObject();
                String province = (String)inputFromClient.readObject();
                update = impl.updateSupplier(name, oldPiva, piva, mail, tel, address, cap, province);
                outputToClient.writeBoolean(update);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return update;
        }else if(line.equals("loadDataIngr")){
            System.out.println("Loading ingredients...");
            String selection = null;
            try {
                selection = (String)inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            outputToClient.writeObject(impl.loadDataIngr(selection));
            return true;
        }else if(line.equals("addIngredientSupplier")){
            System.out.println("Adding ingredient...");
            boolean add = false;
            try {
                String ingr = (String)inputFromClient.readObject();
                String selectedSupplier = (String)inputFromClient.readObject();
                add = impl.addIngrToDb(ingr, selectedSupplier);
                outputToClient.writeBoolean(add);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return add;

        }else if(line.equals("loadMenuWithThisSupplier")){
            System.out.println("Loading these menus...");
            try {
                String selectedSupplier = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.loadMenuWithThisSupplier(selectedSupplier));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }else if(line.equals("loadNoIngr")){
            System.out.println("Loading no available ingredients...");
            try {
                String selectedSupplier = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.loadNoIngr(selectedSupplier));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }else if(line.equals("deleteSupplier")){
            System.out.println("Deleting supplier...");
            ArrayList<IngredientsDbDetails> ingredients = new ArrayList<>();
            boolean delete = false;
            try {
                String piva = (String)inputFromClient.readObject();
                ingredients = (ArrayList<IngredientsDbDetails>) inputFromClient.readObject();
                delete = impl.deleteSupplier(piva, ingredients);
                outputToClient.writeBoolean(delete);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return delete;
        }


        //SPECIAL MENU --------------------------------------------------------------
        else if(line.equals("addSpecialMenu")){
            System.out.println("Adding special menu...");
            boolean add = false;
            try {
                String entree = (String)inputFromClient.readObject();
                String main = (String)inputFromClient.readObject();
                String dessert = (String)inputFromClient.readObject();
                String side = (String)inputFromClient.readObject();
                String drink = (String)inputFromClient.readObject();
                LocalDate date = LocalDate.parse((CharSequence)inputFromClient.readObject());
                SpecialDbDetails special = null;
                try {
                    special = (SpecialDbDetails) inputFromClient.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                add = impl.addSpecialMenu(entree, main, dessert, side, drink, date, special);
                outputToClient.writeBoolean(add);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return add;
        }else if(line.equals("loadAllergicalInterni")){
            System.out.println("Loading allergical interni...");
            LocalDate date = null;
            try {
                date = LocalDate.parse((CharSequence)inputFromClient.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            outputToClient.writeObject(impl.loadInterniWithAllergies(date));
            return true;
        }else if(line.equals("deleteSpecialMenu")){
            System.out.println("Deleting special menu....");
            boolean delete = false;
            try {
                LocalDate date = LocalDate.parse((CharSequence)inputFromClient.readObject());
                String FC = (String)inputFromClient.readObject();
                String allergies = (String)inputFromClient.readObject();
                delete = impl.deleteSpecialMenu(date, FC, allergies);
                outputToClient.writeBoolean(delete);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return delete;
        }else if(line.equals("loadSpecialMenu")){
            System.out.println("Loading special menu...");
            outputToClient.writeObject(impl.loadSpecialMenu());
            return true;
        }else if(line.equals("updateSpecialMenu")){
            System.out.println("Updating special menu...");
            boolean update = false;
            try {
                String entree = (String)inputFromClient.readObject();
                String main = (String)inputFromClient.readObject();
                String dessert = (String)inputFromClient.readObject();
                String side = (String)inputFromClient.readObject();
                String drink = (String)inputFromClient.readObject();
                LocalDate date = LocalDate.parse((CharSequence)inputFromClient.readObject());
                SpecialDbDetails special = null;
                try{
                    special = (SpecialDbDetails) inputFromClient.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                update = impl.updateSpecialMenu(entree, main, dessert,side, drink, date, special);
                outputToClient.writeBoolean(update);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return update;
        }


        //TRIP -------------------------------------------
        else if(line.equals("loadDataTrip")){
            System.out.println("Loading data trip...");
            outputToClient.writeObject(impl.loadDataTrip());
            return true;
        } else if(line.equals("deleteTrip")){
            System.out.println("Deleting supplier...");
            boolean delete = false;
            try {
                String dep = (String)inputFromClient.readObject();
                String dateDep = (String)inputFromClient.readObject();
                String dateCom = (String)inputFromClient.readObject();
                String staying = (String)inputFromClient.readObject();
                String dateArr = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                delete = impl.deleteTrip(dep, dateDep, dateCom, staying, dateArr, arr);
                outputToClient.writeBoolean(delete);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
            try {
                String timeDep = (String)inputFromClient.readObject();
                String timeArr = (String)inputFromClient.readObject();
                String timeCom = (String)inputFromClient.readObject();
                String depFrom = (String)inputFromClient.readObject();
                String arrTo = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                returnedPart = impl.addTrip(children, staff, timeDep, timeArr, timeCom, depFrom, arrTo, arr);
                for(int i=0; i<2; i++){
                    outputToClient.writeInt(returnedPart[i]);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("loadTripSelectedChildren")){
            System.out.println("Loading effective child for trip...");
            try {
                String depFrom = (String)inputFromClient.readObject();
                String dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                String accomodation = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                String arrTo = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.loadTripSelectedChildren(depFrom, dep, com, accomodation, arr, arrTo));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("loadTripSelectedStaff")) {
            System.out.println("Loading effective staff for trip...");
            try {
                String depFrom = (String)inputFromClient.readObject();
                String dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                String accomodation = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                String arrTo = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.loadTripSelectedStaff(depFrom, dep, com, accomodation, arr, arrTo));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("findNotAvailableStaff")){
            System.out.println("Loading not available staff for trip...");
            ArrayList<String> staff = null;
            try {
                staff = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String dep = null;
            try {
                dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.findNotAvailableStaff(staff, dep, com));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("findNotAvailableChild")){
            System.out.println("Loading not available children for trip...");
            ArrayList<String> child = null;
            try {
                child = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String dep = null;
            try {
                dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.findNotAvailableStaff(child, dep, com));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
            try {
                String depFrom = (String)inputFromClient.readObject();
                String dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                String accomodation = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                String arrTo = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.loadWhoTrip(depFrom, dep, com, accomodation, arr, arrTo));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("loadBusTrip")){
            System.out.println("Loading bus to this trip...");
            try {
                String depFrom = (String)inputFromClient.readObject();
                String dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                String accomodation = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                String arrTo = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.loadBusTrip(depFrom, dep, com, accomodation, arr, arrTo));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("loadSolution")) {
            System.out.println("Loading solution...");
            try {
                String depFrom = (String)inputFromClient.readObject();
                String dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                String accomodation = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                String arrTo = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.loadSolution(depFrom, dep, com, accomodation, arr, arrTo));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("findParticipantsOnWrongBus")) {
            System.out.println("Searching for participants on wrong bus...");
            ArrayList<String> participants = null;
            try {
                participants = (ArrayList<String>) inputFromClient.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String selectedBus;
            try {
                selectedBus = (String)inputFromClient.readObject();
                String depFrom = (String)inputFromClient.readObject();
                String dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                String accomodation = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                String arrTo = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.findParticipantOnWrongBus(participants, selectedBus, depFrom, dep, com, accomodation, arr, arrTo));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
            String selectedBus;
            try {
                selectedBus = (String)inputFromClient.readObject();
                String depFrom = (String)inputFromClient.readObject();
                String dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                String accomodation = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                String arrTo = (String)inputFromClient.readObject();
                outputToClient.writeObject(impl.findMissingParticipantsOnThisBus(peopleOnWrongBus, selectedChild, selectedBus, depFrom, dep, com, accomodation, arr, arrTo));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("loadMissing")){
            System.out.println("Loading missing participants on this bus...");

            try {
                ArrayList<String> missing = (ArrayList<String>) inputFromClient.readObject();
                String selectedBus = (String) inputFromClient.readObject();
                String depFrom = (String) inputFromClient.readObject();
                String dep = (String) inputFromClient.readObject();
                String com = (String) inputFromClient.readObject();
                String accomodation = (String) inputFromClient.readObject();
                String arr = (String) inputFromClient.readObject();
                String arrTo = (String) inputFromClient.readObject();
                outputToClient.writeObject(impl.loadMissing(missing, selectedBus, depFrom, dep, com, accomodation, arr, arrTo));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("associateBusToParticipants")){
            System.out.println("Associating participants to bus...");
            try {
                ArrayList<String> children = (ArrayList<String>) inputFromClient.readObject();
                int totChildren = inputFromClient.readInt();
                ArrayList<String> staff = (ArrayList<String>) inputFromClient.readObject();
                int totStaff = inputFromClient.readInt();
                String depFrom = (String) inputFromClient.readObject();
                String dep = (String) inputFromClient.readObject();
                String com = (String) inputFromClient.readObject();
                String accomodation = (String) inputFromClient.readObject();
                String arr = (String) inputFromClient.readObject();
                String arrTo = (String) inputFromClient.readObject();
                outputToClient.writeObject(impl.associateBusToParticipants(children, totChildren, staff, totStaff, depFrom, dep, com, accomodation, arr, arrTo));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("makeIsHereTrue")){
            try {
                String selectedBus = (String)inputFromClient.readObject();
                String depFrom = (String)inputFromClient.readObject();
                String dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                String accomodation = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                String arrTo = (String)inputFromClient.readObject();
                impl.makeIsHereTrue(selectedBus, depFrom, dep, com, accomodation, arr, arrTo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else if(line.equals("makeIsHereFalse")){
            try {
                String depFrom = (String)inputFromClient.readObject();
                String dep = (String)inputFromClient.readObject();
                String com = (String)inputFromClient.readObject();
                String accomodation = (String)inputFromClient.readObject();
                String arr = (String)inputFromClient.readObject();
                String arrTo = (String)inputFromClient.readObject();
                impl.makeIsHereFalse(depFrom, dep, com, accomodation, arr, arrTo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }


        return false;
    }
}
