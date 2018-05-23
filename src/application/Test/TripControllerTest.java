package application.Test;

import application.rmi.server.ServerImpl;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class TripControllerTest {
    private ServerImpl si;
    private TripControllerTest() throws RemoteException{
        si = new ServerImpl();
    }

    

}