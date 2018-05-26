package application.Test;

import application.rmi.server.ServerImpl;
import com.mysql.jdbc.MysqlDataTruncation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TripPlanControllerTest {
    private ServerImpl si;
    private TripPlanControllerTest() throws RemoteException{
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
        si.addTrip(children,staff, "2220-11-11", "2220-11-12", "2220-11-13", "CR", "ER", "AOK" );
    }

    @AfterEach
    void deleteDetailsForTrip() throws RemoteException{
        si.deleteTrip("CR", "2220-11-11", "2220-11-13", "AOK", "2220-11-12", "ER");
        si.deleteStaff("CCC111");
        si.deleteContact("1", "A0");
        si.deleteChild("A0");
    }


    //LOAD

    @Test
    void testLoadDataTrip() throws RemoteException{
        assertNotNull(si.loadDataTrip());
    }

    @Test
    void tstLoadChildTrip() throws RemoteException{
        assertNotNull(si.loadChildTrip());
    }

    @Test
    void testLoadStaffTrip() throws RemoteException{
        assertNotNull(si.loadStaffTrip());
    }

    //ADD

    @Test
    void testNullAddTrip(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.addTrip(null, null, null, null, null, null, null, null);
        });
    }

    //DELETE
    @Test
    void testNullDelete() throws RemoteException {
        assertFalse(si.deleteTrip(null, null, null, null, null, null));
    }

}