package application.contr;

import application.gui.GuiNew;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;


/**
 * Created by ELISA on 12/04/2018.
 */
public class IngredientsController {

    @FXML
    public Button btnDoneSelect;
    @FXML
    public ListView listIngredients;

    public void handleSelectIngredients(){
        //salva selezionati in lista e ritorna lista
        ObservableList<String> ingrArrayList = listIngredients.getSelectionModel().getSelectedItems();
        for(String s : ingrArrayList){
            System.out.println("selected item " + s);
        }

    }

}
