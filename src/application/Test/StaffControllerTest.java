package application.Test;

import application.contr.StaffController;
import application.rmi.server.ServerImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StaffControllerTest {

    private ServerImpl si;

    private StaffControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @Test
    void handleAddStaff() throws RemoteException {
        ArrayList<String> allergies = new ArrayList<>();
        assertTrue(si.addDataStaff("AAA", "BBB", "CCC111", "DDD", LocalDate.parse("1982-11-11"), "EEE", "FFF", "GGG", "12345", "AB", allergies));
        assertFalse(si.addDataStaff(null, null, null, null, null, null, null, null, null, null, null));
        assertTrue(si.addDataStaff("AAA", "BBB", "ABC", "DDD", LocalDate.parse("1982-11-11"), "EEE", "FFF", "GGG", "12345", "AB", allergies));

    }

    @Test
    void handleLoadStaff() throws RemoteException {
        assertNotNull(si.loadDataStaff());
    }


    @Test
    void handleUpdateStaff() {
    }

    @Test
    void handleDeleteStaff() throws RemoteException {
        assertTrue(si.deleteStaff("ABC"));
        assertTrue(si.deleteStaff("CCC111"));
        assertFalse(si.deleteStaff(null));
    }

}