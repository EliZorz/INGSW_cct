package application.Test;

import application.details.IngredientsDbDetails;
import application.details.SpecialDbDetails;
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

public class MenuControllerTest {

    private ServerImpl si;

    private MenuControllerTest() throws RemoteException {
        si = new ServerImpl();
    }

    @Test
    void testAddIngredients() throws RemoteException{
        assertTrue(si.addDataSupplier("ABC", "76", "IO","1234", "ASDF", "12345", "AS"));
        assertTrue(si.addIngrToDb("EEE", "76"));
        assertTrue(si.addIngrToDb("AA", "76"));

    }

    @Test
    void testSaveIngredientsDish() throws RemoteException{
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("EEE");
        assertTrue(si.saveIngredients("WWW", ingredients));
        assertTrue(si.saveIngredients("aa", ingredients));
        assertTrue(si.saveIngredients("bb", ingredients));
        assertTrue(si.saveIngredients("cc", ingredients));
        ingredients.add("AA");
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
        assertFalse(si.controllDate(LocalDate.parse("2200-11-11")));
    }

    @Test
    void testNullControllData() throws RemoteException{
        assertTrue(si.controllDate(LocalDate.parse("2222-11-11")));
    }


    @Test
    void testUpdatMenu() throws RemoteException{
        assertTrue(si.updateMenu("11","bb", "aa", "WWW", "cc", "ee", LocalDate.parse("2200-11-11"),LocalDate.parse("2200-11-11") ));
    }

    @Test
    void testNullUpdate(){
        Assertions.assertThrows(NullPointerException.class,()->{
            si.updateMenu(null, null, null, null, null, null,LocalDate.parse(null), LocalDate.parse(null));
        });
    }

    //////SPECIAL MENU TEST

    @Test
    void testAddChildWithAllergies() throws RemoteException{
        ArrayList<String> allergies = new ArrayList<>();
        allergies.add("AA");
        assertTrue(si.addData("AAA", "BBB", "B1", LocalDate.parse("1992-11-11"), "CCC", "DDD", "EEE", "00000", "FF", allergies, "GG", "HH", "1", "II", "1111", LocalDate.parse("1968-11-11"), "LL", "MM", "12345", "NN", true, false, false));
    }

    @Test
    void testloaInterniWithAllergies() throws RemoteException{
        assertNotNull(si.loadInterniWithAllergies(LocalDate.parse("2200-11-11")));
    }

    @Test
    void testLoadInterniWithAllergiesNoData(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.loadInterniWithAllergies(LocalDate.parse(null));
        });
    }

    @Test
    void testAddSpecialMenu()throws RemoteException{
        SpecialDbDetails special = new SpecialDbDetails("B1", "AA");
        assertTrue(si.addSpecialMenu( "aa", "bb", "cc", null, "WWW", LocalDate.parse("2200-11-11"), special));
    }

    @Test
    void testNullAddSPecialMenu(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.addSpecialMenu(null, null, null, null, null, null, null);
        });
    }

    @Test
    void testLoadSpecialMenu() throws RemoteException{
        assertNotNull(si.loadSpecialMenu());
    }

    @Test
    void testUpdateSpecialMenu() throws RemoteException{
        SpecialDbDetails special = new SpecialDbDetails("B1", "AA");
        assertTrue(si.updateSpecialMenu( "bb", "aa", "cc", null, "WWW", LocalDate.parse("2200-11-11"), special));
    }

    @Test
    void testNullUpdateSpecialMenu(){
        Assertions.assertThrows(NullPointerException.class, () ->{
            si.updateSpecialMenu(null, null, null, null, null, null, null);
        });
    }


    //DELETE SPECIAL MENU

    @Test
    void testDeleteSpecialMenu() throws RemoteException{
        assertTrue(si.deleteSpecialMenu(LocalDate.parse("2200-11-11"),"B1", "AA" ));
    }

    @Test
    void testNullDeleteSpecialMenu() {
        Assertions.assertThrows(NullPointerException.class,() ->{
            si.deleteSpecialMenu(LocalDate.parse(null),null, null);
        });
    }

    //DELETE MENU

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

    @Test
    void testDeleteInterniandContact() throws RemoteException{
        assertTrue(si.deleteChild("B1"));
        assertTrue(si.deleteContact("1"));
    }



}