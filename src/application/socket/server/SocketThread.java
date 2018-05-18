package application.socket.server;

import application.details.DishesDbDetails;
import application.details.IngredientsDbDetails;
import application.details.SpecialDbDetails;
import application.rmi.server.ServerImpl;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//quello corretto
public class SocketThread extends Thread {

    private String line = null;
    private BufferedReader in = null;  //uso questo e non Scanner perché è synchronized
    private PrintWriter out = null;
    private Socket s = null;
    private ObjectOutputStream outputToClient;
    private ObjectInputStream inputFromClient;
    private ServerImpl impl;  //mi serve per poter chiamare le funzioni  ESSENZIALE!!!!!

    //devo considerare che per chiamare  i metodi devo mandare dei messaggi

    public SocketThread(Socket s, ServerImpl impl) throws RemoteException {
        this.impl = impl;
        this.s = s;

    }

    @Override
    public void run() {
        try {
            //creo in e out per la comunicazione con il client
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            out = new PrintWriter(s.getOutputStream());
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
            System.out.println("Client " + line + " Closed");  //stampa nel server che tale client è chiuso
        } finally {
            try {
                System.out.println("Connection Closing..");
                if (in != null) {
                    in.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if (out != null) {
                    out.close();
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
        if(line.equals("login")){
            System.out.println("Logging in...");
            String usr = inputFromClient.readUTF();
            String pwd = inputFromClient.readUTF();
            boolean isLogged = impl.funzLog(usr, pwd);
            outputToClient.writeBoolean(isLogged);
            return isLogged;
        }else if(line.equals("loadmenu")){
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
        }else if(line.equals("loadSuppliers")){
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
        }else if(line.equals("addSpecialMenu")){
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
            return false;

    }
}
