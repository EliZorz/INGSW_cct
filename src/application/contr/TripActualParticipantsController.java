package application.contr;


import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.*;
import application.gui.GuiNew;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by ELISA on 23/04/2018.
 */
public class TripActualParticipantsController implements Initializable {
    private ObservableList<TripTableGuiDetails> tripObsList = FXCollections.observableArrayList();
    private ObservableList<ChildSelectedTripGuiDetails> actualChildrenObsList = FXCollections.observableArrayList();
    private ObservableList<StaffSelectedTripGuiDetails> actualStaffObsList = FXCollections.observableArrayList();

    ArrayList<String> selectedTrip = new ArrayList<>();
    ArrayList<String> selectedChild = new ArrayList<>();
    ArrayList<String> selectedStaff = new ArrayList<>();


    @FXML
    public TableView<ChildSelectedTripGuiDetails> tableActualChildren;
    @FXML
    public TableColumn<ChildSelectedTripGuiDetails, String> colNameChild;
    @FXML
    public TableColumn<ChildSelectedTripGuiDetails, String> colSurnameChild;
    @FXML
    public TableColumn<ChildSelectedTripGuiDetails, String> colCfChild;

    @FXML
    public TableView<StaffSelectedTripGuiDetails> tableActualStaff;
    @FXML
    public TableColumn<StaffSelectedTripGuiDetails, String> colNameStaff;
    @FXML
    public TableColumn<StaffSelectedTripGuiDetails, String> colSurnameStaff;
    @FXML
    public TableColumn<StaffSelectedTripGuiDetails, String> colCfStaff;

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
    public Label lblTotChildren;
    @FXML
    public Label lblTotStaff;
    @FXML
    public Button btnLoadChildren;
    @FXML
    public Button btnDoneSelection;
    @FXML
    public Button btnBack;
    @FXML
    public Button btnLoadStaff;
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
                selectedTrip.add(newSelection.getDepFrom());
                selectedTrip.add(newSelection.getDep());
                selectedTrip.add(newSelection.getCom());
                selectedTrip.add(newSelection.getAccomodation());
                selectedTrip.add(newSelection.getArr());
                selectedTrip.add(newSelection.getArrTo());

            }
        });
        tableTrip.getItems().clear();


        colNameChild.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colSurnameChild.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        colCfChild.setCellValueFactory(cellData -> cellData.getValue().cfProperty());

        tableActualChildren.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableActualChildren.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedChild.add(newSelection.getName());
                selectedChild.add(newSelection.getSurname());
                selectedChild.add(newSelection.getCf());
            }
        });
        tableActualChildren.getItems().clear();


        colNameStaff.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colSurnameStaff.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        colCfStaff.setCellValueFactory(cellData -> cellData.getValue().cfProperty());

        tableActualStaff.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableActualStaff.getSelectionModel().selectedItemProperty().addListener((obsStaff, olsSelectionStaff, newSelectionStaff) -> {
            if (newSelectionStaff != null){
                selectedStaff.add(newSelectionStaff.getName());
                selectedStaff.add(newSelectionStaff.getSurname());
                selectedStaff.add(newSelectionStaff.getCf());
            }
        });
        tableActualStaff.getItems().clear();

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

    public void handleLoadChildren() {
        System.out.println("Loading data...");
        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup
            ArrayList<ChildSelectedTripDbDetails> childDbArrayList = u.loadTripSelectedChildren(selectedTrip.get(0), selectedTrip.get(1), selectedTrip.get(2), selectedTrip.get(3), selectedTrip.get(4), selectedTrip.get(5));  //call method in Server Impl

            actualChildrenObsList.clear();

            if (childDbArrayList != null){
                for(ChildSelectedTripDbDetails c : childDbArrayList){
                    ChildSelectedTripGuiDetails tmp = new ChildSelectedTripGuiDetails(c);
                    actualChildrenObsList.add(tmp);
                }
                tableActualChildren.setItems(null);
                tableActualChildren.setItems(actualChildrenObsList);
                this.renameLabel("Table loaded!");
            }else{
                this.renameLabel("Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleLoadStaff() {
        System.out.println("Loading data...");
        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup
            ArrayList<StaffSelectedTripDbDetails> staffDbArrayList = u.loadTripSelectedStaff(selectedTrip.get(0), selectedTrip.get(1), selectedTrip.get(2), selectedTrip.get(3), selectedTrip.get(4), selectedTrip.get(5));  //call method in Server Impl

            actualStaffObsList.clear();

            if (staffDbArrayList != null){
                for(StaffSelectedTripDbDetails c : staffDbArrayList){
                    StaffSelectedTripGuiDetails tmp = new StaffSelectedTripGuiDetails(c);
                    actualStaffObsList.add(tmp);
                }
                tableActualStaff.setItems(null);
                tableActualStaff.setItems(actualStaffObsList);
                this.renameLabel("Table loaded!");
                selectedTrip.clear();
            }else{
                this.renameLabel("Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleDoneSelection() {

    }

    public void handleBack() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
        try {
            new GuiNew("TripMenu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renameLabel(String st){
        lblWarning.setText(st);
    }

}
