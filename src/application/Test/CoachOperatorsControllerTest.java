package application.Test;

import application.rmi.server.ServerImpl;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class CoachOperatorsControllerTest {

    private ServerImpl si;

    private CoachOperatorsControllerTest() throws RemoteException {
        si = new ServerImpl();
    }


    @Before
    void testAddCoachOperator() throws RemoteException {
        assertTrue(si.addDataCoachOperator("OO", "12", "AS", "1234", "AAAAS", "12345", "AB"));
    }

    @Test
    void testNullAddCoachOperator() throws RemoteException{
        assertFalse(si.addDataCoachOperator(null, null, null, null, null, null, null));
    }

    @Test
    void testLoadCoachOperator() throws RemoteException{
        assertNotNull(si.loadDataCoachOperator());
    }

    @Test
    void testAddBus() throws RemoteException{
        assertTrue(si.addBusToDb("AB", 100, "12"));
    }

    @Test
    void testNullAddBus() throws RemoteException{
        assertFalse(si.addBusToDb(null, 0, null));
    }

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
    }


    @Test
    void testDeleteNullCoachOperator() throws RemoteException{
        assertFalse(si.deleteCoachOperator(null));
    }

    @After
    void testDeleteCoachOperator() throws RemoteException{
        assertTrue(si.deleteCoachOperator("13"));
    }

}