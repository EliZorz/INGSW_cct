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
        assertTrue(si.addData("AAA", "BBB", "A0", LocalDate.parse("1992-11-11"), "CCC", "DDD", "EEE", "00000", "FF", allergies, "GG", "HH", "1", "II", "1111", LocalDate.parse("1968-11-11"), "LL", "MM", "12345", "NN", true, false, false));
    }

    @Test
    void testNullAddChild(){
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.addData(null, null, null,null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, false, false,  false );
        });
    }

    @Test
    void testControllCF() throws RemoteException {
        assertFalse(si.controllCF("0"));
    }

    @Test
    void testNullControllCF() throws RemoteException {
        assertTrue(si.controllCF(null));
    }

    @Test
    void testLoadChild() throws RemoteException {
        assertNotNull(si.loadData());
    }

    @Test
    void testLoadContact() throws RemoteException{
        assertNotNull(si.loadDataContacts("0"));
    }

    @Test
    void testNoContactAddChild(){
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.addData("AAA", "BBB", "1",LocalDate.parse("1991-11-11"), "AAA", "ABB", "BCC", "1234", "AS",null, null, null, null, null, null, null, null, null, null, null, false, false,  false );
        });
    }

    @Test
    void testLoadNullContact() throws RemoteException {
        assertNull(si.loadDataContacts(null));
    }

    @Test
    void testAddContact() throws RemoteException{
        ArrayList<String> children = new ArrayList<>();
        children.add("AAA");
        children.add("BBB");
        children.add("A0");
        children.add("1992-11-11");
        children.add("CCC");
        children.add("EEE");
        children.add("00000");
        children.add("FF");

        assertTrue(si.addContact(children, "OKOK", "AA", "99", "EEE", "1234", LocalDate.parse("1976-11-11"), "AAA", "AA", "00000", "WE", false, false, true));
    }

    @Test
    void testNullChildAddContact() throws RemoteException{
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.addContact(null, "OKOK", "AA", "99", "EEE", "1234", LocalDate.parse("1976-11-11"), "AAA", "AA", "00000", "WE", false, true, false);
        });
    }

    @Test
    void testFalseAddContact() throws RemoteException{
        ArrayList<String> children = new ArrayList<>();
        children.add("AAA");
        children.add("BBB");
        children.add("A0");
        children.add("1992-11-11");
        children.add("CCC");
        children.add("EEE");
        children.add("00000");
        children.add("FF");
        assertFalse(si.addContact(children, "OKOK", "AA", "99", "EEE", "1234", LocalDate.parse("1976-11-11"), "AAA", "AA", "00000", "WE", false, false, false));
    }

    @Test
    void testUpdateChild() throws RemoteException {
        ArrayList<String> allergies = new ArrayList<>();
        assertTrue(si.updateChild("BBBB", "OK", "A0", "29999", LocalDate.parse("1992-11-11"), "CCC", "DDD", "EEE", "00000", "FF", allergies));
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
       assertTrue( si.deleteChild("29999"));
    }

    @Test
    void testDeleteContact() throws RemoteException{
        assertTrue(si.deleteContact("1"));
        assertTrue(si.deleteContact("99"));
    }

    @Test
    void testNullDeleteContact() throws RemoteException{
        assertFalse(si.deleteContact(null));
    }






}