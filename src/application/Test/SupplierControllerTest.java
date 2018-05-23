package application.Test;

import application.rmi.server.ServerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SupplierControllerTest {

    private ServerImpl si;

    private SupplierControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @BeforeEach
    void addSupplier() throws RemoteException{
        si.addDataSupplier("BBB", "1", "AAA", "000", "AAA", "12345", "AB");
        si.addIngrToDb("ABC", "1");
        si.addDataSupplier("AEF","2","QWE", "000", "AAA", "12345", "AB");
    }

    @AfterEach
    void deleteSupplier() throws RemoteException{
        si.deleteSupplier("1", si.loadNoIngr("1"));
        si.deleteSupplier("2", si.loadNoIngr("2"));
        si.deleteSupplier("33", si.loadNoIngr("33"));
    }

    //ADD SUPPLIER

    @Test
    void testAddNullParameterSupplier() throws RemoteException {
            assertFalse(si.addDataSupplier("AAA", null, "AAA", "000", "AAA", "00000", "AA"));
    }

    @Test
    void testAddNullSupplier() throws RemoteException{
        assertFalse(si.addDataSupplier(null, null, null, null, null, null, null));
    }

    //LOAD SUPPLIER

    @Test
    void testLoadSupplier() throws RemoteException{
        assertNotNull(si.loadDataSuppliers());
    }

    //ADD INGREDIENTS

    @Test
    void testNoIngrAddIngrSupplier() throws RemoteException{
        assertFalse(si.addIngrToDb(null, "1"));
    }

    @Test
    void testNoSupplierAddIngrSupplier() throws RemoteException{
        assertFalse(si.addIngrToDb("AAA", null));
    }

    @Test
    void testNullAddIngrSupplier() throws RemoteException{
        assertFalse(si.addIngrToDb(null,  null));
    }

    //LOAD NO INGREDIENTS

    @Test
    void testLoadNoIngr() throws RemoteException{
        assertNotNull(si.loadNoIngr("1"));
    }


    @Test
    void testNullLoadNoIngr() throws RemoteException{
        assertNull(si.loadNoIngr(null));
    }

    // UPDATE SUPPLIER

    @Test
    void testNullUpdateSupplier() throws RemoteException {
       assertFalse( si.updateSupplier(null, null, null, null, null, null, null, null));
    }

    @Test
    void testUpdateSupplier() throws RemoteException {
        assertTrue(si.updateSupplier("AAA", "1", "33", "AAA", "01234", "AAAA", "12345", "AA"));
    }

    @Test
    void testNullParameterUpdateSupplier() throws RemoteException {
        assertFalse(si.updateSupplier("AAA", null, "31", "AAA", "01234", "AAAA", "12345", "AA"));
    }

    //DELETE SUPPLIER

    @Test
    void testNullDeleteSupplier() throws RemoteException {
        assertFalse(si.deleteSupplier(null, null));
    }

    @Test
    void testDeleteSupplierWithoutIngredients() throws RemoteException{
        assertTrue(si.deleteSupplier("2", si.loadNoIngr("2")));
    }



}