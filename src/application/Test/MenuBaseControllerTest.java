package application.Test;

import application.details.IngredientsDbDetails;
import application.rmi.server.ServerImpl;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MenuBaseControllerTest {

    private ServerImpl si;

    private MenuBaseControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @Test
    void testAddIngredients() throws RemoteException{
        assertTrue(si.addDataSupplier("ABC", "76", "IO","1234", "ASDF", "12345", "AS"));
        assertTrue(si.addIngrToDb("EEE", "76"));

    }

    @Test
    void testSaveIngredientsDish() throws RemoteException{
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("EEE");
        assertTrue(si.saveIngredients("WWW", ingredients));
        assertTrue(si.saveIngredients("aa", ingredients));
        assertTrue(si.saveIngredients("bb", ingredients));
        assertTrue(si.saveIngredients("cc", ingredients));
        assertTrue(si.saveIngredients("ee", ingredients));
    }

    @Test
    void testSearchIngredients() throws RemoteException{
        assertNotNull(si.searchIngredients("WWW"));
    }

    @Test
    void testNullSearchIngredients() throws RemoteException {
        assertNull(si.searchIngredients(null));
    }

    @Test
    void testAddNullIngredientsDish(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.saveIngredients(null, null);
        });
    }

    @Test
    void testAddNullSelectedIngredientsDish() {
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.saveIngredients("AAA", null);
        });
    }

    @Test
    void testAddMenu() throws RemoteException{
        assertTrue(si.addMenu("11", "aa", "bb", "cc", "WWW", "ee", LocalDate.parse("2200-11-11")));
    }

    @Test
    void testNullAddMenu(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.addMenu(null, null, null, null, null, null, null);
        });
    }

    @Test
    void testLoadMenu() throws RemoteException{
        assertNotNull(si.loadMenu());
    }

    @Test
    void testControllData() throws RemoteException{
        assertTrue(si.controllDate(LocalDate.parse("2200-11-11")));
    }

    @Test
    void testNullControllData() throws RemoteException{
        assertFalse(si.controllDate(LocalDate.parse("2222-11-11")));
    }

    @Test
    void testUpdatMenu() throws RemoteException{
        assertTrue(si.updateMenu("11","bb", "aa", "WWW", "cc", "ee", LocalDate.parse("2200-12-11"),LocalDate.parse("2200-11-11") ));
    }

    @Test
    void testNullUpdate(){
        Assertions.assertThrows(NullPointerException.class,()->{
            si.updateMenu(null, null, null, null, null, null,LocalDate.parse(null), LocalDate.parse(null));
        });
    }

    @Test
    void testDeleteMenu() throws RemoteException{
        assertTrue(si.deleteMenu(LocalDate.parse("2200-11-11")));
    }

    @Test
    void testNullDeleteMenu(){
        Assertions.assertThrows(NullPointerException.class,()->{
            si.deleteMenu(LocalDate.parse(null));
        });
    }

    @Test
    void testLoadNoIngr() throws RemoteException{
        assertNotNull(si.loadNoIngr("76"));
    }
    @Test
    void testDeleteDishes() throws RemoteException{
        assertTrue(si.deleteSupplier("76", si.loadNoIngr("76")));
    }


}