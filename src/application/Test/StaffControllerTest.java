package application.Test;

import application.rmi.server.ServerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class StaffControllerTest {

    private ServerImpl si;

    private StaffControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @BeforeEach
    void addStaff() throws RemoteException{
        ArrayList<String> allergies = new ArrayList<>();
        si.addDataStaff("AAA", "BBB",
                "CCC111", "DDD", LocalDate.parse("1982-11-11"),
                "EEE", "FFF", "GGG",
                "12345", "AB", allergies);

    }

    @AfterEach
    void deleteStaff() throws RemoteException{
        si.deleteStaff("CCC111");
    }

    //ADD STAFF

    @Test
    void testAddNullStaff() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.addDataStaff(null, null, null, null,
                    null, null, null, null,
                    null, null, null);
        });
    }

    @Test
    void testAddNullAllergiesStaff(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.addDataStaff("AAA", "BBB", null,
                    "DDD", LocalDate.parse("1982-11-11"), "EEE",
                    "FFF", "GGG", "12345",
                    "AB", null);
        });
    }

    //LOAD

    @Test
    void testLoadDataStaff() throws RemoteException {
        assertNotNull(si.loadDataStaff());
    }

    @Test
    void testLoadIngredients() throws RemoteException {
        assertNotNull(si.loadIngr());
    }

    //UPDATE

    @Test
    void testNullParameterUpdateStaff() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.updateStaff(null, null, null,
                    null, null, null, null, null,
                    null, null, null, null);
            si.updateStaff(null, null, "ABC", null,
                    null, null, null, null,
                    null, null, null, null);
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
        assertTrue(si.updateStaff("AAA", "BBB", "CCC111",
                "DDD", "EEE",  LocalDate.parse("1982-11-11"), "EEE",
                "FFF", "GGG", "12345", "AB", allergies));
        assertTrue(si.updateStaff("AAA", "BBB", "DDD", "CCC111",
                "EEE",  LocalDate.parse("1982-11-11"), "EEE", "FFF",
                "GGG", "12345", "AB", allergies));

    }

    //CONTROLL STAFF

    @Test
    void testControll() throws RemoteException{
        assertFalse(si.controllCF("CCC111"));
        assertTrue(si.controllCF(null));
    }


    //DELETE STAFF

    @Test
    void testNullDeleteStaff() throws RemoteException{
        assertFalse(si.deleteStaff(null));
    }

}
