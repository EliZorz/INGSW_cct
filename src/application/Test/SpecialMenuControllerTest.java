package application.Test;

import application.details.IngredientsDbDetails;
import application.details.SpecialDbDetails;
import application.rmi.server.ServerImpl;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SpecialMenuControllerTest {

    private ServerImpl si;

    private SpecialMenuControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @BeforeEach
    void addDetailsForMenu() throws RemoteException{
        si.addDataSupplier("ABC", "76", "IO","1234", "ASDF", "12345", "AS");
        si.addIngrToDb("EEE", "76");
        si.addIngrToDb("AA", "76");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("EEE");
        si.saveIngredients("WWW", ingredients);
        si.saveIngredients("aa", ingredients);
        si.saveIngredients("bb", ingredients);
        ingredients = new ArrayList<>();
        ingredients.add("AA");
        si.saveIngredients("cc", ingredients);
        si.saveIngredients("ee", ingredients);

        si.addMenu("11", "aa", "bb", "cc", "WWW", "ee", LocalDate.parse("2200-11-11"));
        ArrayList<String> allergies = new ArrayList<>();
        allergies.add("AA");
        si.addData("AAA", "BBB", "AB1", LocalDate.parse("1992-11-11"), "CCC", "DDD", "EEE", "00000", "FF", allergies, "GG", "HH", "1", "II", "1111", LocalDate.parse("1968-11-11"), "LL", "MM", "12345", "NN", true, false, false);
    }

    @AfterEach
    void deleteDetailsForMenu() throws RemoteException{
        si.deleteMenu(LocalDate.parse("2200-11-11"));
        si.deleteContact("1", "AB1");
        si.deleteChild("AB1");
        si.deleteSupplier("76", si.loadNoIngr("76"));
    }




    //LOAD THIS MENU AND ADD

    @Test
    void testLoadThisMenu() throws RemoteException{
        assertNotNull(si.loadThisMenu(LocalDate.parse("2200-11-11")));
        assertNull(si.loadThisMenu(null));
        assertFalse(si.addSpecialMenu(null, null, null, null, null, null, null));

    }



    //LOAD INTERNI ALLERGIE

    @Test
    void testloaInterniWithAllergies() throws RemoteException{
        assertNotNull(si.loadInterniWithAllergies(LocalDate.parse("2200-11-11")));
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.loadInterniWithAllergies(LocalDate.parse(null));
        });
    }


    //UPDATE MENU

    @Test
    void testUpdateSpecialMenu() throws RemoteException{
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.updateSpecialMenu(null, null, null, null, null, LocalDate.parse(null), null);
        });
    }

    //DELETE SPECIAL MENU

    @Test
    void testNullDeleteSpecialMenu() throws RemoteException {
       assertFalse(si.deleteSpecialMenu(LocalDate.parse("2200-11-11"),null, null));
    }











}