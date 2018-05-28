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
        si = new ServerImpl();      //chiamo i metodi sull'impl dell'interfaccia
    }

    @BeforeEach //prima di ogni test crea bambino
    void addChild() throws RemoteException{
        ArrayList<String> allergies = new ArrayList<>();
        si.addData("AAA", "BBB", "A0", LocalDate.parse("1992-11-11"), "CCC", "DDD", "EEE", "00000", "FF", allergies, "GG", "HH", "1", "II", "1111", LocalDate.parse("1968-11-11"), "LL", "MM", "12345", "NN", true, false, false);

    }

    @AfterEach  //dopo ogni test cancella bambino, contatto
    void delete() throws RemoteException{
        si.deleteContact("1", "A0");
        si.deleteContact("99", "A0");
        si.deleteChild("A0");
        si.deleteChild("29999");
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
        assertFalse(si.controllCF("A0"));       //false: non esiste quest CF in db

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
    void testNoContactAddChild(){       //aggiungo contatto senza controllare nessun bambino
        Assertions.assertThrows(NullPointerException.class, () -> {
            si.addData("AAA", "BBB", "1",LocalDate.parse("1991-11-11"), "AAA", "ABB", "BCC", "1234", "AS",null, null, null, null, null, null, null, null, null, null, null, false, false,  false );
        });
    }

    @Test
    void testAddContact() throws RemoteException{
        assertTrue(si.addContact("A0", "OKOK", "AA", "99", "EEE", "1234", LocalDate.parse("1976-11-11"), "AAA", "AA", "00000", "WE", false, false, true));
        assertFalse(si.addContact(null, "OKOK", "AA", "99", "EEE", "1234", LocalDate.parse("1976-11-11"), "AAA", "AA", "00000", "WE", false, true, false));

    }


    //LOAD CONTACT

    @Test
    void testLoadContact() throws RemoteException{
        assertNotNull(si.loadDataContacts("A0"));
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
        assertTrue(si.updateChild("BBBB", "OK", "2999", "A0", LocalDate.parse("1992-11-11"), "CCC", "DDD", "EEE", "00000", "FF", allergies));

    }


    @Test
    void testNullUpdateChild() throws RemoteException {
        assertFalse(si.updateChild(null, null, null, null, null, null, null, null, null, null, null));

    }



    //UPDATE CONTACT

    @Test
    void testUpdateContact() throws RemoteException {
        assertTrue(si.updateContact( "OKOK", "AA", "1", "990","A0", "OKOK", "123", LocalDate.parse("1976-11-11"),"AAA", "AA", "00000", "WE", false, false, true));
        assertTrue(si.updateContact( "OKOK", "AA", "990", "1","A0", "OKOK", "123", LocalDate.parse("1976-11-11"),"AAA", "AA", "00000", "WE", false, false, true));

    }

    @Test
    void testNullUpdateContact() throws RemoteException {
        assertFalse(si.updateContact( "OKOK", "AA", "99", null,"A0", "AOK","1234", LocalDate.parse("1976-11-11"), "AAA", "AA", "00000", "WE", false, false, true));

    }


    //DELETE CHILD

    @Test
    void testNullDeleteChild() throws RemoteException {
        assertFalse(si.deleteChild(null));
    }



    //DELETE CONTACT

    @Test
    void testNullDeleteContact() throws RemoteException{
        assertFalse(si.deleteContact(null, null));
    }





}