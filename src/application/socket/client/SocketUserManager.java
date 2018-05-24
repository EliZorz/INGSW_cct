package application.socket.client;

import application.Interfaces.UserRemote;
import application.details.*;
import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


public class SocketUserManager implements UserRemote {
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;


    SocketUserManager(Socket s) {
        try{
            toServer = new ObjectOutputStream(s.getOutputStream());
            toServer.flush();
            fromServer = new ObjectInputStream(s.getInputStream());
            System.out.println("Created streams I/O for socket user manager");
        }catch(Exception e){
            System.out.println("IO error in server thread");
        }

    }


    //LOGIN --------------------------------------------------------------------------------------------------
    @Override
    public boolean funzLog(String usr, String pwd) throws RemoteException {
        boolean isLogged = false;
        try{
            toServer.writeUnshared("login");
            toServer.flush();
            toServer.writeUnshared (usr);
            toServer.flush();
            toServer.writeUnshared (pwd);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            isLogged = (boolean) fromServer.readUnshared ();
            System.out.println("Has logged in? " +isLogged);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isLogged;
    }

    //LOGOUT -------------------------------------------------------------------------
    @Override
    public boolean logout(){
        try{
            toServer.writeUnshared("bye");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //CHILDREN ---------------------------------------------------------------------------
    @Override
    public ArrayList<ChildDbDetails> loadData() throws RemoteException {
        System.out.println("Loading from SockUsrMananger");
        boolean ok = false;
        ArrayList<ChildDbDetails> arrayListToReturn = new ArrayList<>();
        Object loaded = new Object();

        try{
            toServer.writeUnshared("loadChild");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                loaded = fromServer.readUnshared();
                System.out.println("Read array as Object");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (loaded instanceof ArrayList<?>) {
                //get list
                ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                if (loadedAl.size() > 0) {
                    for (Object element : loadedAl) {
                        if (element instanceof ChildDbDetails) {
                            ChildDbDetails myElement = (ChildDbDetails) element;
                            arrayListToReturn.add(myElement);
                            System.out.println(myElement.getCf());
                        }
                    }
                }
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public boolean addData(String name, String surname, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy, String nameContact, String surnameContact, String cfContact, String mailContact, String telContact, LocalDate birthdayContact, String bornWhereContact, String addressContact, String capContact, String provinceContact, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("addChild");
            toServer.flush();
            toServer.writeUnshared (name);
            toServer.flush();
            toServer.writeUnshared (surname);
            toServer.flush();
            toServer.writeUnshared (cf);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(bornOn));
            toServer.flush();
            toServer.writeUnshared (bornWhere);
            toServer.flush();
            toServer.writeUnshared (residence);
            toServer.flush();
            toServer.writeUnshared (address);
            toServer.flush();
            toServer.writeUnshared (cap);
            toServer.flush();
            toServer.writeUnshared (province);
            toServer.flush();
            toServer.writeUnshared (selectedAllergy);
            toServer.flush();
            toServer.writeUnshared (nameContact);
            toServer.flush();
            toServer.writeUnshared (surnameContact);
            toServer.flush();
            toServer.writeUnshared (cfContact);
            toServer.flush();
            toServer.writeUnshared (mailContact);
            toServer.flush();
            toServer.writeUnshared (telContact);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(birthdayContact));
            toServer.flush();
            toServer.writeUnshared (bornWhereContact);
            toServer.flush();
            toServer.writeUnshared (addressContact);
            toServer.flush();
            toServer.writeUnshared (capContact);
            toServer.flush();
            toServer.writeUnshared (provinceContact);
            toServer.flush();
            toServer.writeUnshared(isDoc);
            toServer.flush();
            toServer.writeUnshared(isGuardian);
            toServer.flush();
            toServer.writeUnshared(isContact);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;

    }

    @Override
    public boolean updateChild(String name, String surname, String oldcf, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException{
        boolean ok = false;
        try {
            toServer.writeUnshared("updateChild");
            toServer.flush();
            toServer.writeUnshared (name);
            toServer.flush();
            toServer.writeUnshared (surname);
            toServer.flush();
            toServer.writeUnshared (oldcf);
            toServer.flush();
            toServer.writeUnshared (cf);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(bornOn));
            toServer.flush();
            toServer.writeUnshared (bornWhere);
            toServer.flush();
            toServer.writeUnshared (residence);
            toServer.flush();
            toServer.writeUnshared (address);
            toServer.flush();
            toServer.writeUnshared (cap);
            toServer.flush();
            toServer.writeUnshared (province);
            toServer.flush();
            toServer.writeUnshared (selectedAllergy);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean deleteChild(String cf) throws RemoteException{
        boolean ok = false;
        try{
            toServer.writeUnshared ("deleteChild");
            toServer.flush();
            toServer.writeUnshared (cf);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadIngredients");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<IngredientsDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public boolean controllCF(String CF) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeObject("controllCF");
            toServer.flush();
            toServer.writeObject(CF);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }


    //CONTACT --------------------------------------------------------------------------------
    @Override
    public boolean addContact (ArrayList<String> selectedChild, String surname, String name, String cf, String mail, String tel, LocalDate birthday, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("addContact");
            toServer.flush();
            toServer.writeUnshared (selectedChild);
            toServer.flush();
            toServer.writeUnshared (surname);
            toServer.flush();
            toServer.writeUnshared (name);
            toServer.flush();
            toServer.writeUnshared (cf);
            toServer.flush();
            toServer.writeUnshared (mail);
            toServer.flush();
            toServer.writeUnshared (tel);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(birthday));
            toServer.flush();
            toServer.writeUnshared (bornWhere);
            toServer.flush();
            toServer.writeUnshared (address);
            toServer.flush();
            toServer.writeUnshared (cap);
            toServer.flush();
            toServer.writeUnshared (province);
            toServer.flush();
            toServer.writeUnshared(isDoc);
            toServer.flush();
            toServer.writeUnshared(isGuardian);
            toServer.flush();
            toServer.writeUnshared(isContact);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean deleteContact (String oldcfContact) throws RemoteException{
        boolean ok =false;
        try{
            toServer.writeUnshared ("deleteContact");
            toServer.flush();
            toServer.writeUnshared (oldcfContact);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean updateContact(String name, String surname, String oldcf, String cf, String mail, String tel, LocalDate bornOn, String bornWhere, String address, String cap, String province, int isDoc, int isGuardian, int isContact) throws RemoteException{
        boolean ok = false;
        try{
            toServer.writeUnshared ("updateContact");
            toServer.flush();
            toServer.writeUnshared (name);
            toServer.flush();
            toServer.writeUnshared (surname);
            toServer.flush();
            toServer.writeUnshared (oldcf);
            toServer.flush();
            toServer.writeUnshared (cf);
            toServer.flush();
            toServer.writeUnshared (mail);
            toServer.flush();
            toServer.writeUnshared (tel);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(bornOn));
            toServer.flush();
            toServer.writeUnshared (bornWhere);
            toServer.flush();
            toServer.writeUnshared (address);
            toServer.flush();
            toServer.writeUnshared (cap);
            toServer.flush();
            toServer.writeUnshared (province);
            toServer.flush();
            toServer.write(isDoc);
            toServer.flush();
            toServer.write(isGuardian);
            toServer.flush();
            toServer.write(isContact);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public ArrayList<ContactsDbDetails> loadDataContacts(String cfChild) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadDataContacts");
            toServer.flush();
            toServer.writeUnshared (cfChild);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<ContactsDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof ContactsDbDetails) {
                                ContactsDbDetails myElement = (ContactsDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }


    //STAFF ------------------------------------------------------------------------------------------
    @Override
    public ArrayList<StaffDbDetails> loadDataStaff() throws RemoteException {
        boolean ok = false;
        try {
            toServer.writeUnshared ("loadDataStaff");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<StaffDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof StaffDbDetails) {
                                StaffDbDetails myElement = (StaffDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public boolean addDataStaff(String name, String surname, String cf, String mail, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("addDataStaff");
            toServer.flush();
            toServer.writeUnshared (name);
            toServer.flush();
            toServer.writeUnshared (surname);
            toServer.flush();
            toServer.writeUnshared (cf);
            toServer.flush();
            toServer.writeUnshared (mail);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(birthday));
            toServer.flush();
            toServer.writeUnshared (bornWhere);
            toServer.flush();
            toServer.writeUnshared (residence);
            toServer.flush();
            toServer.writeUnshared (address);
            toServer.flush();
            toServer.writeUnshared (cap);
            toServer.flush();
            toServer.writeUnshared (province);
            toServer.flush();
            toServer.writeUnshared (selectedAllergy);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean deleteStaff(String cf) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("deleteStaff");
            toServer.flush();
            toServer.writeUnshared (cf);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean updateStaff(String name, String surname, String oldcf, String cf, String mail, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException{
        boolean ok = false;
        try{
            toServer.writeUnshared ("updateStaff");
            toServer.flush();
            toServer.writeUnshared (name);
            toServer.flush();
            toServer.writeUnshared (surname);
            toServer.flush();
            toServer.writeUnshared (oldcf);
            toServer.flush();
            toServer.writeUnshared (cf);
            toServer.flush();
            toServer.writeUnshared (mail);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(bornOn));
            toServer.flush();
            toServer.writeUnshared (bornWhere);
            toServer.flush();
            toServer.writeUnshared (residence);
            toServer.flush();
            toServer.writeUnshared (address);
            toServer.flush();
            toServer.writeUnshared (cap);
            toServer.flush();
            toServer.writeUnshared (province);
            toServer.flush();
            toServer.writeUnshared (selectedAllergy);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    //SUPPLIERS ------------------------------------------------------------------------
    @Override
    public ArrayList<SupplierDbDetails> loadDataSuppliers() throws RemoteException {
        boolean ok = false;
        try {
            toServer.writeUnshared ("loadSuppliers");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<SupplierDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof SupplierDbDetails) {
                                SupplierDbDetails myElement = (SupplierDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public boolean addDataSupplier(String name, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("addSupplier");
            toServer.flush();
            toServer.writeUnshared (name);
            toServer.flush();
            toServer.writeUnshared (piva);
            toServer.flush();
            toServer.writeUnshared (mail);
            toServer.flush();
            toServer.writeUnshared (tel);
            toServer.flush();
            toServer.writeUnshared (address);
            toServer.flush();
            toServer.writeUnshared (cap);
            toServer.flush();
            toServer.writeUnshared (province);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean updateSupplier(String name, String oldPiva, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("updateSupplier");
            toServer.flush();
            toServer.writeUnshared (name);
            toServer.flush();
            toServer.writeUnshared (oldPiva);
            toServer.flush();
            toServer.writeUnshared (piva);
            toServer.flush();
            toServer.writeUnshared (mail);
            toServer.flush();
            toServer.writeUnshared (tel);
            toServer.flush();
            toServer.writeUnshared (address);
            toServer.flush();
            toServer.writeUnshared (cap);
            toServer.flush();
            toServer.writeUnshared (province);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean deleteSupplier(String piva, ArrayList<IngredientsDbDetails> ingrNO) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("deleteSupplier");
            toServer.flush();
            toServer.writeUnshared (piva);
            toServer.flush();
            toServer.writeUnshared (ingrNO);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }



    @Override
    public ArrayList<CodRifChildDbDetails> loadDataIngr(String selectedSupplier) throws RemoteException {
        boolean ok = false;
        try {
            toServer.writeUnshared ("loadDataIngr");
            toServer.flush();
            toServer.writeUnshared (selectedSupplier);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<CodRifChildDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof CodRifChildDbDetails) {
                                CodRifChildDbDetails myElement = (CodRifChildDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public boolean addIngrToDb(String ingr, String selectedSupplier) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("addIngredientSupplier");
            toServer.flush();
            toServer.writeUnshared (ingr);
            toServer.flush();
            toServer.writeUnshared (selectedSupplier);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public ArrayList<DishesDbDetails> loadMenuWithThisSupplier(String selectedSupplier) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadMenuWithThisSupplier");
            toServer.flush();
            toServer.writeUnshared (selectedSupplier);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<DishesDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof DishesDbDetails) {
                                DishesDbDetails myElement = (DishesDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadNoIngr(String selectedSupplier) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadNoIngr");
            toServer.flush();
            toServer.writeUnshared (selectedSupplier);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<IngredientsDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }
    //COACH OPERATOR -------------------------------------------------------------------

    @Override
    public ArrayList<SupplierDbDetails> loadDataCoachOperator() throws RemoteException {
        boolean ok = false;
        try {
            toServer.writeUnshared ("loadCoachOperator");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<SupplierDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof SupplierDbDetails) {
                                SupplierDbDetails myElement = (SupplierDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public boolean addDataCoachOperator(String name, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("addCoachOperator");
            toServer.flush();
            toServer.writeUnshared (name);
            toServer.flush();
            toServer.writeUnshared (piva);
            toServer.flush();
            toServer.writeUnshared (mail);
            toServer.flush();
            toServer.writeUnshared (tel);
            toServer.flush();
            toServer.writeUnshared (address);
            toServer.flush();
            toServer.writeUnshared (cap);
            toServer.flush();
            toServer.writeUnshared (province);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean addBusToDb(String plate, int capacity, String selectedSupplier) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("addBusToDb");
            toServer.flush();
            toServer.writeUnshared (plate);
            toServer.flush();
            toServer.writeUnshared (capacity);
            toServer.flush();
            toServer.writeUnshared (selectedSupplier);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }


    @Override
    public boolean updateCoachOperator(String name, String oldPiva, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("updateCoachOperator");
            toServer.flush();
            toServer.writeUnshared (name);
            toServer.flush();
            toServer.writeUnshared (oldPiva);
            toServer.flush();
            toServer.writeUnshared (piva);
            toServer.flush();
            toServer.writeUnshared (mail);
            toServer.flush();
            toServer.writeUnshared (tel);
            toServer.flush();
            toServer.writeUnshared (address);
            toServer.flush();
            toServer.writeUnshared (cap);
            toServer.flush();
            toServer.writeUnshared (province);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean deleteCoachOperator(String piva) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("deleteCoachOperator");
            toServer.flush();
            toServer.writeUnshared (piva);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean deleteCoachOperatorBus(String plate) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("deleteCoachOperatorBus");
            toServer.flush();
            toServer.writeUnshared (plate);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    //MENU -------------------------------------------------------------------------------
    @Override
    public DishesDbDetails loadThisMenu(LocalDate date) throws RemoteException {
        boolean ok = false;
        DishesDbDetails menu = null;
        try {
            toServer.writeUnshared ("loadThisMenu");
            toServer.flush();
            toServer.writeUnshared(String.valueOf(date));
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                menu = (DishesDbDetails) fromServer.readUnshared();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return menu;
        }
        return null;        }

    @Override
    public ArrayList<IngredientsDbDetails> searchIngredients(String dish) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("searchIngredients");
            toServer.flush();
            toServer.writeUnshared (dish);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<IngredientsDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;       }

    @Override
    public ArrayList<IngredientsDbDetails> loadIngr(LocalDate day) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadIngredientsForThisDay");
            toServer.flush();
            toServer.writeUnshared(String.valueOf(day));
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<IngredientsDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public boolean updateMenu(String num, String entree, String main, String dessert, String side, String drink, LocalDate day, LocalDate oldDate) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("updateMenu");
            toServer.flush();
            toServer.writeUnshared (num);
            toServer.flush();
            toServer.writeUnshared (entree);
            toServer.flush();
            toServer.writeUnshared (main);
            toServer.flush();
            toServer.writeUnshared (dessert);
            toServer.flush();
            toServer.writeUnshared (side);
            toServer.flush();
            toServer.writeUnshared (drink);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(day));
            toServer.flush();
            toServer.writeUnshared (String.valueOf(oldDate));
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }


    @Override
    public ArrayList<DishesDbDetails> loadMenu() throws RemoteException {
        boolean ok = false;
        ArrayList<DishesDbDetails> arrayListToReturn = new ArrayList<>();
        try {
            toServer.writeUnshared ("loadMenuBasic");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded != null) {
                    if (loaded instanceof ArrayList<?>) {
                        //get list
                        ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                        if (loadedAl.size() > 0) {
                            for (Object element : loadedAl) {
                                if (element instanceof DishesDbDetails) {
                                    DishesDbDetails myElement = (DishesDbDetails) element;
                                    arrayListToReturn.add(myElement);
                                }
                            }
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok)
            return arrayListToReturn;

        return null;

    }


    @Override
    public boolean addMenu(String num, String entree, String mainCourse, String dessert, String sideDish, String drink, LocalDate date) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("addMenu");
            toServer.flush();
            toServer.writeUnshared (num);
            toServer.flush();
            toServer.writeUnshared (entree);
            toServer.flush();
            toServer.writeUnshared (mainCourse);
            toServer.flush();
            toServer.writeUnshared (dessert);
            toServer.flush();
            toServer.writeUnshared (sideDish);
            toServer.flush();
            toServer.writeUnshared (drink);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(date));
            toServer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean controllDate(LocalDate d) throws RemoteException {
        boolean ok = false;
        try {
            toServer.writeUnshared ("controllDate");
            toServer.flush();
            toServer.writeUnshared (String.valueOf(d));
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean deleteMenu(LocalDate d) throws RemoteException {
        boolean ok =false;
        try{
            toServer.writeUnshared ("deleteMenu");
            toServer.flush();
            toServer.writeUnshared (String.valueOf(d));
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public ArrayList<SpecialDbDetails> loadInterniWithAllergies(LocalDate date) throws RemoteException {
        boolean ok =false;
        try{
            toServer.writeUnshared ("loadAllergicalInterni");
            toServer.flush();
            toServer.writeUnshared (String.valueOf(date));
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<SpecialDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof SpecialDbDetails) {
                                SpecialDbDetails myElement = (SpecialDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public boolean saveIngredients(String dish, ArrayList<String> selectedIngredients) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("saveIngredients");
            toServer.flush();
            toServer.writeUnshared (dish);
            toServer.flush();
            toServer.writeUnshared (selectedIngredients);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }


    //SPECIAL MENU ------------------------------------------------------------------------------------
    @Override
    public ArrayList<SpecialMenuDbDetails> loadSpecialMenu() throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadSpecialMenu");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<SpecialMenuDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof SpecialMenuDbDetails) {
                                SpecialMenuDbDetails myElement = (SpecialMenuDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public boolean deleteSpecialMenu(LocalDate date, String FC, String allergies) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("deleteSpecialMenu");
            toServer.flush();
            toServer.writeUnshared (String.valueOf(date));
            toServer.flush();
            toServer.writeUnshared (FC);
            toServer.flush();
            toServer.writeUnshared (allergies);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean addSpecialMenu(String entree, String main, String dessert, String side, String drink, LocalDate date, SpecialDbDetails special) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("addSpecialMenu");
            toServer.flush();
            toServer.writeUnshared (entree);
            toServer.flush();
            toServer.writeUnshared (main);
            toServer.flush();
            toServer.writeUnshared (dessert);
            toServer.flush();
            toServer.writeUnshared (side);
            toServer.flush();
            toServer.writeUnshared (drink);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(date));
            toServer.flush();
            toServer.writeUnshared (special);
            toServer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean updateSpecialMenu(String entree, String main, String dessert, String side, String drink, LocalDate date, SpecialDbDetails special) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("updateSpecialMenu");
            toServer.flush();
            toServer.writeUnshared (entree);
            toServer.flush();
            toServer.writeUnshared (main);
            toServer.flush();
            toServer.writeUnshared (dessert);
            toServer.flush();
            toServer.writeUnshared (side);
            toServer.flush();
            toServer.writeUnshared (drink);
            toServer.flush();
            toServer.writeUnshared (String.valueOf(date));
            toServer.flush();
            toServer.writeUnshared (special);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    //TRIP -------------------------------------------------------------------------------
    @Override
    public boolean zeroActualParticipants(String plate) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared("zeroActualParticipants");
            toServer.flush();
            toServer.writeUnshared(plate);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean deleteIsHere(String plate) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared("deleteIsHere");
            toServer.flush();
            toServer.writeUnshared(plate);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public void deleteFromGitaHasBus(String plate) throws RemoteException {
        boolean ok;
        try{
            toServer.writeUnshared("deleteFromGitaHasBus");
            toServer.flush();
            toServer.writeUnshared(plate);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            if(ok)
                System.out.println("Read reply from server: deleted");
            else
                System.out.println("Read reply from server: not deleted. Problems upcoming.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<BusPlateCapacityDbDetails> loadDataBus(String selectedSupplier) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadDataBus");
            toServer.flush();
            toServer.writeUnshared(selectedSupplier);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<BusPlateCapacityDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof BusPlateCapacityDbDetails) {
                                BusPlateCapacityDbDetails myElement = (BusPlateCapacityDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }


    @Override
    public ArrayList<TripTableDbDetails> loadDataTrip() throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadDataTrip");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<TripTableDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof TripTableDbDetails) {
                                TripTableDbDetails myElement = (TripTableDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public boolean deleteTrip(String dep, String dateDep, String dateCom, String alloggio, String dateArr, String arr) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("deleteTrip");
            toServer.flush();
            //String dep, String dateDep, String dateCom, String staying, String dateArr, String arr
            toServer.writeUnshared (dep);
            toServer.flush();
            toServer.writeUnshared (dateDep);
            toServer.flush();
            toServer.writeUnshared (dateCom);
            toServer.flush();
            toServer.writeUnshared (alloggio);
            toServer.flush();
            toServer.writeUnshared (dateArr);
            toServer.flush();
            toServer.writeUnshared (arr);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            ok = (boolean) fromServer.readUnshared();
            System.out.println("Read reply from server");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }


    @Override
    public ArrayList<ChildTripDbDetails> loadChildTrip() throws RemoteException{
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadChildTrip");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<ChildTripDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof ChildTripDbDetails) {
                                ChildTripDbDetails myElement = (ChildTripDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public ArrayList<StaffTripDbDetails> loadStaffTrip() throws RemoteException{
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadStaffTrip");
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<StaffTripDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof StaffTripDbDetails) {
                                StaffTripDbDetails myElement = (StaffTripDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public int[] addTrip(ArrayList<String> selectedChild, ArrayList<String> selectedStaff, String timeDep, String timeArr, String timeCom, String departureFrom, String arrivalTo, String staying) throws RemoteException {
        boolean ok =false;
        int[] participantsFromServer = new int[2];

        try{
            toServer.writeUnshared ("addTrip");
            toServer.flush();
            toServer.writeUnshared (selectedChild);
            toServer.flush();
            toServer.writeUnshared (selectedStaff);
            toServer.flush();
            toServer.writeUnshared (timeDep);
            toServer.flush();
            toServer.writeUnshared (timeArr);
            toServer.flush();
            toServer.writeUnshared (timeCom);
            toServer.flush();
            toServer.writeUnshared (departureFrom);
            toServer.flush();
            toServer.writeUnshared (arrivalTo);
            toServer.flush();
            toServer.writeUnshared (staying);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean canProceed = false;
        try{
            canProceed = (boolean) fromServer.readUnshared();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(canProceed) {
            try {
                participantsFromServer[0] = (int) fromServer.readUnshared();
                participantsFromServer[1] = (int) fromServer.readUnshared();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return participantsFromServer;
        }
        return null;
    }

    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadTripSelectedChildren (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadTripSelectedChildren");
            toServer.flush();
            toServer.writeUnshared (selectedDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedDep);
            toServer.flush();
            toServer.writeUnshared (selectedCom);
            toServer.flush();
            toServer.writeUnshared (selectedAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedArr);
            toServer.flush();
            toServer.writeUnshared (selectedArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<ChildSelectedTripDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof ChildSelectedTripDbDetails) {
                                ChildSelectedTripDbDetails myElement = (ChildSelectedTripDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public ArrayList<StaffSelectedTripDbDetails> loadTripSelectedStaff (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadTripSelectedStaff");
            toServer.flush();
            toServer.writeUnshared (selectedDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedDep);
            toServer.flush();
            toServer.writeUnshared (selectedCom);
            toServer.flush();
            toServer.writeUnshared (selectedAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedArr);
            toServer.flush();
            toServer.writeUnshared (selectedArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<StaffSelectedTripDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof StaffSelectedTripDbDetails) {
                                StaffSelectedTripDbDetails myElement = (StaffSelectedTripDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> findNotAvailableStaff(ArrayList<String> selectedStaffCf, String selectedTripDep, String selectedTripCom) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("findNotAvailableStaff");
            toServer.flush();
            toServer.writeUnshared (selectedStaffCf);
            toServer.flush();
            toServer.writeUnshared (selectedTripDep);
            toServer.flush();
            toServer.writeUnshared (selectedTripCom);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<CodRifChildDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isUnavailable = false;
        try {
            isUnavailable = (boolean) fromServer.readUnshared();
            System.out.println("Is there staff unavailable? " + isUnavailable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isUnavailable) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof CodRifChildDbDetails) {
                                CodRifChildDbDetails myElement = (CodRifChildDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> findNotAvailableChild(ArrayList<String> selectedChildCf, String selectedTripDep, String selectedTripCom) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("findNotAvailableChild");
            toServer.flush();
            toServer.writeUnshared (selectedChildCf);
            toServer.flush();
            toServer.writeUnshared (selectedTripDep);
            toServer.flush();
            toServer.writeUnshared (selectedTripCom);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<CodRifChildDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isUnavailable = false;
        try {
            isUnavailable = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " + isUnavailable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isUnavailable) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof CodRifChildDbDetails) {
                                CodRifChildDbDetails myElement = (CodRifChildDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public int[] howManyActualParticipants(ArrayList<String> selectedChildCf, ArrayList<String> selectedStaffCf) throws RemoteException {
        int[] participants = new int[2];
        boolean ok = false;
        try{
            toServer.writeUnshared ("howManyActualParticipants");
            toServer.flush();
            toServer.writeUnshared (selectedChildCf);
            toServer.flush();
            toServer.writeUnshared (selectedStaffCf);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean canProceed = false;
        try{
            canProceed = (boolean) fromServer.readUnshared();
        } catch(Exception e){
            e.printStackTrace();
        }

        if(canProceed) {
            try {
                for (int i = 0; i <= 1; i++) {
                    participants[i] = (int) fromServer.readUnshared();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return participants;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public HashMap<String, ArrayList<String>> associateBusToParticipants(ArrayList<String> selectedChildCfArrayList, int totChildren, ArrayList<String> selectedStaffCfArrayList, int totStaff, String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        boolean ok = false;
        HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
        try{
            toServer.writeUnshared ("associateBusToParticipants");
            toServer.flush();
            toServer.writeUnshared (selectedChildCfArrayList);
            toServer.writeInt(totChildren);
            toServer.flush();
            toServer.writeUnshared (selectedStaffCfArrayList);
            toServer.flush();
            toServer.writeInt(totStaff);
            toServer.flush();
            toServer.writeUnshared (selectedDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedDep);
            toServer.flush();
            toServer.writeUnshared (selectedCom);
            toServer.flush();
            toServer.writeUnshared (selectedAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedArr);
            toServer.flush();
            toServer.writeUnshared (selectedArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            hashMap = (HashMap<String, ArrayList<String>>) fromServer.readUnshared();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ok = (boolean) fromServer.readUnshared();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ok) {
            return hashMap;
        }
        return null;
    }

    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadWhoTrip(String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadWhoTrip");
            toServer.flush();
            toServer.writeUnshared (selectedDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedDep);
            toServer.flush();
            toServer.writeUnshared (selectedCom);
            toServer.flush();
            toServer.writeUnshared (selectedAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedArr);
            toServer.flush();
            toServer.writeUnshared (selectedArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<ChildSelectedTripDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof ChildSelectedTripDbDetails) {
                                ChildSelectedTripDbDetails myElement = (ChildSelectedTripDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> loadBusTrip(String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadBusTrip");
            toServer.flush();
            toServer.writeUnshared (selectedDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedDep);
            toServer.flush();
            toServer.writeUnshared (selectedCom);
            toServer.flush();
            toServer.writeUnshared (selectedAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedArr);
            toServer.flush();
            toServer.writeUnshared (selectedArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<CodRifChildDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof CodRifChildDbDetails) {
                                CodRifChildDbDetails myElement = (CodRifChildDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public ArrayList<SolutionDbDetails> loadSolution(String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadSolution");
            toServer.flush();
            toServer.writeUnshared (selectedTripDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedTripDep);
            toServer.flush();
            toServer.writeUnshared (selectedTripCom);
            toServer.flush();
            toServer.writeUnshared (selectedTripAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedTripArr);
            toServer.flush();
            toServer.writeUnshared (selectedTripArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<SolutionDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is db empty? " +isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof SolutionDbDetails) {
                                SolutionDbDetails myElement = (SolutionDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public ArrayList<String> findParticipantOnWrongBus(ArrayList<String> selectedChildCfArrayList, String selectedBus, String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("findParticipantOnWrongBus");
            toServer.flush();
            toServer.writeUnshared (selectedChildCfArrayList);
            toServer.flush();
            toServer.writeUnshared (selectedBus);
            toServer.flush();
            toServer.writeUnshared (selectedTripDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedTripDep);
            toServer.flush();
            toServer.writeUnshared (selectedTripCom);
            toServer.flush();
            toServer.writeUnshared (selectedTripAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedTripArr);
            toServer.flush();
            toServer.writeUnshared (selectedTripArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> arrayListToReturn = new ArrayList<>();
        boolean areThere = false;
        try {
            areThere = (boolean) fromServer.readUnshared();
            System.out.println("Are there participants on wrong bus? " + areThere);   //true = there are participants on wrong bus
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(areThere) {
            try {
                Object loaded = fromServer.readUnshared();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public ArrayList<String> findMissingParticipantsOnThisBus(ArrayList<String> peopleOnWrongBus, ArrayList<String> selectedChildCfArrayList, String selectedBus, String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("findMissingParticipantsOnThisBus");
            toServer.flush();
            toServer.writeUnshared (peopleOnWrongBus);
            toServer.flush();
            toServer.writeUnshared (selectedChildCfArrayList);
            toServer.flush();
            toServer.writeUnshared (selectedBus);
            toServer.flush();
            toServer.writeUnshared (selectedTripDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedTripDep);
            toServer.flush();
            toServer.writeUnshared (selectedTripCom);
            toServer.flush();
            toServer.writeUnshared (selectedTripAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedTripArr);
            toServer.flush();
            toServer.writeUnshared (selectedTripArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> arrayListToReturn = new ArrayList<>();
        boolean isMissing = false;
        try {
            isMissing = (boolean) fromServer.readUnshared();
            System.out.println("Is someone missing? " + isMissing);      //true = someone's missing
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isMissing) {
            try {
                Object loaded = fromServer.readUnshared();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }

    @Override
    public void makeIsHereFalse(String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("makeIsHereFalse");
            toServer.flush();
            toServer.writeUnshared (selectedTripDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedTripDep);
            toServer.flush();
            toServer.writeUnshared (selectedTripCom);
            toServer.flush();
            toServer.writeUnshared (selectedTripAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedTripArr);
            toServer.flush();
            toServer.writeUnshared (selectedTripArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok)
            System.out.println("Read reply from server: is_here made false");

        else
            System.out.println("Read reply from server: problems upcoming, is_here unchanged.");
    }

    @Override
    public void makeIsHereTrue(String selectedBus, String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("makeIsHereTrue");
            toServer.flush();
            toServer.writeUnshared (selectedBus);
            toServer.flush();
            toServer.writeUnshared (selectedTripDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedTripDep);
            toServer.flush();
            toServer.writeUnshared (selectedTripCom);
            toServer.flush();
            toServer.writeUnshared (selectedTripAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedTripArr);
            toServer.flush();
            toServer.writeUnshared (selectedTripArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok)
            System.out.println("Read reply from server: is_here made true");

        else
            System.out.println("Read reply from server: problems upcoming, is_here unchanged.");
    }

    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadMissing(ArrayList<String> missingArrayList, String selectedBus, String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException {
        boolean ok = false;
        try{
            toServer.writeUnshared ("loadMissing");
            toServer.flush();
            toServer.writeUnshared (missingArrayList);
            toServer.flush();
            toServer.writeUnshared (selectedBus);
            toServer.flush();
            toServer.writeUnshared (selectedTripDepFrom);
            toServer.flush();
            toServer.writeUnshared (selectedTripDep);
            toServer.flush();
            toServer.writeUnshared (selectedTripCom);
            toServer.flush();
            toServer.writeUnshared (selectedTripAccomodation);
            toServer.flush();
            toServer.writeUnshared (selectedTripArr);
            toServer.flush();
            toServer.writeUnshared (selectedTripArrTo);
            toServer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<ChildSelectedTripDbDetails> arrayListToReturn = new ArrayList<>();
        boolean isEmpty = false;
        try {
            isEmpty = (boolean) fromServer.readUnshared();
            System.out.println("Is anyone missing? " + !isEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty) {
            try {
                Object loaded = fromServer.readUnshared();
                if (loaded instanceof ArrayList<?>) {
                    //get list
                    ArrayList<?> loadedAl = (ArrayList<?>) loaded;
                    if (loadedAl.size() > 0) {
                        for (Object element : loadedAl) {
                            if (element instanceof ChildSelectedTripDbDetails) {
                                ChildSelectedTripDbDetails myElement = (ChildSelectedTripDbDetails) element;
                                arrayListToReturn.add(myElement);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            ok = (boolean) fromServer.readUnshared ();
            System.out.println("Read reply from server");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println(ok);

        if(ok){
            return arrayListToReturn;
        }
        return null;
    }


}