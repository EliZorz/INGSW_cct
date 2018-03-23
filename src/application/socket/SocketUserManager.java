package application.socket;

import application.Interfaces.UserRemote;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class SocketUserManager implements UserRemote {

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
}
