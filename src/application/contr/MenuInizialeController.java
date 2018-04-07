package application.contr;


import application.Inter;
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
    void openInformation(ActionEvent event) throws Exception{
        new GuiNew("Information");
    }

    @FXML
    void openMenu(ActionEvent event) throws Exception{
        new GuiNew("Menu");
    }

    @FXML
    void openChildren(ActionEvent event) throws Exception{
        new GuiNew("Children");
    }

    @FXML
    void openStaff(ActionEvent event) throws Exception{
        new GuiNew("Staff");
    }




    @FXML
    void openMenuBasePlates (ActionEvent event)throws Exception{
        new GuiNew("MenuBasePlates");
    }

    @FXML
    void openSpecialMenu (ActionEvent event)throws Exception{
        new GuiNew("SpecialMenu");
    }


    @FXML
    void openDayTrip(ActionEvent event)throws Exception{
        new GuiNew("DayTrip");

    }



    @FXML

    void openSuppliers (ActionEvent event)throws Exception{

       new GuiNew("Suppliers");
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


