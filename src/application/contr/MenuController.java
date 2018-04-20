package application.contr;

import application.details.DishesDbDetails;
import application.details.DishesDetails;
import application.gui.GuiNew;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import application.Interfaces.UserRemote;
import application.Singleton;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;

public class MenuController implements Initializable {

    public String selectedMenu;

    private ObservableList<DishesDetails> menu = FXCollections.observableArrayList();

    @FXML
    public Button handleLoad;

    @FXML
    public Button createMenu;

    @FXML
    public Button deleteMenu;

    @FXML
    public Button updateMenu;

    @FXML
    public Button backHome;

    @FXML
    public Label labelStatus;

    @FXML
    public TableColumn<DishesDetails, String> colNumber;

    @FXML
    public TableColumn<DishesDetails, String> colEntree;

    @FXML
    public TableColumn<DishesDetails, String> colMainCourse;

    @FXML
    public TableColumn<DishesDetails, String> colDessert;

    @FXML
    public TableColumn<DishesDetails, String> colDrink;

    @FXML
    public TableColumn<DishesDetails, String> colDay;

    @FXML
    public TableColumn<DishesDetails, String> colSide;

    @FXML
    public TableView<DishesDetails> tableMenu;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colDessert.setCellValueFactory(cellData -> cellData.getValue().dessertProperty());
        colDrink.setCellValueFactory(cellData -> cellData.getValue().drinkProperty());
        colMainCourse.setCellValueFactory(cellData -> cellData.getValue().mainCourseProperty());
        colEntree.setCellValueFactory(cellData -> cellData.getValue().entreeProperty());
        colNumber.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        colDay.setCellValueFactory(cellData -> cellData.getValue().dayProperty());
        colSide.setCellValueFactory(cellData -> cellData.getValue().sideDishProperty());
        tableMenu.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableMenu.getSelectionModel().setCellSelectionEnabled(true);
        tableMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedMenu = (newSelection.getDay());
            }
                }
        );
        tableMenu.getItems().clear();
    }


    @FXML
    public void handleLoad() {
        if (MainControllerLogin.selected.equals("RMI")) {
            System.out.println("oper RMI menu");
            try {
                UserRemote u = Singleton.getInstance().methodRmi();
                ArrayList<DishesDbDetails> dishesDbArrayList = u.loadMenu();
                menu.clear();

                if (dishesDbArrayList != null) {
                    for (DishesDbDetails d : dishesDbArrayList) {
                        DishesDetails tmp = new DishesDetails(d);
                        menu.add(tmp);
                    }
                    tableMenu.setItems(null);
                    tableMenu.setItems(menu);
                    //labelStatus.setText("Table loaded");
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("open SOCKET menu");
            try {
                UserRemote u = Singleton.getInstance().methodSocket(); //devo modificarla perché così crea solo nuove socket inutilmente
                ArrayList<DishesDbDetails> dishesDbArrayList = u.loadMenu();
                if (dishesDbArrayList != null) {
                    for (DishesDbDetails d : dishesDbArrayList) {
                        DishesDetails tmp = new DishesDetails(d);
                        menu.add(tmp);
                    }
                    tableMenu.setItems(null);
                    tableMenu.setItems(menu);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void openCreation(ActionEvent event) throws IOException {
        new GuiNew("newMenu");
    }


    @FXML
    public void esc(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void delete() {
        if (selectedMenu == null)
            labelStatus.setText("Please select a menu");
        else {
            try {
                UserRemote u = Singleton.getInstance().methodRmi();
                System.out.println(LocalDate.parse(selectedMenu));
                boolean deleted = u.deleteMenu(LocalDate.parse(selectedMenu));

                if (deleted) {
                    labelStatus.setText("Delete success!!");
                    handleLoad();
                } else
                    labelStatus.setText("ERROR!! NO DELETE");

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    public void update(ActionEvent event) {
       /* if (selectedMenu == null)
            labelStatus.setText("Please select a menu");
        else {
            try {
                CreationMenuController.updateMenu(selectedMenu);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}

