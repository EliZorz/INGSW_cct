package application.Test;

import application.rmi.server.ServerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TripPartecipantsControllerTest {
    private ServerImpl si;
    private TripPartecipantsControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @BeforeEach
    void addDetailsForTrip() throws RemoteException{
        ArrayList<String> allergies = new ArrayList<>();
        si.addData("AAA", "BBB", "A0", LocalDate.parse("1992-11-11"), "CCC", "DDD", "EEE", "00000", "FF", allergies, "GG", "HH", "1", "II", "1111", LocalDate.parse("1968-11-11"), "LL", "MM", "12345", "NN", true, false, false);
        si.addDataStaff("AAA", "BBB",
                "CCC111", "DDD", LocalDate.parse("1982-11-11"),
                "EEE", "FFF", "GGG",
                "12345", "AB", allergies);
        ArrayList<String> children = new ArrayList<>();
        children.add("A0");
        ArrayList<String> staff = new ArrayList<>();
        staff.add("CCC111");
        si.addTrip(children, staff, "2220-11-11", "2220-11-12", "2220-11-13", "CR", "ER", "AOK" );
    }

    @AfterEach
    void deleteDetailsForTrip() throws RemoteException{
        si.deleteTrip("CR", "2220-11-11", "2220-11-13", "AOK", "2220-11-12", "ER");
        si.deleteStaff("CCC111");
        si.deleteContact("1", "A0");
        si.deleteChild("A0");
    }


    //LOAD PARTECIPANTS

    @Test
    void testLoadPartecipants() throws RemoteException{
        assertNotNull(si.loadTripSelectedChildren("CR", "2220-11-11", "2220-11-13", "AOK", "2220-11-12", "ER"));
        assertNotNull(si.loadTripSelectedStaff("CR", "2220-11-11", "2220-11-13", "AOK", "2220-11-12", "ER"));
    }

    @Test
    void testNullLoadPartecipants() throws RemoteException{
        assertNull(si.loadTripSelectedChildren(null, null, null, null, null, null));
    }

    @Test
    void testLoadWhoTrip() throws RemoteException{
        assertNotNull(si.loadWhoTrip("CR", "2220-11-11", "2220-11-13", "AOK", "2220-11-12", "ER"));
    }

    @Test
    void testNullLoadWhoTrip() throws RemoteException {
        assertNull(si.loadWhoTrip(null, null, null, null, null,null));
    }

    //NOT AVAILABLE

    @Test
    void testNoAvailableChild() throws RemoteException{
        ArrayList<String> partecipants = new ArrayList<>();
        partecipants.add("A0");
        assertNull(si.findNotAvailableChild(partecipants,"2220-11-11", "2220-11-13" ));
        partecipants = new ArrayList<>();
        partecipants.add("1");
        assertNull(si.findNotAvailableStaff(partecipants, "2220-11-11", "2220-11-13"));
    }

    @Test
    void testNullNoAvailableChild(){
        Assertions.assertThrows(NullPointerException.class,() ->{
            si.findNotAvailableStaff(null, null, null);
        });
    }

    //TOTAL PARTECIPANTS

    @Test
    void testHowManyPartecipants() throws RemoteException{
        ArrayList<String> partecipants = new ArrayList<>();
        partecipants.add("A0");
        ArrayList<String> staff = new ArrayList<>();
        staff.add("1");
        assertNotNull(si.howManyActualParticipants(partecipants, staff));
    }

    @Test
    void testNullPartecipants(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.howManyActualParticipants(null, null);
        });
    }







}