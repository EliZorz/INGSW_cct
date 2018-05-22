package application.Test;

import application.rmi.server.ServerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

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
    void testAddNullStaff() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.addDataStaff(null, null, null, null, null, null, null, null, null, null, null);
        });
    }

    @Test
    void testAddNullAllergiesStaff(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.addDataStaff("AAA", "BBB", null, "DDD", LocalDate.parse("1982-11-11"), "EEE", "FFF", "GGG", "12345", "AB", null);
        });
    }

    @Test
    void testAddDataStaff()throws RemoteException{
        ArrayList<String> allergies = new ArrayList<>();
        assertTrue(si.addDataStaff("AAA", "BBB", "CCC111", "DDD", LocalDate.parse("1982-11-11"), "EEE", "FFF", "GGG", "12345", "AB", allergies));
        assertTrue(si.addDataStaff("AAA", "BBB", "ABC", "DDD", LocalDate.parse("1982-11-11"), "EEE", "FFF", "GGG", "12345", "AB", allergies));
    }

    @Test
    void testLoadDataStaff() throws RemoteException {
        assertNotNull(si.loadDataStaff());
    }

    @Test
    void testLoadIngredients() throws RemoteException {
        assertNotNull(si.loadIngr());
    }

    @Test
    void testNullParameterUpdateStaff() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.updateStaff(null, null, null, null, null, null, null, null, null, null, null, null);
            si.updateStaff(null, null, "ABC", null, null, null, null, null, null, null, null, null);
        });
    }

    @Test
    void testNullSelectedAllergiesUpdateStaff() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            assertTrue(si.updateStaff("AAA", "BBB", "CCC111", "ASGH", "DDD", LocalDate.parse("1982-11-11"), "EEE", "FFF", "GGG", "12345", "AB", null));
        });
    }

    @Test
    void  testUpdateStaff() throws RemoteException{
        ArrayList<String> allergies = new ArrayList<>();
        assertTrue(si.updateStaff("AAA", "BBB", "CCC111", "DDD", "EEE",  LocalDate.parse("1982-11-11"), "EEE", "FFF", "GGG", "12345", "AB", allergies));
    }

    @Test
    void testDeleteStaff() throws RemoteException {
        assertTrue(si.deleteStaff("ABC"));
        assertTrue(si.deleteStaff("CCC111"));
    }

    @Test
    void testNullDeleteStaff() throws RemoteException{
        assertTrue(si.deleteStaff(null));
    }

}