package application.rmi.server;

import application.Interfaces.UserRemote;
import application.contr.Database;
import application.contr.GuiNew;
import com.mysql.jdbc.Connection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by ELISA on 21/03/2018.
 */
public class ServerImpl extends UnicastRemoteObject implements UserRemote {

    private static final String usrname = "root";

    private static final String pw = "Monali2009!"; //123456

    //JDBC driver name and DB URL
    private static final String url = "jdbc:mysql://localhost:3306/Login";    //"jdbc:mysql://127.0.0.1:3306/Login";

    private static final String db = "com.mysql.jdbc.Driver";

    public ServerImpl() throws RemoteException {
        super();
    }


    public ResultSet funzLog (String usr, String pwd){

        PreparedStatement st;
        ResultSet result = null;

        String queryLog = "SELECT * FROM sys.login WHERE Username = ? AND Password = ? ";//"SELECT * FROM UserIn WHERE Username = ? AND Password = ? " ;


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
            e.printStackTrace();
        }

        return result;


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
    public void funzLog() throws RemoteException {

    }

    public String sendMessage(String clientMessage) {
        return "Client Message".equals(clientMessage) ? "Server Message" : null;
    }


}
