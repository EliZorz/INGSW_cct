package application.socket.server;

import application.details.*;
import application.rmi.server.ServerImpl;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


public class SocketThread extends Thread implements Runnable {

    private ObjectOutputStream outputToClient;
    private ObjectInputStream inputFromClient;
    private Socket s = null;
    private ServerImpl impl;
    private int counter;


    public SocketThread(Socket s, ServerImpl impl, int counter) {
        this.impl = impl;
        this.s = s;
        this.counter = counter;
    }


    boolean reply = false;

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
            for(;;) {
                System.out.println("Ready to receive a message");
                String line = (String) inputFromClient.readUnshared();


                    System.out.println("Received " + line + " from client # " + counter + "");

                    reply = doAction(line);  //passo il messaggio al doAction che decide cosa fare

                    System.out.println("Sending back : " + reply);
                    outputToClient.writeUnshared(reply);
                    outputToClient.flush();
                    System.out.println("Reply sent.");
                    outputToClient.reset();

            }

        } catch (Exception e) {
            String threadName = this.getName();
            System.out.println("EOF: " + threadName + " terminated");
            e.printStackTrace();
            try {
                s.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private boolean doAction(String line) throws IOException {

        switch (line) {

            //LOGIN AND LOGOUT ----------------------------------------------------
            case "login": {
                System.out.println("Logging in...");
                try {
                    String usr = (String) inputFromClient.readUnshared();
                    String pwd = (String) inputFromClient.readUnshared();
                    reply = impl.funzLog(usr, pwd);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }


            case "bye" : {
                System.out.println("Logging out");
                s.close();
                System.out.println("Socket closed. Bye bye.");
            }


            //CHILDREN -----------------------------------------------------------------------
            case "loadChild": {
                System.out.println("Loading children...");
                ArrayList<ChildDbDetails> isLoadedal = impl.loadData();
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "addChild": {
                System.out.println("Adding child...");
                try {
                    String name = (String) inputFromClient.readUnshared();
                    String surname = (String) inputFromClient.readUnshared();
                    String cf = (String) inputFromClient.readUnshared();
                    LocalDate bornOn = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    String bornWhere = (String) inputFromClient.readUnshared();
                    String residence = (String) inputFromClient.readUnshared();
                    String address = (String) inputFromClient.readUnshared();
                    String cap = (String) inputFromClient.readUnshared();
                    String province = (String) inputFromClient.readUnshared();
                    ArrayList<String> arrayListToReturn = new ArrayList<>();
                    try {
                        Object loaded = inputFromClient.readUnshared();
                        if (loaded instanceof ArrayList<?>) {
                            //get list
                            ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                            if (loadedAl.size() > 0) {
                                for (Object element : loadedAl) {
                                    if (element instanceof String) {
                                        String myElement = (String) element;
                                        arrayListToReturn.add(myElement);
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    String nameContact = (String) inputFromClient.readUnshared();
                    String surnameContact = (String) inputFromClient.readUnshared();
                    String cfContact = (String) inputFromClient.readUnshared();
                    String mailContact = (String) inputFromClient.readUnshared();
                    String telContact = (String) inputFromClient.readUnshared();
                    LocalDate birthContact = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    String bornWhereContact = (String) inputFromClient.readUnshared();
                    String addressContact = (String) inputFromClient.readUnshared();
                    String capContact = (String) inputFromClient.readUnshared();
                    String provinceContact = (String) inputFromClient.readUnshared();
                    boolean isDoc = (boolean) inputFromClient.readUnshared();
                    boolean isGuardian = (boolean) inputFromClient.readUnshared();
                    boolean isContact = (boolean) inputFromClient.readUnshared();

                    reply = impl.addData(name, surname, cf, bornOn, bornWhere, residence, address, cap, province, arrayListToReturn, nameContact, surnameContact, cfContact, mailContact, telContact, birthContact, bornWhereContact, addressContact, capContact, provinceContact, isDoc, isGuardian, isContact);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "updateChild": {
                System.out.println("Updating child...");
                try {
                    String name = (String) inputFromClient.readUnshared();
                    String surname = (String) inputFromClient.readUnshared();
                    String oldCf = (String) inputFromClient.readUnshared();
                    String cf = (String) inputFromClient.readUnshared();
                    LocalDate bornOn = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    String bornWhere = (String) inputFromClient.readUnshared();
                    String residence = (String) inputFromClient.readUnshared();
                    String address = (String) inputFromClient.readUnshared();
                    String cap = (String) inputFromClient.readUnshared();
                    String province = (String) inputFromClient.readUnshared();
                    ArrayList<String> arrayListToReturn = new ArrayList<>();
                    try {
                        Object loaded = inputFromClient.readUnshared();
                        if (loaded instanceof ArrayList<?>) {
                            //get list
                            ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                            if (loadedAl.size() > 0) {
                                for (Object element : loadedAl) {
                                    if (element instanceof String) {
                                        String myElement = (String) element;
                                        arrayListToReturn.add(myElement);
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    reply = impl.updateChild(name, surname, oldCf, cf, bornOn, bornWhere, residence, address, cap, province, arrayListToReturn);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "deleteChild": {
                System.out.println("Deleting child...");
                try {
                    String cf = (String) inputFromClient.readUnshared();
                    reply = impl.deleteChild(cf);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "addContact": {
                System.out.println("Adding contact...");
                try {
                    ArrayList<String> arrayListToReturn = new ArrayList<>();
                    try {
                        Object loaded = inputFromClient.readUnshared();
                        if (loaded instanceof ArrayList<?>) {
                            //get list
                            ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                            if (loadedAl.size() > 0) {
                                for (Object element : loadedAl) {
                                    if (element instanceof String) {
                                        String myElement = (String) element;
                                        arrayListToReturn.add(myElement);
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    String surname = (String) inputFromClient.readUnshared();
                    String name = (String) inputFromClient.readUnshared();
                    String cf = (String) inputFromClient.readUnshared();
                    String mail = (String) inputFromClient.readUnshared();
                    String tel = (String) inputFromClient.readUnshared();
                    LocalDate birthday = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    String bornWhere = (String) inputFromClient.readUnshared();
                    String address = (String) inputFromClient.readUnshared();
                    String cap = (String) inputFromClient.readUnshared();
                    String province = (String) inputFromClient.readUnshared();
                    boolean isDoc = (boolean) inputFromClient.readUnshared();
                    boolean isGuardian = (boolean) inputFromClient.readUnshared();
                    boolean isContact = (boolean) inputFromClient.readUnshared();
                    reply = impl.addContact(arrayListToReturn, surname, name, cf, mail, tel, birthday, bornWhere, address, cap, province, isDoc, isGuardian, isContact);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "deleteContact": {
                try {
                    String oldcf = (String) inputFromClient.readUnshared();
                    reply = impl.deleteContact(oldcf);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "updateContact": {
                try {
                    String name = (String) inputFromClient.readUnshared();
                    String surname = (String) inputFromClient.readUnshared();
                    String oldcf = (String) inputFromClient.readUnshared();
                    String cf = (String) inputFromClient.readUnshared();
                    String mail = (String) inputFromClient.readUnshared();
                    String tel = (String) inputFromClient.readUnshared();
                    LocalDate birthday = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    String bornWhere = (String) inputFromClient.readUnshared();
                    String address = (String) inputFromClient.readUnshared();
                    String cap = (String) inputFromClient.readUnshared();
                    String province = (String) inputFromClient.readUnshared();
                    int isDoc = inputFromClient.read();
                    int isGuardian = inputFromClient.read();
                    int isContact = inputFromClient.read();
                    reply = impl.updateContact(name, surname, oldcf, cf, mail, tel, birthday, bornWhere, address, cap, province, isDoc, isGuardian, isContact);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "loadDataContacts": {
                System.out.println("Loading contacts...");
                String rif = null;
                try {
                    rif = (String) inputFromClient.readUnshared();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ArrayList<ContactsDbDetails> isLoadedal = impl.loadDataContacts(rif);
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }

            case "controllCF": {
                System.out.println("Controlling cf...");
                try {
                    String cf = (String) inputFromClient.readObject();
                    reply = impl.controllCF(cf);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }

            //STAFF -------------------------------------------------------------------------
            case "loadDataStaff": {
                System.out.println("Loading staff members...");
                ArrayList<StaffDbDetails> isLoadedal = impl.loadDataStaff();
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "addDataStaff": {
                System.out.println("Adding staff...");
                try {
                    String name = (String) inputFromClient.readUnshared();
                    String surname = (String) inputFromClient.readUnshared();
                    String cf = (String) inputFromClient.readUnshared();
                    String mail = (String) inputFromClient.readUnshared();
                    LocalDate date = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    String bornWhere = (String) inputFromClient.readUnshared();
                    String residence = (String) inputFromClient.readUnshared();
                    String address = (String) inputFromClient.readUnshared();
                    String cap = (String) inputFromClient.readUnshared();
                    String province = (String) inputFromClient.readUnshared();
                    ArrayList<String> arrayListToReturn = new ArrayList<>();
                    try {
                        Object loaded = inputFromClient.readUnshared();
                        if (loaded instanceof ArrayList<?>) {
                            //get list
                            ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                            if (loadedAl.size() > 0) {
                                for (Object element : loadedAl) {
                                    if (element instanceof String) {
                                        String myElement = (String) element;
                                        arrayListToReturn.add(myElement);
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    reply = impl.addDataStaff(name, surname, cf, mail, date, bornWhere, residence, address, cap, province, arrayListToReturn);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "deleteStaff": {
                System.out.println("Deleting staff...");
                try {
                    String cf = (String) inputFromClient.readUnshared();
                    reply = impl.deleteStaff(cf);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "updateStaff": {
                System.out.println("Updating staff...");
                try {
                    String name = (String) inputFromClient.readUnshared();
                    String surname = (String) inputFromClient.readUnshared();
                    String oldcf = (String) inputFromClient.readUnshared();
                    String cf = (String) inputFromClient.readUnshared();
                    String mail = (String) inputFromClient.readUnshared();
                    LocalDate date = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    String bornWhere = (String) inputFromClient.readUnshared();
                    String residence = (String) inputFromClient.readUnshared();
                    String address = (String) inputFromClient.readUnshared();
                    String cap = (String) inputFromClient.readUnshared();
                    String province = (String) inputFromClient.readUnshared();
                    ArrayList<String> arrayListToReturn = new ArrayList<>();
                    try {
                        Object loaded = inputFromClient.readUnshared();
                        if (loaded instanceof ArrayList<?>) {
                            //get list
                            ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                            if (loadedAl.size() > 0) {
                                for (Object element : loadedAl) {
                                    if (element instanceof String) {
                                        String myElement = (String) element;
                                        arrayListToReturn.add(myElement);
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    reply = impl.updateStaff(name, surname, oldcf, cf, mail, date, bornWhere, residence, address, cap, province, arrayListToReturn);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }


            //MENU BASE -------------------------------------------------------------------------------
            case "loadMenuBasic": {
                System.out.println("Loading basic menu...");
                ArrayList<DishesDbDetails> isLoadedal = impl.loadMenu();
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    System.out.println("Db empty. Please add before loading again.");
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    System.out.println("Db not empty. Here you go.");
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "deleteMenu": {
                System.out.println("Deleting menu...");
                try {
                    LocalDate date = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    reply = impl.deleteMenu(date);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "loadIngredients": {
                System.out.println("Loading ingredients...");
                ArrayList<IngredientsDbDetails> isLoadedal = impl.loadIngr();
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "searchIngredients": {
                System.out.println("Looking for ingredients...");
                String dish = null;
                try {
                    dish = (String) inputFromClient.readUnshared();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ArrayList<IngredientsDbDetails> ingrAl = impl.searchIngredients(dish);
                if (ingrAl == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(ingrAl);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "controllDate": {
                System.out.println("Controlling the date...");
                try {
                    LocalDate date = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    reply = impl.controllDate(date);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "addMenu": {
                System.out.println("adding the menu...");
                try {
                    String num = (String) inputFromClient.readUnshared();
                    String entree = (String) inputFromClient.readUnshared();
                    String main = (String) inputFromClient.readUnshared();
                    String dessert = (String) inputFromClient.readUnshared();
                    String side = (String) inputFromClient.readUnshared();
                    String drink = (String) inputFromClient.readUnshared();
                    LocalDate date = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    reply = impl.addMenu(num, entree, main, dessert, side, drink, date);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "updateMenu": {
                System.out.println("updating the menu...");
                try {
                    String num = (String) inputFromClient.readUnshared();
                    String entree = (String) inputFromClient.readUnshared();
                    String main = (String) inputFromClient.readUnshared();
                    String dessert = (String) inputFromClient.readUnshared();
                    String side = (String) inputFromClient.readUnshared();
                    String drink = (String) inputFromClient.readUnshared();
                    LocalDate date = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    LocalDate oldDate = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    reply = impl.updateMenu(num, entree, main, dessert, side, drink, date, oldDate);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "saveIngredients": {
                System.out.println("saving ingredients...");
                try {
                    String dish = (String) inputFromClient.readUnshared();
                    ArrayList<String> arrayListToReturn = new ArrayList<>();
                    try {
                        Object loaded = inputFromClient.readUnshared();
                        if (loaded instanceof ArrayList<?>) {
                            //get list
                            ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                            if (loadedAl.size() > 0) {
                                for (Object element : loadedAl) {
                                    if (element instanceof String) {
                                        String myElement = (String) element;
                                        arrayListToReturn.add(myElement);
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Input read. Calling saveIngredients");
                    reply = impl.saveIngredients(dish, arrayListToReturn);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }


            //MENU ------------------------------------------------------------------------------------------
            case "loadThisMenu" : {
                System.out.println("Loading this menu...");
                String dateString = null;
                ArrayList<DishesDbDetails> isLoadedal = new ArrayList<>();
                try {
                    dateString = (String) inputFromClient.readUnshared();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if(dateString != null) {
                    LocalDate date = LocalDate.parse(dateString);
                    isLoadedal = impl.loadThisMenu(date);
                }

                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }

            case "loadIngredientsForThisDay" : {
                System.out.println("Loading ingredients for given day...");
                String dateString = null;
                ArrayList<IngredientsDbDetails> isLoadedal = new ArrayList<>();
                try {
                    dateString = (String) inputFromClient.readUnshared();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if(dateString != null) {
                    LocalDate date = LocalDate.parse(dateString);
                    isLoadedal = impl.loadIngr(date);
                }
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }

            //SUPPLIER ------------------------------------------------------------
            case "loadSuppliers": {
                System.out.println("Loading suppliers...");
                ArrayList<SupplierDbDetails> isLoadedal = impl.loadDataSuppliers();
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "addSupplier": {
                System.out.println("Adding supplier...");
                try {
                    String name = (String) inputFromClient.readUnshared();
                    String piva = (String) inputFromClient.readUnshared();
                    String mail = (String) inputFromClient.readUnshared();
                    String tel = (String) inputFromClient.readUnshared();
                    String address = (String) inputFromClient.readUnshared();
                    String cap = (String) inputFromClient.readUnshared();
                    String province = (String) inputFromClient.readUnshared();
                    reply = impl.addDataSupplier(name, piva, mail, tel, address, cap, province);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "updateSupplier": {
                System.out.println("Updating supplier...");
                try {
                    String name = (String) inputFromClient.readUnshared();
                    String oldPiva = (String) inputFromClient.readUnshared();
                    String piva = (String) inputFromClient.readUnshared();
                    String mail = (String) inputFromClient.readUnshared();
                    String tel = (String) inputFromClient.readUnshared();
                    String address = (String) inputFromClient.readUnshared();
                    String cap = (String) inputFromClient.readUnshared();
                    String province = (String) inputFromClient.readUnshared();
                    reply = impl.updateSupplier(name, oldPiva, piva, mail, tel, address, cap, province);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "loadDataIngr": {
                System.out.println("Loading ingredients...");
                String rif = null;
                try {
                    rif = (String) inputFromClient.readUnshared();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ArrayList<CodRifChildDbDetails> isLoadedal = impl.loadDataIngr(rif);
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "addIngredientSupplier": {
                System.out.println("Adding ingredient...");
                try {
                    String ingr = (String) inputFromClient.readUnshared();
                    String selectedSupplier = (String) inputFromClient.readUnshared();
                    reply = impl.addIngrToDb(ingr, selectedSupplier);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;

            }
            case "loadMenuWithThisSupplier":{
                System.out.println("Loading these menus...");
                String rif = null;
                try {
                    rif = (String) inputFromClient.readUnshared();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ArrayList<DishesDbDetails> isLoadedal = impl.loadMenuWithThisSupplier(rif);
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "loadNoIngr": {
                System.out.println("Loading no available ingredients...");
                String rif = null;
                try {
                    rif = (String) inputFromClient.readUnshared();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ArrayList<IngredientsDbDetails> isLoadedal = impl.loadNoIngr(rif);
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "deleteSupplier": {
                System.out.println("Deleting supplier...");
                try {
                    String piva = (String) inputFromClient.readUnshared();
                    ArrayList<IngredientsDbDetails> arrayListToReturn = new ArrayList<>();
                    try {
                        Object loaded = inputFromClient.readUnshared();
                        if (loaded instanceof ArrayList<?>) {
                            //get list
                            ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                            if (loadedAl.size() > 0) {
                                for (Object element : loadedAl) {
                                    if (element instanceof IngredientsDbDetails) {
                                        IngredientsDbDetails myElement = (IngredientsDbDetails) element;
                                        arrayListToReturn.add(myElement);
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    reply = impl.deleteSupplier(piva, arrayListToReturn);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return reply;
            }


            //COACH OPERATOR -----------------------------------------------------------
            case "loadCoachOperator": {
                System.out.println("Loading coach operator...");
                ArrayList<SupplierDbDetails> isLoadedal = impl.loadDataCoachOperator();
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "addCoachOperator": {
                System.out.println("Adding coach operator...");
                try {
                    String name = (String) inputFromClient.readUnshared();
                    String piva = (String) inputFromClient.readUnshared();
                    String mail = (String) inputFromClient.readUnshared();
                    String tel = (String) inputFromClient.readUnshared();
                    String address = (String) inputFromClient.readUnshared();
                    String cap = (String) inputFromClient.readUnshared();
                    String province = (String) inputFromClient.readUnshared();
                    reply = impl.addDataCoachOperator(name, piva, mail, tel, address, cap, province);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "updateCoachOperator": {
                System.out.println("Updating coach operator...");
                try {
                    String name = (String) inputFromClient.readUnshared();
                    String oldPiva = (String) inputFromClient.readUnshared();
                    String piva = (String) inputFromClient.readUnshared();
                    String mail = (String) inputFromClient.readUnshared();
                    String tel = (String) inputFromClient.readUnshared();
                    String address = (String) inputFromClient.readUnshared();
                    String cap = (String) inputFromClient.readUnshared();
                    String province = (String) inputFromClient.readUnshared();
                    reply = impl.updateCoachOperator(name, oldPiva, piva, mail, tel, address, cap, province);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "addBusToDb": {
                System.out.println("Adding bus...");
                try {
                    String plate = (String) inputFromClient.readUnshared();
                    int capacity = (int) inputFromClient.readUnshared();
                    String selectedSupplier = (String) inputFromClient.readUnshared();
                    reply = impl.addBusToDb(plate, capacity, selectedSupplier);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }

            case "deleteCoachOperator": {
                System.out.println("Deleting coach operator...");
                try {
                    String piva = (String) inputFromClient.readUnshared();
                    reply = impl.deleteCoachOperator(piva);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return reply;
            }

            case "deleteCoachOperatorBus": {
                System.out.println("Deleting bus related to coach operator...");
                try {
                    String plate = (String) inputFromClient.readUnshared();
                    reply = impl.deleteCoachOperatorBus(plate);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return reply;
            }

            //SPECIAL MENU --------------------------------------------------------------
            case "addSpecialMenu": {
                System.out.println("Adding special menu...");
                try {
                    String entree = (String) inputFromClient.readUnshared();
                    String main = (String) inputFromClient.readUnshared();
                    String dessert = (String) inputFromClient.readUnshared();
                    String side = (String) inputFromClient.readUnshared();
                    String drink = (String) inputFromClient.readUnshared();
                    LocalDate date = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    SpecialDbDetails special = null;
                    try {
                        special = (SpecialDbDetails) inputFromClient.readUnshared();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    reply = impl.addSpecialMenu(entree, main, dessert, side, drink, date, special);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "loadAllergicalInterni": {
                System.out.println("Loading allergical interni...");
                String rif = null;
                try {
                    rif = (String) inputFromClient.readUnshared();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ArrayList<SpecialDbDetails> isLoadedal = impl.loadInterniWithAllergies(LocalDate.parse(rif));
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "deleteSpecialMenu": {
                System.out.println("Deleting special menu....");
                try {
                    LocalDate date = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    String FC = (String) inputFromClient.readUnshared();
                    String allergies = (String) inputFromClient.readUnshared();
                    reply = impl.deleteSpecialMenu(date, FC, allergies);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "loadSpecialMenu": {
                ArrayList<SpecialMenuDbDetails> isLoadedal = impl.loadSpecialMenu();
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "updateSpecialMenu": {
                System.out.println("Updating special menu...");
                try {
                    String entree = (String) inputFromClient.readUnshared();
                    String main = (String) inputFromClient.readUnshared();
                    String dessert = (String) inputFromClient.readUnshared();
                    String side = (String) inputFromClient.readUnshared();
                    String drink = (String) inputFromClient.readUnshared();
                    LocalDate date = LocalDate.parse((CharSequence) inputFromClient.readUnshared());
                    SpecialDbDetails special = null;
                    try {
                        special = (SpecialDbDetails) inputFromClient.readUnshared();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    reply = impl.updateSpecialMenu(entree, main, dessert, side, drink, date, special);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }


            //TRIP -------------------------------------------

            case "zeroActualParticipants" : {
                System.out.println("Deleting people here....");
                try {
                    String plate = (String) inputFromClient.readUnshared();
                    reply = impl.zeroActualParticipants(plate);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }

            case "deleteIsHere" : {
                System.out.println("Deleting people here....");
                try {
                    String plate = (String) inputFromClient.readUnshared();
                    reply = impl.deleteIsHere(plate);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }

            case "deleteFromGitaHasBus": {
                System.out.println("Deleting from gita_has_bus....");
                try {
                    String plate = (String) inputFromClient.readUnshared();
                    impl.deleteFromGitaHasBus(plate);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

            case "loadDataBus" : {
                System.out.println("Loading data bus...");
                String rif = null;
                try {
                    rif = (String) inputFromClient.readUnshared();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ArrayList<BusPlateCapacityDbDetails> isLoadedal = impl.loadDataBus(rif);
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }

            case "loadDataTrip": {
                System.out.println("Loading data trip...");
                ArrayList<TripTableDbDetails> isLoadedal = impl.loadDataTrip();
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "deleteTrip": {
                System.out.println("Deleting supplier...");
                try {
                    String dep = (String) inputFromClient.readUnshared();
                    String dateDep = (String) inputFromClient.readUnshared();
                    String dateCom = (String) inputFromClient.readUnshared();
                    String staying = (String) inputFromClient.readUnshared();
                    String dateArr = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    reply = impl.deleteTrip(dep, dateDep, dateCom, staying, dateArr, arr);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "loadChildTrip": {
                System.out.println("Loading child for trip...");
                ArrayList<ChildTripDbDetails> isLoadedal = impl.loadChildTrip();
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "loadStaffTrip": {
                System.out.println("Loading staff for trip...");
                ArrayList<StaffTripDbDetails> isLoadedal = impl.loadStaffTrip();
                if (isLoadedal == null) {
                    outputToClient.writeUnshared(true);
                    outputToClient.flush();
                    reply = false;
                } else {
                    outputToClient.writeUnshared(false);
                    outputToClient.flush();
                    outputToClient.writeUnshared(isLoadedal);
                    outputToClient.flush();
                    reply = true;
                }
                return reply;
            }
            case "addTrip": {
                System.out.println("Adding trip...");
                int[] returnedPart;
                ArrayList<String> childrenArrayList = new ArrayList<>();
                try {
                    Object loaded = inputFromClient.readUnshared();
                    if (loaded instanceof ArrayList<?>) {
                        //get list
                        ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                        if (loadedAl.size() > 0) {
                            for (Object element : loadedAl) {
                                if (element instanceof String) {
                                    String myElement = (String) element;
                                    childrenArrayList.add(myElement);
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    reply = false;
                }
                ArrayList<String> staffArrayList = new ArrayList<>();
                try {
                    Object loaded = inputFromClient.readUnshared();
                    if (loaded instanceof ArrayList<?>) {
                        //get list
                        ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                        if (loadedAl.size() > 0) {
                            for (Object element : loadedAl) {
                                if (element instanceof String) {
                                    String myElement = (String) element;
                                    staffArrayList.add(myElement);
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    reply = false;
                }
                try {
                    String timeDep = (String) inputFromClient.readUnshared();
                    String timeArr = (String) inputFromClient.readUnshared();
                    String timeCom = (String) inputFromClient.readUnshared();
                    String depFrom = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    returnedPart = impl.addTrip(childrenArrayList, staffArrayList, timeDep, timeArr, timeCom, depFrom, arrTo, arr);
                    for (int i = 0; i < 2; i++) {
                        outputToClient.writeInt(returnedPart[i]);
                        reply = true;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    reply = false;
                }
                return reply;
            }
            case "loadTripSelectedChildren": {
                System.out.println("Loading effective child for trip...");
                try {
                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    ArrayList<ChildSelectedTripDbDetails> loadedAl = impl.loadTripSelectedChildren(depFrom, dep, com, accomodation, arr, arrTo);
                    if (loadedAl == null) {
                        outputToClient.writeUnshared(true);
                        outputToClient.flush();
                        reply = false;
                    } else {
                        outputToClient.writeUnshared(false);
                        outputToClient.flush();
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "loadTripSelectedStaff": {
                System.out.println("Loading effective staff for trip...");
                try {
                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    ArrayList<StaffSelectedTripDbDetails> loadedAl = impl.loadTripSelectedStaff(depFrom, dep, com, accomodation, arr, arrTo);
                    if (loadedAl == null) {
                        outputToClient.writeUnshared(true);
                        outputToClient.flush();
                        reply = false;
                    } else {
                        outputToClient.writeUnshared(false);
                        outputToClient.flush();
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "findNotAvailableStaff": {
                System.out.println("Loading not available staff for trip...");
                ArrayList<String> arrayListToReturn = new ArrayList<>();
                try {
                    Object loaded = inputFromClient.readUnshared();
                    if (loaded instanceof ArrayList<?>) {
                        //get list
                        ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                        if (loadedAl.size() > 0) {
                            for (Object element : loadedAl) {
                                if (element instanceof String) {
                                    String myElement = (String) element;
                                    arrayListToReturn.add(myElement);
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String dep;
                try {
                    dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    ArrayList<CodRifChildDbDetails> loadedAl = impl.findNotAvailableStaff(arrayListToReturn, dep, com);
                    if (!loadedAl.isEmpty()) {
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    } else {
                        reply = false;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "findNotAvailableChild": {
                System.out.println("Loading not available children for trip...");
                ArrayList<String> arrayListToReturn = new ArrayList<>();
                try {
                    Object loaded = inputFromClient.readUnshared();
                    if (loaded instanceof ArrayList<?>) {
                        //get list
                        ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                        if (loadedAl.size() > 0) {
                            for (Object element : loadedAl) {
                                if (element instanceof String) {
                                    String myElement = (String) element;
                                    arrayListToReturn.add(myElement);
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String dep;
                try {
                    dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    ArrayList<CodRifChildDbDetails> loadedAl = impl.findNotAvailableChild(arrayListToReturn, dep, com);
                    if (!loadedAl.isEmpty()) {
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    } else {
                        reply = false;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "howManyActualParticipants": {
                System.out.println("Calculating number of participants...");
                int[] returnedPart;
                ArrayList<String> childrenArrayList = new ArrayList<>();
                try {
                    Object loaded = inputFromClient.readUnshared();
                    if (loaded instanceof ArrayList<?>) {
                        //get list
                        ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                        if (loadedAl.size() > 0) {
                            for (Object element : loadedAl) {
                                if (element instanceof String) {
                                    String myElement = (String) element;
                                    childrenArrayList.add(myElement);
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(childrenArrayList.isEmpty())
                    reply = false;
                ArrayList<String> staffArrayList = new ArrayList<>();
                try {
                    Object loaded = inputFromClient.readUnshared();
                    if (loaded instanceof ArrayList<?>) {
                        //get list
                        ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                        if (loadedAl.size() > 0) {
                            for (Object element : loadedAl) {
                                if (element instanceof String) {
                                    String myElement = (String) element;
                                    staffArrayList.add(myElement);
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(staffArrayList.isEmpty())
                    reply = false;
                returnedPart = impl.howManyActualParticipants(childrenArrayList, staffArrayList);
                for (int i = 0; i < 2; i++) {
                    outputToClient.writeInt(returnedPart[i]);
                    reply = true;
                }
                return reply;
            }
            case "loadWhoTrip": {
                System.out.println("Loading who participates to this trip...");
                try {
                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    ArrayList<ChildSelectedTripDbDetails> loadedAl = impl.loadWhoTrip(depFrom, dep, com, accomodation, arr, arrTo);
                    if (loadedAl == null) {
                        outputToClient.writeUnshared(true);
                        outputToClient.flush();
                        reply = false;
                    } else {
                        outputToClient.writeUnshared(false);
                        outputToClient.flush();
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    }
                    return reply;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "loadBusTrip": {
                System.out.println("Loading bus to this trip...");
                try {
                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    ArrayList<CodRifChildDbDetails> loadedAl = impl.loadBusTrip(depFrom, dep, com, accomodation, arr, arrTo);
                    if (loadedAl == null) {
                        outputToClient.writeUnshared(true);
                        outputToClient.flush();
                        reply = false;
                    } else {
                        outputToClient.writeUnshared(false);
                        outputToClient.flush();
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    }
                    return reply;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "loadSolution": {
                System.out.println("Loading solution...");
                try {
                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    ArrayList<SolutionDbDetails> loadedAl = impl.loadSolution(depFrom, dep, com, accomodation, arr, arrTo);
                    if (loadedAl == null) {
                        outputToClient.writeUnshared(true);
                        outputToClient.flush();
                        reply = false;
                    } else {
                        outputToClient.writeUnshared(false);
                        outputToClient.flush();
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "findParticipantsOnWrongBus": {
                System.out.println("Searching for participants on wrong bus...");
                ArrayList<String> arrayListToReturn = new ArrayList<>();
                try {
                    Object loaded = inputFromClient.readUnshared();
                    if (loaded instanceof ArrayList<?>) {
                        //get list
                        ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                        if (loadedAl.size() > 0) {
                            for (Object element : loadedAl) {
                                if (element instanceof String) {
                                    String myElement = (String) element;
                                    arrayListToReturn.add(myElement);
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(arrayListToReturn.isEmpty())
                    reply = false;
                try {
                    String selectedBus = (String) inputFromClient.readUnshared();
                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    ArrayList<String> loadedAl = impl.findParticipantOnWrongBus(arrayListToReturn, selectedBus, depFrom, dep, com, accomodation, arr, arrTo);
                    if (!loadedAl.isEmpty()) {
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    } else {
                        reply = false;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "findMissingParticipantsOnThisBus": {
                System.out.println("Searching for missing participants on this bus...");
                ArrayList<String> peopleOnWrongBus = new ArrayList<>();
                try {
                    Object loaded = inputFromClient.readUnshared();
                    if (loaded instanceof ArrayList<?>) {
                        //get list
                        ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                        if (loadedAl.size() > 0) {
                            for (Object element : loadedAl) {
                                if (element instanceof String) {
                                    String myElement = (String) element;
                                    peopleOnWrongBus.add(myElement);
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ArrayList<String> selectedChild = new ArrayList<>();
                try {
                    Object loaded = inputFromClient.readUnshared();
                    if (loaded instanceof ArrayList<?>) {
                        //get list
                        ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                        if (loadedAl.size() > 0) {
                            for (Object element : loadedAl) {
                                if (element instanceof String) {
                                    String myElement = (String) element;
                                    selectedChild.add(myElement);
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(selectedChild.isEmpty())
                    reply = false;
                String selectedBus;
                try {
                    selectedBus = (String) inputFromClient.readUnshared();
                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    ArrayList<String> loadedAl = impl.findMissingParticipantsOnThisBus(peopleOnWrongBus, selectedChild, selectedBus, depFrom, dep, com, accomodation, arr, arrTo);
                    if (!loadedAl.isEmpty()) {
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    } else {
                        reply = false;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "loadMissing": {
                System.out.println("Loading missing participants on this bus...");

                try {
                    ArrayList<String> arrayListToReturn = new ArrayList<>();
                    try {
                        Object loaded = inputFromClient.readUnshared();
                        if (loaded instanceof ArrayList<?>) {
                            //get list
                            ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                            if (loadedAl.size() > 0) {
                                for (Object element : loadedAl) {
                                    if (element instanceof String) {
                                        String myElement = (String) element;
                                        arrayListToReturn.add(myElement);
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    String selectedBus = (String) inputFromClient.readUnshared();
                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    ArrayList<ChildSelectedTripDbDetails> loadedAl = impl.loadMissing(arrayListToReturn, selectedBus, depFrom, dep, com, accomodation, arr, arrTo);
                    if (loadedAl == null) {
                        outputToClient.writeUnshared(true);
                        outputToClient.flush();
                        reply = false;
                    } else {
                        outputToClient.writeUnshared(false);
                        outputToClient.flush();
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "associateBusToParticipants": {
                System.out.println("Associating participants to bus...");
                try {
                    ArrayList<String> childrenArrayList = new ArrayList<>();
                    try {
                        Object loaded = inputFromClient.readUnshared();
                        if (loaded instanceof ArrayList<?>) {
                            //get list
                            ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                            if (loadedAl.size() > 0) {
                                for (Object element : loadedAl) {
                                    if (element instanceof String) {
                                        String myElement = (String) element;
                                        childrenArrayList.add(myElement);
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(childrenArrayList.isEmpty())
                        reply = false;

                    int totChildren = inputFromClient.readInt();

                    ArrayList<String> staffArrayList = new ArrayList<>();
                    try {
                        Object loaded = inputFromClient.readUnshared();
                        if (loaded instanceof ArrayList<?>) {
                            //get list
                            ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                            if (loadedAl.size() > 0) {
                                for (Object element : loadedAl) {
                                    if (element instanceof String) {
                                        String myElement = (String) element;
                                        staffArrayList.add(myElement);
                                    }
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(staffArrayList.isEmpty())
                        reply = false;

                    int totStaff = inputFromClient.readInt();

                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    HashMap<String, ArrayList<String>> loadedAl = impl.associateBusToParticipants(childrenArrayList, totChildren, staffArrayList, totStaff, depFrom, dep, com, accomodation, arr, arrTo);
                    if (!loadedAl.isEmpty()) {
                        outputToClient.writeUnshared(loadedAl);
                        outputToClient.flush();
                        reply = true;
                    } else {
                        reply = false;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "makeIsHereTrue": {
                try {
                    String selectedBus = (String) inputFromClient.readUnshared();
                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    impl.makeIsHereTrue(selectedBus, depFrom, dep, com, accomodation, arr, arrTo);
                    reply = true;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
            case "makeIsHereFalse": {
                try {
                    String depFrom = (String) inputFromClient.readUnshared();
                    String dep = (String) inputFromClient.readUnshared();
                    String com = (String) inputFromClient.readUnshared();
                    String accomodation = (String) inputFromClient.readUnshared();
                    String arr = (String) inputFromClient.readUnshared();
                    String arrTo = (String) inputFromClient.readUnshared();
                    impl.makeIsHereFalse(depFrom, dep, com, accomodation, arr, arrTo);
                    reply = true;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return reply;
            }
        }
        return false;
    }
}
