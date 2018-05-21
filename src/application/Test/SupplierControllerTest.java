package application.Test;

import application.contr.StaffController;
import application.details.IngredientsDbDetails;

import application.rmi.server.ServerImpl;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SupplierControllerTest {
    private ServerImpl si;

    private SupplierControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @Test
    void handleAddSupplier() throws RemoteException {
        assertTrue(si.addDataSupplier("AAA", "11112", "CCCCCD","1234", "DDDD", "12345", "AB"));
        assertTrue(si.addDataSupplier("BBB", "000078", "CCCCCD","1234", "DDDD", "12345", "AB"));
        assertFalse(si.addDataSupplier(null , "11112", "CCCCCD","1234", "DDDD", "12345", "AB"));
        assertFalse(si.addDataSupplier("AAA", null, "CCCCCD","1234", "DDDD", "12345", "AB"));
        assertFalse(si.addDataSupplier("AAA", "11112", null,"1234", "DDDD", "12345", "AB"));
        assertFalse(si.addDataSupplier("AAA", "11112", "CCCCCD",null, "DDDD", "12345", "AB"));
        assertFalse(si.addDataSupplier("AAA", "11112", "CCCCCD","1234", null, "12345", "AB"));
        assertFalse(si.addDataSupplier("AAA", "11112", "CCCCCD","1234", "DDDD", null, "AB"));
        assertFalse(si.addDataSupplier("AAA", "11112", "CCCCCD","1234", "DDDD", "12345", null));
        assertFalse(si.addDataSupplier(null, null, null, null, null, null, null));
        assertTrue(si.addIngrToDb("FFFFF", "11112"));
    }

    @Test
    void handleAddIngrToDb() throws RemoteException {
        assertFalse(si.addIngrToDb("ABCD", null));
        assertFalse(si.addIngrToDb(null, null));
        assertFalse(si.addIngrToDb(null, "AAA"));
    }

    @Test
    void handleLoadIngr() throws RemoteException {
        assertNotNull(si.loadIngr());
    }

    @Test
    void handleLoadSuppliers() throws RemoteException {
        assertNotNull(si.loadDataSuppliers());
    }

    @Test
    void handleUpdateSupplier() throws RemoteException {
        assertTrue(si.updateSupplier("ABC", "11112", "11", "AAA", "123456", "CCCC", "12345", "AB"));
        assertTrue(si.updateSupplier("ABC", "11", "11112", "AAA", "123456", "CCCC", "12345", "AB"));
        assertFalse(si.updateSupplier(null, null, null, null, null, null, null, null));
        assertFalse(si.updateSupplier("ABC", null, "BBB", "AAA", "000", "AAAAA", "00123", "BC"));
    }

    @Test
    void handleDeleteSupplier() throws RemoteException {
        ArrayList<IngredientsDbDetails> ingr = new ArrayList<>();
        ingr.add(new IngredientsDbDetails("FFFFF"));
        assertFalse(si.deleteSupplier(null, null));
        assertFalse(si.deleteSupplier(null, ingr));
        assertTrue(si.deleteSupplier("11112", ingr));
        assertTrue(si.deleteSupplier("000078", null));


    }




}