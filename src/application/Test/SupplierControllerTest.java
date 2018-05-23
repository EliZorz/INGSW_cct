package application.Test;

import application.rmi.server.ServerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import java.rmi.RemoteException;
import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SupplierControllerTest {

    private ServerImpl si;

    private SupplierControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @Before
    void testAddSupplier() throws RemoteException{
        assertTrue(si.addDataSupplier("AAA","0","BBB", "1234", "CCC", "01234", "DD"));
        assertTrue(si.addDataSupplier("BBB", "1", "AAA", "000", "AAA", "12345", "AB"));
    }


    @Test
    void testAddNullParameterSupplier() throws RemoteException {
            assertFalse(si.addDataSupplier("AAA", null, "AAA", "000", "AAA", "00000", "AA"));
    }

    @Test
    void testAddNullSupplier() throws RemoteException{
        assertFalse(si.addDataSupplier(null, null, null, null, null, null, null));
    }

    @Test
    void testAddIngrSupplier() throws RemoteException {
        assertTrue(si.addIngrToDb("ABC", "1"));
    }

    @Test
    void testLoadSupplier() throws RemoteException{
        assertNotNull(si.loadDataSuppliers());
    }

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

    @Test
    void testNullUpdateSupplier() throws RemoteException {
       assertFalse( si.updateSupplier(null, null, null, null, null, null, null, null));
    }

    @Test
    void testUpdateSupplier() throws RemoteException {
        assertTrue(si.updateSupplier("AAA", "0", "33", "AAA", "01234", "AAAA", "12345", "AA"));
    }

    @Test
    void testNullParameterUpdateSupplier() throws RemoteException {
        assertFalse(si.updateSupplier("AAA", null, "31", "AAA", "01234", "AAAA", "12345", "AA"));

    }

    @After
    void testDeleteSupplier() throws RemoteException {
        assertTrue(si.deleteSupplier("33", null));
    }

    @Test
    void testNullDeleteSupplier() throws RemoteException {
        assertFalse(si.deleteSupplier(null, null));
    }

    @After
    void testDeleteSupplierWIthIngredients() throws RemoteException{
        assertTrue(si.deleteSupplier("1", si.loadNoIngr("1")));
    }

}