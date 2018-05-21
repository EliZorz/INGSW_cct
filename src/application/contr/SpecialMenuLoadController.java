package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.SpecialDbDetails;
import application.details.SpecialMenuDbDetails;
import application.details.SpecialMenuGuiDetails;
import application.gui.GuiNew;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SpecialMenuLoadController implements Initializable{

    private ObservableList<SpecialMenuGuiDetails> specialMenu = FXCollections.observableArrayList();
    private  ObservableList<SpecialMenuGuiDetails> searchedMenu = FXCollections.observableArrayList();

    private String[] selectedMenu = new String[8];

    @FXML
    public Button newMenu;

    @FXML
    public Button delete;

    @FXML
    public Button update;

    @FXML
    public Button load;

    @FXML
    public Button deselect;

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

    @FXML
    public Button searchButton;

    @FXML
    public Button back;

    @FXML
    public DatePicker dateSearch;


    UserRemote  u;

    public SpecialMenuLoadController(){
        if(MainControllerLogin.selected.equals("RMI"))
            u= Singleton.getInstance().methodRmi();
        else
            u= Singleton.getInstance().methodSocket();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colDate.setCellValueFactory(cellData-> cellData.getValue().dateProperty());
        colEntree.setCellValueFactory(cellData-> cellData.getValue().entreeProperty());
        colMain.setCellValueFactory(cellData-> cellData.getValue().mainCourseProperty());
        colSide.setCellValueFactory(cellData-> cellData.getValue().sideDishProperty());
        colDrink.setCellValueFactory(cellData-> cellData.getValue().drinkProperty());
        colDessert.setCellValueFactory(cellData-> cellData.getValue().dessertProperty());
        colFC.setCellValueFactory(cellData-> cellData.getValue().FCProperty());
        colAllergies.setCellValueFactory(cellData-> cellData.getValue().allergiesProperty());

        tabSpecialMenu.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tabSpecialMenu.getSelectionModel().setCellSelectionEnabled(false);
        tabSpecialMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                selectedMenu = new String[8];
                selectedMenu[0] = newSelection.getDate();
                selectedMenu[1] = newSelection.getEntree();
                selectedMenu[2] = newSelection.getMain();
                selectedMenu[3] = newSelection.getDessert();
                selectedMenu[4] = newSelection.getSide();
                selectedMenu[5] = newSelection.getDrink();
                selectedMenu[6] = newSelection.getFC();
                selectedMenu[7] = newSelection.getAllergies();
            }
        });
        tabSpecialMenu.getItems().clear();
        loadMenu();

    }


    public void exit(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }

    public void addSpecialMenu(ActionEvent event) throws IOException {
        SpecialMenuController.selectedMenu = null;
        selectedMenu = null;
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("SpecialMenu");
    }

    public void updateMenu(ActionEvent event) throws IOException {
        if(selectedMenu != null) {
            SpecialMenuController.selectedMenu = selectedMenu;
            ((Node)(event.getSource())).getScene().getWindow().hide();
            new GuiNew("SpecialMenu");
        }
        else labelStatus.setText("Please select a menu");
    }


    public void deselect(){
        tabSpecialMenu.getSelectionModel().clearSelection();
        selectedMenu = null;
    }


    public void deleteMenu(ActionEvent event) {
        if (selectedMenu == null)
            labelStatus.setText("Please select a menu");
        else {
            try {
                boolean deleted = u.deleteSpecialMenu(LocalDate.parse(selectedMenu[0]), selectedMenu[6], selectedMenu[7]);
                if (deleted) {
                    labelStatus.setText("Delete success!!");
                    loadMenu();
                } else
                    labelStatus.setText("ERROR!! NO DELETE");

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadMenu() {
        try{
            ArrayList<SpecialMenuDbDetails> specialDbArrayList = u.loadSpecialMenu();
            specialMenu.clear();
            if(specialDbArrayList != null) {
                for (SpecialMenuDbDetails x : specialDbArrayList) {
                    SpecialMenuGuiDetails tmp = new SpecialMenuGuiDetails(x);
                    specialMenu.add(tmp);
                }
                if(specialDbArrayList.isEmpty()) labelStatus.setText("No special menu in the DB");
                else labelStatus.setText("Loaded");
            }

            tabSpecialMenu.setItems(null);
            tabSpecialMenu.setItems(specialMenu);
            selectedMenu = null;

        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    public void search(){
        searchedMenu = FXCollections.observableArrayList();
        if(dateSearch.getValue() != null){
            for(SpecialMenuGuiDetails x : specialMenu ){
                if(LocalDate.parse(x.getDate()).isEqual(dateSearch.getValue()))
                    searchedMenu.add(x);
            }
            tabSpecialMenu.setItems(null);
            tabSpecialMenu.setItems(searchedMenu);
        }
        else{
            tabSpecialMenu.setItems(null);
            tabSpecialMenu.setItems(specialMenu);
        }
    }

    public void reLoad(){
        searchedMenu = FXCollections.observableArrayList();
        dateSearch.setValue(null);
        tabSpecialMenu.setItems(null);
        tabSpecialMenu.setItems(specialMenu);
    }
}