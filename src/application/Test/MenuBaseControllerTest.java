package application.Test;

import application.rmi.server.ServerImpl;
import org.junit.jupiter.api.*;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuBaseControllerTest {

    private ServerImpl si;

    private MenuBaseControllerTest() throws RemoteException{
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
        si.saveIngredients("cc", ingredients);
        si.saveIngredients("ee", ingredients);

        si.addMenu("11", "aa", "bb", "cc", "WWW", "ee", LocalDate.parse("2200-11-11"));

    }


    @AfterEach
    void deleteMenu() throws RemoteException{
        si.deleteMenu(LocalDate.parse("2200-11-11"));

        si.deleteSupplier("76", si.loadNoIngr("76"));

    }


    //ADD MENU

    @Test
    void testNullAddMenu(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.addMenu(null, null, null, null, null, null, LocalDate.parse(null));
        });
    }

    //LOAD MENU

    @Test
    void testLoadMenu() throws RemoteException{
        assertNotNull(si.loadMenu());
    }

    //CONTROLL DATA

    @Test
    void testControllData() throws RemoteException{
        assertFalse(si.controllDate(LocalDate.parse("2200-11-11")));
        assertTrue(si.controllDate(LocalDate.parse("2222-11-11")));
    }


    //UPDATE MENU

    @Test
    void testUpdatMenu() throws RemoteException{
        assertTrue(si.updateMenu("11","aa", "bb", "WWW", "cc", "ee", LocalDate.parse("2200-11-11"),LocalDate.parse("2220-11-11") ));
    }

    @Test
    void testNullUpdate(){
        Assertions.assertThrows(NullPointerException.class,()->{
            si.updateMenu(null, null, null, null, null, null,LocalDate.parse(null), LocalDate.parse(null));
        });
    }

    //DELETE MENU

    @Test
    void testNullDeleteMenu(){
        Assertions.assertThrows(NullPointerException.class,()->{
            si.deleteMenu(LocalDate.parse(null));
        });
    }


}