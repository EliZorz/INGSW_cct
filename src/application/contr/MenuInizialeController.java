package application.contr;


import application.gui.GuiNew;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuInizialeController {


    @FXML
    void openInformation(ActionEvent event) throws Exception{
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("Information");
    }

    @FXML
    void openMenu(ActionEvent event) throws Exception{
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("Menu");
    }

    @FXML
    void openChildren(ActionEvent event) throws Exception{
        new GuiNew("Children");
    }

    @FXML
    void openStaff(ActionEvent event) throws Exception{
        ((Node)(event.getSource())).getScene().getWindow().hide();
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
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("TripMenu");

    }

    @FXML
    void openTableTrips(ActionEvent event) throws Exception {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("TripTable");
    }

    @FXML
    void openPlanTrip(ActionEvent event) throws Exception {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("TripPlan");
    }

    @FXML
    public void openActualParticipants(ActionEvent event) throws Exception {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("TripActualParticipants");
    }

    @FXML
    void openBeforeTrip(ActionEvent event) throws Exception {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("TripBefore");
    }


    @FXML

    void openSuppliers (ActionEvent event)throws Exception{
        ((Node)(event.getSource())).getScene().getWindow().hide();
       new GuiNew("Suppliers");
    }


    @FXML
    public void backHome(ActionEvent event)throws Exception{
        //.setOnAction(e -> Platform.exit());

        //System.exit(0);  //in questo modo chiudo tutto
       // Platform.exit(); altro modo per chiudere tutto
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("MenuIniziale");

    }


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


}


