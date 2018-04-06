package application.rmi.server;

import application.Interfaces.UserRemote;

import application.contr.Database;
import com.mysql.jdbc.Connection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by ELISA on 21/03/2018.
 */
public class ServerImpl extends UnicastRemoteObject implements UserRemote {  //l'idea è che socket e rmi usino entrambe questa implementazione


    public ServerImpl() throws RemoteException {
        super();
    }


    public boolean funzLog (String usr, String pwd){

        PreparedStatement st;
        ResultSet result = null;

        String queryLog = "SELECT * FROM UserIn WHERE Username = ? AND Password = ? ";//"SELECT * FROM sys.login WHERE Username = ? AND Password = ? " ;

        boolean res = false;

        try{
            Database receivedCon = new Database();
            Connection connectionOK = receivedCon.databaseCon();
            if(connectionOK != null)
                System.out.println("Connection successful");
            else
                System.out.println("Connection failed");


            st = connectionOK.prepareStatement(queryLog);
            st.setString(1, usr);
            st.setString(2, pwd);

            result = st.executeQuery();

        } catch (SQLException e) {
            System.out.println("problema nella ricerca del db");
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
        }

        return res;


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

    /*


    public String sendMessage(String clientMessage) {
        return "Client Message".equals(clientMessage) ? "Server Message" : null;
    }
    */


}
