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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.ChildDbDetails;
import application.details.ChildGuiDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    private ObservableList<DishesDetails> menu = FXCollections.observableArrayList();

    @FXML
    public Button handleLoad;

    @FXML
    public Button createMenu;

    @FXML
    public Button noMenu;

    @FXML
    public Button updateMenu;

    @FXML
    public Button backHome;

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
    public TableColumn<DishesDetails,String> colDay;

    @FXML
    public TableColumn<DishesDetails,String> colSide;

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
    }


    @FXML
    public void handleLoad(ActionEvent event) {
        if (MainControllerLogin.selected.equals("RMI")) {
            System.out.println("oper RMI menu");
            try {
                UserRemote u = Singleton.getInstance().methodRmi();
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
        } else {
            System.out.println("open SOCKET menu");
            try {
                UserRemote u = Singleton.getInstance().methodSocket(); //devo modificarla perché così crea solo nuove socket inutilmente
                ArrayList<DishesDbDetails> dishesDbArrayList =  u.loadMenu();
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
    public void backHome(ActionEvent event)throws Exception{
        //.setOnAction(e -> Platform.exit());

        //System.exit(0);  //in questo modo chiudo tutto
        // Platform.exit(); altro modo per chiudere tutto
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("Menu");

    }

}

