package application.contr;


import application.gui.GuiNew;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;


import java.awt.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuInizialeController {



    @FXML
    void MenuBase(ActionEvent event)throws Exception{
        new GuiNew("MenuBasePlates");
    }

    @FXML
    void MenusSpeciale(ActionEvent event)throws Exception{
        new GuiNew("MenuSpeciale");
    }

    @FXML
    void GiteDisponibili(ActionEvent event)throws Exception{
        new GuiNew("GiteDisponibili");
    }

    @FXML
    void NuovaGita(ActionEvent event)throws Exception{
        new GuiNew("NuovaGita");

    }


    @FXML
    void fornitore(ActionEvent event)throws Exception{
            new GuiNew("Fornitore");

    }
@FXML
    void bambini(ActionEvent event) throws Exception{
            new GuiNew("Bambini");

    }
    @FXML

    void PersonaleInterno(ActionEvent event)throws Exception{

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


