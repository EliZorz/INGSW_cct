package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.TripTableDbDetails;
import application.details.TripTableGuiDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
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
    public TableColumn<TripTableGuiDetails, String> colLayover;
    @FXML
    public TableColumn<TripTableGuiDetails, String> colAccomodation;
    @FXML
    public TableColumn<TripTableGuiDetails, String> colParticipants;
    @FXML
    public TableColumn<TripTableGuiDetails, String> colStaff;
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
        colDeparture.setCellValueFactory(cellData -> cellData.getValue().depProperty());
        colArrival.setCellValueFactory(cellData->cellData.getValue().arrProperty());
        colComeback.setCellValueFactory(cellData->cellData.getValue().comProperty());
        colLayover.setCellValueFactory(cellData->cellData.getValue().layoverProperty());
        colAccomodation.setCellValueFactory(cellData->cellData.getValue().accomodationProperty());
        //colParticipants.setCellValueFactory(cellData->cellData.getValue().numChildrenProperty());
        //colStaff.setCellValueFactory(cellData->cellData.getValue().numStaffProperty());

        //COME INIZIALIZZO COL_PARTICIPANTS, COL_STAFF???????

        tableTrip.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableTrip.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTrip.add(newSelection.getDep());
                selectedTrip.add(newSelection.getArr());
                selectedTrip.add(newSelection.getCom());
                selectedTrip.add(newSelection.getLayover());
                selectedTrip.add(newSelection.getAccomodation());
                //selectedTrip.add(newSelection.getNumChildren());
                //selectedTrip.add(newSelection.getNumStaff());
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
    }

    @FXML
    public void handleDelete() {

    }

    public void renameLabel(String st){
        lblStatus.setText(st);
    }
}
