package application.contr;


import application.Interfaces.UserRemote;
import application.LookupCall;
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
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("Children");
    }

    @FXML
    void openStaff(ActionEvent event) throws Exception{
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("Staff");
    }


    @FXML
    void openMenuBasePlates (ActionEvent event)throws Exception{
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("MenuBasePlates");
    }

    @FXML
    void openSpecialMenu (ActionEvent event)throws Exception{
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("LoadSpecialMenu");
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
    void handleOpenCoachOperators (ActionEvent event)throws Exception{
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("CoachOperators");
    }


    @FXML
    public void backHome(ActionEvent event)throws Exception{
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("MenuIniziale");

    }

    @FXML
    public void logout(ActionEvent event) throws Exception {
        if(MainControllerLogin.selected.equals("RMI")) {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            new GuiNew("LoginUser");
        } else {
            UserRemote u = LookupCall.getInstance().methodSocket();
            boolean result = u.logout();
            if (result){
                ((Node)(event.getSource())).getScene().getWindow().hide();
                new GuiNew("LoginUser");
            }else{
                System.out.println("Something went wrong.");
            }
        }

    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


}