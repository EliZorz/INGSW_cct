package application.Test;


import application.rmi.server.ServerImpl;
import org.junit.jupiter.api.*;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ChildControllerTest {

    private ServerImpl si;

    private ChildControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @BeforeEach
    void addChild() throws RemoteException{
        ArrayList<String> allergies = new ArrayList<>();
        si.addData("AAA", "BBB", "A0", LocalDate.parse("1992-11-11"), "CCC", "DDD", "EEE", "00000", "FF", allergies, "GG", "HH", "1", "II", "1111", LocalDate.parse("1968-11-11"), "LL", "MM", "12345", "NN", true, false, false);

    }

    //ADD CHILD

    @Test
    void testNullAddChild(){
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.addData(null, null, null,null, null, null, null, null, null,null, null, null, null, null, null, null, null, null, null, null, false, false,  false );
        });
    }

    //CONTROLL FISCAL CODE TO HAVE NO DOUBLE FC IN DB
    @Test
    void testControllCF() throws RemoteException {
        assertFalse(si.controllCF("A0"));
    }

    @Test
    void testNullControllCF() throws RemoteException {
        assertTrue(si.controllCF(null));
    }

    //LOAD CHILD
    @Test
    void testLoadChild() throws RemoteException {
        assertNotNull(si.loadData());
    }

    //ADD CONTACT

    @Test
    void testNoContactAddChild(){
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.addData("AAA", "BBB", "1",LocalDate.parse("1991-11-11"), "AAA", "ABB", "BCC", "1234", "AS",null, null, null, null, null, null, null, null, null, null, null, false, false,  false );
        });
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

    //LOAD CONTACT

    @Test
    void testLoadContact() throws RemoteException{
        assertNotNull(si.loadDataContacts("0"));
    }

    @Test
    void testLoadNullContact() throws RemoteException {
        assertNull(si.loadDataContacts(null));
    }


    //UPDATE CHILD

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



    //UPDATE CONTACT

    @Test
    void testUpdateContact() throws RemoteException {
        assertTrue(si.updateContact( "OKOK", "AA", "99", "990","EEE", "1234", LocalDate.parse("1976-11-11"), "AAA", "AA", "00000", "WE", 0, 0, 1));
    }

    @Test
    void testNullUpdateContact() throws RemoteException {
        assertFalse(si.updateContact( "OKOK", "AA", "99", null,"EEE", "1234", LocalDate.parse("1976-11-11"), "AAA", "AA", "00000", "WE", 0, 0, 1));

    }


    //DELETE CHILD

    @Test
    void testNullDeleteChild() throws RemoteException {
        assertFalse(si.deleteChild(null));
    }



    //DELETE CONTACT

    @Test
    void testDeleteContact() throws RemoteException{
        assertTrue(si.deleteContact("1"));
        assertTrue(si.deleteContact("99"));
    }

    @Test
    void testNullDeleteContact() throws RemoteException{
        assertFalse(si.deleteContact(null));
    }

    @AfterEach
    void delete() throws RemoteException{
        si.deleteContact("1");
        si.deleteContact("99");
        si.deleteChild("A0");
        si.deleteChild("29999");
    }




}