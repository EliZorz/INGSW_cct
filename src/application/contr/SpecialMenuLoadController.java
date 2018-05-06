package application.contr;

import application.details.SpecialMenuGuiDetails;
import application.gui.GuiNew;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SpecialMenuLoadController implements Initializable{

    private ObservableList<SpecialMenuGuiDetails> specialMenu = FXCollections.observableArrayList();

    @FXML
    public Button newMenu;

    @FXML
    public Button delete;

    @FXML
    public Button update;

    @FXML
    public Button load;

    @FXML
    public Label labelStatus;

    @FXML
    public TableColumn<SpecialMenuGuiDetails, String> colDate;

    @FXML
    public TableColumn<SpecialMenuGuiDetails, String> colEntree;

    @FXML
    public TableColumn<SpecialMenuGuiDetails, String> colMain;

    @FXML
    public TableColumn<SpecialMenuGuiDetails, String> colDessert;

    @FXML
    public TableColumn<SpecialMenuGuiDetails, String> colDrink;

    @FXML
    public TableColumn<SpecialMenuGuiDetails, String> colSide;

    @FXML
    public TableColumn<SpecialMenuGuiDetails, String> colFC;

    @FXML
    public TableColumn<SpecialMenuGuiDetails, String> colAllergies;

    @FXML
    public TableView<SpecialMenuGuiDetails> tabSpecialMenu;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colDate.setCellValueFactory(cellData-> cellData.getValue().dateProperty());
    }


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
