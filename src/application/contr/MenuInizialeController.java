package application.contr;


import application.gui.GuiNew;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuInizialeController {


    @FXML
    void MenuBase()throws Exception{
        new GuiNew("MenuBasePlates");
    }

    @FXML
    void MenusSpeciale()throws Exception{
        new GuiNew("MenuSpeciale");
    }

    @FXML
    void GiteDisponibili()throws Exception{
        new GuiNew("GiteDisponibili");
    }

    @FXML
    void NuovaGita()throws Exception{
        new GuiNew("NuovaGita");

    }


    @FXML
    void fornitore()throws Exception{
        new GuiNew("Fornitore");

    }
    @FXML
    void bambini() throws Exception{
        new GuiNew("Bambini");

    }

    @FXML
    void PersonaleInterno()throws Exception{
       new GuiNew("PersonaleInterno");

    }


    @FXML
    public void ReturnToHomePage(ActionEvent event)throws Exception{
        //.setOnAction(e -> Platform.exit());

        //System.exit(0);  //in questo modo chiudo tutto
       // Platform.exit(); altro modo per chiudere tutto
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void initialize() {


    }



}


