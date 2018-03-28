package application.contr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

/**
 * Created by ELISA on 22/03/2018.
 */
public class GuiNew {
    public void openFxml (String what) throws IOException {
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(what));
                Parent p = fxmlLoader.load();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Stage stage = new Stage();
                stage.setScene(new Scene(p,screenSize.getWidth(),screenSize.getHeight()));
                stage.show();

            } catch (IOException ioe){
                ioe.printStackTrace();
            }



    }
}
