package application.Test;


import application.rmi.server.ServerImpl;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOnServerImpl {


    //QUESTO TEST VERIFICA CHE NON RIESCA AD ACCEDERE AL DATABASE UN UTENTE CHE NON SCRIVE PASSWORD E USERNAME OPPURE NON NE SCRIVE UNO DEI DUE
    @Test
    public void testAdd1Plus() throws RemoteException {

        ServerImpl si = new ServerImpl();

        assertEquals(false, si.funzLog(null,null));
        assertEquals(false, si.funzLog("ludo",null));
        assertEquals(false, si.funzLog(null,"ludo123"));
    }

    @Test
    public void testLoadMenu() throws RemoteException{
        ServerImpl si = new ServerImpl();
        assertEquals(null, si.loadThisMenu(null));
    }

    @Test
    public void testSearchIngredients() throws RemoteException{
        ServerImpl si = new ServerImpl();
        assertEquals(null, si.searchIngredients(null));
       
    }






}
