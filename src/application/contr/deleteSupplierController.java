package application.contr;

import application.details.DishesDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class deleteSupplierController implements Initializable{

    private ObservableList<DishesDetails> dishes = FXCollections.observableArrayList();
    private String[] selectedMenu = new String[7];

    @FXML
    public TextField entreeTF;
    @FXML
    public TextField mainTF;
    @FXML
    public TextField sideTF;
    @FXML
    public TextField dessertTF;
    @FXML
    public TextField drinkTF;
    @FXML
    public Button showEntree;
    @FXML
    public Button showMain;
    @FXML
    public Button showSide;
    @FXML
    public Button showDessert;
    @FXML
    public Button showDrink;
    @FXML
    public TableView<DishesDetails> tabMenu;
    @FXML
    public TableColumn<DishesDetails, String> colNum;
    @FXML
    public TableColumn<DishesDetails, String> colEntree;
    @FXML
    public TableColumn<DishesDetails, String> colMain;
    @FXML
    public TableColumn<DishesDetails, String> colSide;
    @FXML
    public TableColumn<DishesDetails, String> colDrink;
    @FXML
    public TableColumn<DishesDetails, String> colDessert;
    @FXML
    public TableColumn<DishesDetails, String> colDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNum.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        colEntree.setCellValueFactory(cellData -> cellData.getValue().entreeProperty());
        colMain.setCellValueFactory(cellData -> cellData.getValue().mainCourseProperty());
        colSide.setCellValueFactory(cellData -> cellData.getValue().sideDishProperty());
        colDessert.setCellValueFactory(cellData -> cellData.getValue().dessertProperty());
        colDrink.setCellValueFactory(cellData -> cellData.getValue().drinkProperty());
        colDate.setCellValueFactory(cellData -> cellData.getValue().dayProperty());
        tabMenu.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tabMenu.getSelectionModel().setCellSelectionEnabled(false);
        tabMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedMenu = new String[7];
            selectedMenu[0] = newSelection.getNumber();
            selectedMenu[1] = newSelection.getEntree();
            selectedMenu[2] = newSelection.getMainCourse();
            selectedMenu[3] = newSelection.getSideDish();
            selectedMenu[4] = newSelection.getDessert();
            selectedMenu[5] = newSelection.getDrink();
            selectedMenu[6] = newSelection.getDay();
        });
        tabMenu.getItems().clear();
        handleLoad();
    }

    public void handleLoad(){
        
    }
}
