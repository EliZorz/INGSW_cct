package application.Test;

import application.rmi.server.ServerImpl;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SupplierControllerTest {

    private ServerImpl si;

    private SupplierControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @Test
    void testAddSupplier() throws RemoteException{
        assertTrue(si.addDataSupplier("AAA","0000","BBB", "1234", "CCC", "01234", "DD"));
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
        assertTrue(si.updateSupplier("AAA", "0000", "0001", "AAA", "01234", "AAAA", "12345", "AA"));
    }

    @Test
    void testNullParameterUpdateSupplier() throws RemoteException {
        assertFalse(si.updateSupplier("AAA", null, "0001", "AAA", "01234", "AAAA", "12345", "AA"));

    }

    @Test
    void testDeleteSupplier() throws RemoteException {
        Assertions.assertThrows(NullPointerException.class, ()->{
            si.deleteSupplier("0001", null);
        });    }

    @Test
    void testNullDeleteSupplier(){
        Assertions.assertThrows(NullPointerException.class, ()->{
            si.deleteSupplier(null, null);
        });
    }

    @Test
    void testDeleteSupplierWIthIngredients() throws RemoteException{
        assertTrue(si.deleteSupplier("1", si.loadNoIngr("1")));
    }

}