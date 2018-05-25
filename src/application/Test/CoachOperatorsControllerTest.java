package application.Test;

import application.rmi.server.ServerImpl;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class CoachOperatorsControllerTest {

    private ServerImpl si;

    private CoachOperatorsControllerTest() throws RemoteException {
        si = new ServerImpl();
    }


    @BeforeEach
    void testAddCoachOperator() throws RemoteException {
        si.addDataCoachOperator("OO", "12", "AS", "1234", "AAAAS", "12345", "AO");
        si.addBusToDb("AB", 100, "12");
    }

    @AfterEach
    void testDeleteCoachOperator() throws RemoteException{
        si.deleteCoachOperator("12");
        si.deleteCoachOperatorBus("AB");
    }

    //ADD

    @Test
    void testNullAddCoachOperator() throws RemoteException{
        assertFalse(si.addDataCoachOperator(null, null, null, null, null, null, null));
    }

    //LOAD
    @Test
    void testLoadCoachOperator() throws RemoteException{
        assertNotNull(si.loadDataCoachOperator());
    }


    //ADD BUS

    @Test
    void testNullAddBus() throws RemoteException{
        assertFalse(si.addBusToDb(null, 0, null));
    }

    //UPDATE

    @Test
    void testNullPivaUpdateCoachOperator() throws RemoteException{
        assertFalse(si.updateCoachOperator("OKKK", "12", null, "AAA", "123", "ASDF", "12345", "AB"));
    }

    @Test
    void testNullUpdateCoachOperator() throws RemoteException{
        assertFalse(si.updateCoachOperator(null, null, null, null,null, null, null, null));
    }

    @Test
    void testUpdateCoachOperator() throws RemoteException{
        assertTrue(si.updateCoachOperator("OKKK", "12", "13", "AAA", "123", "ASDF", "12345", "AB"));
        assertTrue(si.updateCoachOperator("OKKK", "13", "12", "AAA", "123", "ASDF", "12345", "AB"));
    }

    //LOAD BUS

    @Test
    void testLoadDataBus() throws RemoteException{
        assertNotNull(si.loadDataBus("12"));
    }

    @Test
    void testLoadNullDataBus() throws RemoteException{
        assertNull(si.loadDataBus(null));
    }

    //CONTROLL PARTITA IVA
    @Test
    void testControllPiva() throws RemoteException{
        assertFalse(si.controllPiva("12"));
        assertTrue(si.controllPiva(null));
    }

    //CONTROLL PLATE
    @Test
    void testControllPlate() throws RemoteException{
        assertFalse(si.controllBus("AB"));
        assertTrue(si.controllBus(null));
    }


    //DELETE BUS

    @Test
    void testNullDeleteCoachOperatorBus() throws RemoteException{
        assertFalse(si.deleteCoachOperatorBus(null));
    }

    //DELETE COACH

    @Test
    void testDeleteNullCoachOperator() throws RemoteException{
        assertFalse(si.deleteCoachOperator(null));
    }


}