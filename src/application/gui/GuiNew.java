package application.gui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Created by ELISA on 22/03/2018.
 */
public class GuiNew  {

    public GuiNew(String what) throws IOException {

        Parent p = (Parent) FXMLLoader.load(getClass().getResource(what+".fxml"));

        Stage stage = new Stage();
        stage.setTitle(what);
        stage.setResizable(false);
        stage.setScene(new Scene(p));
        stage.show();
    }






}
