package application.socket;

import application.Interfaces.UserRemote;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.Scanner;

public class SocketAdapter  implements UserRemote {

    private ServerSocketImpl wizard;
    Scanner s = new Scanner(System.in);

    public SocketAdapter(ServerSocketImpl wizard){
        this.wizard = wizard;
    }
    @Override
    public ResultSet funzLog(String usr, String pwd) throws IOException {
        ResultSet result = null;
        this.wizard.controllLogin();
        System.out.println(usr);
        System.out.println(pwd);
        return result;
    }

    @Override
    public boolean logOut() throws RemoteException {
        return false;
    }

    @Override
    public boolean save() throws RemoteException {
        return false;
    }

    @Override
    public void add() throws RemoteException {

    }

    @Override
    public void delete() throws RemoteException {

    }

    @Override
    public void update() throws RemoteException {

    }

    @Override
    public void funzLog() throws RemoteException {

    }
}
