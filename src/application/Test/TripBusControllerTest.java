package application.Test;

import application.rmi.server.ServerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

class TripBusControllerTest {
    private ServerImpl si;
    private TripBusControllerTest() throws RemoteException{
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
        si.addDataCoachOperator("OO", "12", "AS", "1234", "AAAAS", "12345", "AB");
        si.addBusToDb("IO", 10, "12");
    }

    @AfterEach
    void deleteDetailsForTrip() throws RemoteException{
        si.deleteTrip("CR", "2220-11-11", "2220-11-13", "AOK", "2220-11-12", "ER");
        si.deleteStaff("CCC111");
        si.deleteContact("1", "A0");
        si.deleteChild("A0");
        si.deleteCoachOperator("12");

    }

    @Test
    void testBusPartecipants() throws RemoteException{
        ArrayList<String> children = new ArrayList<>();
        children.add("A0");
        ArrayList<String> staff = new ArrayList<>();
        staff.add("CCC111");
        assertNotNull(si.associateBusToParticipants(children,1, staff, 1, "CR", "2220-11-11", "2220-11-13", "AOK", "2220-11-12", "ER" ));
        assertNotNull(si.loadBusTrip("CR", "2220-11-11", "2220-11-13", "AOK", "2220-11-12", "ER"));
    }



}