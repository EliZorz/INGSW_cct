package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.SolutionDbDetails;
import application.details.SolutionGuiDetails;
import application.details.TripTableDbDetails;
import application.details.TripTableGuiDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by ELISA on 09/05/2018.
 */
public class TripSolutionBusController  implements Initializable{
    private ObservableList<SolutionGuiDetails> dataObsList = FXCollections.observableArrayList();
    private ObservableList<TripTableGuiDetails> tripObsList = FXCollections.observableArrayList();

    String selectedTripDepFrom = new String();
    String selectedTripDep = new String();
    String selectedTripCom = new String();
    String selectedTripAccomodation = new String();
    String selectedTripArrTo = new String();
    String selectedTripArr = new String();


    @FXML
    public TableView<SolutionGuiDetails> tableWho;
    @FXML
    public TableColumn<SolutionGuiDetails, String> colName;
    @FXML
    public TableColumn<SolutionGuiDetails, String> colSurname;
    @FXML
    public TableColumn<SolutionGuiDetails, String> colCf;
    @FXML
    public TableColumn<SolutionGuiDetails, String> colPlate;
    @FXML
    public Button btnHide;
    @FXML
    public Button btnLoad;

    @FXML
    public TableView<TripTableGuiDetails> tableTrip;
    @FXML
    public TableColumn<TripTableGuiDetails, String> colDeparture;
    @FXML
    public TableColumn<TripTableGuiDetails, String> colArrival;
    @FXML
    public TableColumn<TripTableGuiDetails, String> colComeback;
    @FXML
    public TableColumn<TripTableGuiDetails, String> colAccomodation;
    @FXML
    public TableColumn<TripTableGuiDetails, String> colDepFrom;
    @FXML
    public TableColumn<TripTableGuiDetails, String> colArrivalTo;
    @FXML
    public Button btnLoadTrip;

    @FXML
    public Label lblWarning;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colDepFrom.setCellValueFactory(cellData->cellData.getValue().depFromProperty());
        colDeparture.setCellValueFactory(cellData -> cellData.getValue().depProperty());
        colComeback.setCellValueFactory(cellData->cellData.getValue().comProperty());
        colAccomodation.setCellValueFactory(cellData->cellData.getValue().accomodationProperty());
        colArrival.setCellValueFactory(cellData->cellData.getValue().arrProperty());
        colArrivalTo.setCellValueFactory(cellData->cellData.getValue().arrToProperty());

        tableTrip.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableTrip.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTripDepFrom = newSelection.getDepFrom();
                selectedTripDep = newSelection.getDep();
                selectedTripCom = newSelection.getCom();
                selectedTripAccomodation = newSelection.getAccomodation();
                selectedTripArr = newSelection.getArr();
                selectedTripArrTo = newSelection.getArrTo();
            }
        });
        tableTrip.getItems().clear();


        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colSurname.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        colCf.setCellValueFactory(cellData -> cellData.getValue().cfProperty());
        colPlate.setCellValueFactory(cellData -> cellData.getValue().codRifProperty());

        tableWho.getItems().clear();
    }

    public void handleHide(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void handleLoadTrip() {
        System.out.println("Loading data...");
        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup
            ArrayList<TripTableDbDetails> tripDbArrayList = u.loadDataTrip();  //already existing

            tripObsList.clear();

            if (tripDbArrayList != null){
                for(TripTableDbDetails c : tripDbArrayList){
                    TripTableGuiDetails tmp = new TripTableGuiDetails(c);
                    tripObsList.add(tmp);
                }

                tableTrip.setItems(null);
                tableTrip.setItems(tripObsList);
                this.renameLabel("Table loaded!");
            }else{
                this.renameLabel("Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void handleLoadSolution() {
        System.out.println("Loading data...");
        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup
            ArrayList<SolutionDbDetails> tripDbArrayList = u.loadSolution(selectedTripDepFrom, selectedTripDep, selectedTripCom, selectedTripAccomodation, selectedTripArr, selectedTripArrTo);  //call method in Server Impl

            dataObsList.clear();

            if (tripDbArrayList != null){
                for(SolutionDbDetails c : tripDbArrayList){
                    SolutionGuiDetails tmp = new SolutionGuiDetails(c);
                    dataObsList.add(tmp);
                }

                tableWho.setItems(null);
                tableWho.setItems(dataObsList);
                this.renameLabel("Table loaded!");
            }else{
                this.renameLabel("Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void renameLabel(String st){
        lblWarning.setText(st);
    }


}
