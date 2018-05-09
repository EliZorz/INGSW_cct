package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.TripTableDbDetails;
import application.details.TripTableGuiDetails;
import application.gui.GuiNew;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by ELISA on 22/04/2018.
 */
public class TripTableController implements Initializable{
    private ObservableList<TripTableGuiDetails> dataObsList = FXCollections.observableArrayList();

    ArrayList<String> selectedTrip = new ArrayList<>();
    
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
    public Button btnBack;
    @FXML
    public Button btnDeleteSelected;
    @FXML
    public Label lblStatus;


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
                selectedTrip.add(newSelection.getDepFrom());
                selectedTrip.add(newSelection.getDep());
                selectedTrip.add(newSelection.getCom());
                selectedTrip.add(newSelection.getAccomodation());
                selectedTrip.add(newSelection.getArr());
                selectedTrip.add(newSelection.getArrTo());
            }
        });
        tableTrip.getItems().clear();
    }

    @FXML
    public void handleLoad() {
        System.out.println("Loading data...");
        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup
            ArrayList<TripTableDbDetails> tripDbArrayList = u.loadDataTrip();  //call method in Server Impl

            dataObsList.clear();

            if (tripDbArrayList != null){
                for(TripTableDbDetails c : tripDbArrayList){
                    TripTableGuiDetails tmp = new TripTableGuiDetails(c);
                    dataObsList.add(tmp);
                }
                tableTrip.setItems(null);
                tableTrip.setItems(dataObsList);
                this.renameLabel("Table loaded!");
            }else{
                this.renameLabel("Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBack() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
        try{
            new GuiNew("TripMenu");
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    @FXML
    public void handleDelete() {
        String stringDep = selectedTrip.get(1);
        String stringArr = selectedTrip.get(4);
        String stringCom = selectedTrip.get(2);

        System.out.println("Loading data...");
        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup
            boolean deleted = u.deleteTrip(selectedTrip.get(0), stringDep, stringCom, selectedTrip.get(3), stringArr, selectedTrip.get(5));
            if(deleted){
                this.renameLabel("Deleted.");
                selectedTrip.clear();
            } else {
                this.renameLabel("Error deleting.");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void renameLabel(String st){
        lblStatus.setText(st);
    }
}
