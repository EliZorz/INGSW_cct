package application.Test;

import application.rmi.server.ServerImpl;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MenuIngredientsControllerTest {

    private ServerImpl si;

    private MenuIngredientsControllerTest() throws RemoteException{
        si = new ServerImpl();
    }


    @BeforeEach
    void addDetailsIngredients() throws RemoteException{
        si.addDataSupplier("ABC", "76", "IO","1234", "ASDF", "12345", "AS");
        si.addIngrToDb("EEE", "76");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("EEE");
        si.saveIngredients("WWW", ingredients);
    }

    @AfterEach
    void deleteDetailsIngredients() throws RemoteException{
        si.deleteSupplier("76", si.loadNoIngr("76"));
    }

    //SEARCH INGREDIENTS

    @Test
    void testSearchIngredients() throws RemoteException {
        assertNotNull(si.searchIngredients("WWW"));
    }

    @Test
    void testNullSearchIngredients() throws RemoteException {
        assertNull(si.searchIngredients(null));
    }

    //SAVE INGREDIENTS

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
}