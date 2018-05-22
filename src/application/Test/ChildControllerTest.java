package application.Test;

import application.details.IngredientsDbDetails;
import application.rmi.server.ServerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ChildControllerTest {

    private ServerImpl si;

    private ChildControllerTest() throws RemoteException {
        si = new ServerImpl();
    }


    @Test
    void testAddChild() throws RemoteException{
        ArrayList<String> allergies = new ArrayList<>();
        assertTrue(si.addData("AAA", "BBB", "0", LocalDate.parse("1992-11-11"), "CCC", "DDD", "EEE", "00000", "FF", allergies, "GG", "HH", "1", "II", "1111", LocalDate.parse("1968-11-11"), "LL", "MM", "12345", "NN", true, false, false));
    }

    @Test
    void testNullAddChild(){
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.addData(null, null, null,null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, false, false,  false );
        });
    }

    @Test
    void testNoContactAddChild(){
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.addData("AAA", "BBB", "1",LocalDate.parse("1991-11-11"), "AAA", "ABB", "BCC", "1234", "AS",null, null, null, null, null, null, null, null, null, null, null, false, false,  false );
        });
    }

    @Test
    void testUpdateChild() throws RemoteException {
        ArrayList<String> allergies = new ArrayList<>();
        assertTrue(si.updateChild("BBBB", "OK", "0", "3", LocalDate.parse("1992-11-11"), "CCC", "DDD", "EEE", "00000", "FF", allergies));
    }


    @Test
    void testNullUpdateChild(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.updateChild(null, null, null, null, null, null, null, null, null, null, null);
        });
    }

    @Test
    void testNullDeleteChild() throws RemoteException {
        assertFalse(si.deleteChild(null));
    }

    @Test
    void testDeleteChild() throws RemoteException {
       assertTrue( si.deleteChild("3"));
    }

    @Test
    void testDeleteContact() throws RemoteException{
        assertTrue(si.deleteContact("1"));
    }




}