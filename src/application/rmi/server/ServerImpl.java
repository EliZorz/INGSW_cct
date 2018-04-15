package application.rmi.server;

import application.Interfaces.UserRemote;
import application.contr.Database;
import application.details.ChildDbDetails;
import application.details.ChildGuiDetails;
import application.details.ChildDbDetails;
import application.details.ChildGuiDetails;
import application.details.DishesDbDetails;
import application.details.DishesDetails;
import com.mysql.jdbc.Connection;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ELISA on 21/03/2018.
 */
public class ServerImpl extends UnicastRemoteObject implements UserRemote {  //socket e rmi usano entrambe questa implementazione


    public ServerImpl() throws RemoteException {
        super();
    }


    public boolean funzLog (String usr, String pwd){

        PreparedStatement st = null;

        ResultSet result = null;

        String queryLog = "SELECT * FROM UserIn WHERE Username = ? AND Password = ? ";//"SELECT * FROM sys.login WHERE Username = ? AND Password = ? " ;

        boolean res = false;

        try{

            st = this.connHere().prepareStatement(queryLog);
            st.setString(1, usr);
            st.setString(2, pwd);

            result = st.executeQuery();


        } catch (SQLException e) {
            System.out.println("Error during search in DB");
            e.printStackTrace();
        }


        try{
            if( !result.next() ) {
                System.out.println("No user like that in your database");
                res = false;
            } else {
                result.beforeFirst();
                while (result.next()) {
                    String usrFound = result.getString("Username");
                    System.out.println("USER: " + usrFound);
                    String pwdFound = result.getString("Password");
                    System.out.println("PASSWORD: " + pwdFound);
                }

                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

         }


        return res;


    }


    public ArrayList<ChildDbDetails> loadData() throws RemoteException {

        PreparedStatement st;

        ResultSet result = null;

        ArrayList<ChildDbDetails> childDbArrayList = new ArrayList<>(9);

        String queryLoad = "SELECT Cognome, Nome, CF, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia" +
                " FROM interni INNER JOIN bambino" +
                " ON interni.CF = bambino.Interni_CF";

        try{
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try{
            if( !result.next() ) {
                System.out.println("No child in DB");

            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        ChildDbDetails prova = new ChildDbDetails(result.getString(1),result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6),
                                result.getString(7),
                                result.getString(8),
                                result.getString(9));
                        //get string from db, put into list of ChildGuiData, ready to put it into GUI
                        childDbArrayList.add(prova);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //ritorna lista di bambini
        return childDbArrayList;

    }


    public ArrayList<ChildGuiDetails> addData(String name, String surname, String cf, String birthday, String bornWhere, String residence, String address, String cap, String province) throws RemoteException {
        PreparedStatement st = null;

        ResultSet result = null;

        String queryAdd = "INSERT INTO interni(Cognome, Nome, CF, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia)";


        return null;
    }

    @Override
    public boolean logOut() throws RemoteException {
        System.out.println("logout");
        return true;
    }

    @Override
    public boolean save() throws RemoteException {
        System.out.println("save");
        return true;
    }

    @Override
    public void add() throws RemoteException {
        System.out.println("add");
    }

    @Override
    public void delete() throws RemoteException {
        System.out.println("del");
    }

    @Override
    public void update() throws RemoteException {
        System.out.println("upd");
    }

    @Override
    public ArrayList<DishesDbDetails> loadMenu() throws RemoteException {
        PreparedStatement st;
        ResultSet result = null;
        ArrayList<DishesDbDetails> dishes = new ArrayList<>(4);

        String queryLoad1 = "SELECT * FROM login.menubase";

        try{
            st = this.connHere().prepareStatement(queryLoad1);
            result = st.executeQuery(queryLoad1);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try{
            System.out.println("ok");
            if( !result.next() ) {
                System.out.println("No menu in Db");

            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        DishesDbDetails prova  = null;
                        prova = new DishesDbDetails(result.getString(1),result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5));


                        //get string from db, put into list of ChildGuiData, ready to put it into GUI
                        dishes.add(prova);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return dishes;

    }


    public String sendMessage(String clientMessage) {
        return "Client Message".equals(clientMessage) ? "Server Message" : null;
    }


    public Connection connHere (){

        Database receivedCon = new Database();
        Connection connectionOK = receivedCon.databaseCon();
        if(connectionOK != null)
            System.out.println("Connection successful");
        else
            System.out.println("Connection failed");

        return connectionOK;

    }

}
