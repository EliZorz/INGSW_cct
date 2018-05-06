package application.contr;

import application.gui.GuiNew;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class SpecialMenuLoadController {

    
    public void exit(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }

    public void addSpecialMenu(ActionEvent event) throws IOException {
        new GuiNew("SpecialMenu");
    }

    public void updateMenu(ActionEvent event) {
    }

    public void deleteMenu(ActionEvent event) {
    }
}
