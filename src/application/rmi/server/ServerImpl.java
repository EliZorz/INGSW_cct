package application.rmi.server;

import application.Interfaces.UserRemote;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * Created by ELISA on 21/03/2018.
 */
public class ServerImpl extends UnicastRemoteObject implements UserRemote {

    private static final String usrname = "root";

    private static final String pw = "Monali2009!"; //123456

    //JDBC driver name and DB URL
    private static final String url = "jdbc:mysql://localhost:3306/Login";    //"jdbc:mysql://127.0.0.1:3306/Login";

    private static final String db = "com.mysql.jdbc.Driver";

    protected ServerImpl() throws RemoteException {
        super();
    }


    @Override
    public void logOut() throws RemoteException {
        System.out.println("logout");
    }

    @Override
    public void save() throws RemoteException {
        System.out.println("save");
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

    public String sendMessage(String clientMessage) {
        return "Client Message".equals(clientMessage) ? "Server Message" : null;
    }

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;


}
