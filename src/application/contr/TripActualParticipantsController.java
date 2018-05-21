package application.contr;


import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.*;
import application.gui.GuiNew;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ELISA on 23/04/2018.
 */
public class TripActualParticipantsController implements Initializable {

    private ObservableList<TripTableGuiDetails> tripObsList = FXCollections.observableArrayList();
    private ObservableList<ChildSelectedTripGuiDetails> actualChildrenObsList = FXCollections.observableArrayList();
    private ObservableList<StaffSelectedTripGuiDetails> actualStaffObsList = FXCollections.observableArrayList();

    int totChildren = 0;
    int totStaff = 0;

    private HashMap<String, ArrayList<String>> busForParticipants = new HashMap<>();

    ArrayList<String> selectedTrip = new ArrayList<>();
    ArrayList<String> selectedChild = new ArrayList<>();
    ArrayList<String> selectedChildCfArrayList = new ArrayList<>();
    ArrayList<String> selectedStaffCfArrayList = new ArrayList<>();
    ArrayList<String> selectedStaff = new ArrayList<>();
    String selectedTripDepFrom = new String();
    String selectedTripDep = new String();
    String selectedTripCom = new String();
    String selectedTripAccomodation = new String();
    String selectedTripArrTo = new String();
    String selectedTripArr = new String();

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
    public Button btnBus;
    @FXML
    public Button btnDeselect;
    @FXML
    public Label lblWarning;

    UserRemote  u;

    public TripActualParticipantsController(){
        if(MainControllerLogin.selected.equals("RMI"))
            u= Singleton.getInstance().methodRmi();
        else
            u= Singleton.getInstance().methodSocket();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnBus.setDisable(true);

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

                selectedTripDepFrom = newSelection.getDepFrom();
                selectedTripDep = newSelection.getDep();
                selectedTripCom = newSelection.getCom();
                selectedTripAccomodation = newSelection.getAccomodation();
                selectedTripArr = newSelection.getArr();
                selectedTripArrTo = newSelection.getArrTo();
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
                selectedChildCfArrayList.add(newSelection.getCf());
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
                selectedStaffCfArrayList.add(newSelectionStaff.getCf());
            }
        });
        tableActualStaff.getItems().clear();

    }


    @FXML
    public void handleLoadTrip() {
        System.out.println("Loading data...");
        try {
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
        if(selectedChild == null || selectedStaff == null){
            this.renameLabel("Add at least one child AND one staff member");
        }
        else {
            System.out.println("Selection is being processed...");
            try{
                ArrayList<CodRifChildDbDetails> notAvailableStaffArrayList = u.findNotAvailableStaff(selectedStaffCfArrayList, selectedTripDep, selectedTripCom);
                ArrayList<CodRifChildDbDetails> notAvailableChildArrayList = u.findNotAvailableChild(selectedChildCfArrayList, selectedTripDep, selectedTripCom);

                //find out if some participants the user selected are already used in a concurrent trip
                if (notAvailableStaffArrayList.isEmpty() && notAvailableChildArrayList.isEmpty()){
                    int[] totParticipantsSelectedArray = u.howManyActualParticipants(selectedChildCfArrayList, selectedStaffCfArrayList);
                    totChildren = totParticipantsSelectedArray[0];
                    totStaff = totParticipantsSelectedArray[1];
                    this.renameLabelTotChildren(totChildren);
                    this.renameLabelTotStaff(totStaff);
                    System.out.println("Your selection has been saved.");

//ENABLE BUTTON CALCULATE BUS *****************************************************************************************************
                    btnBus.setDisable(false);
                    btnDoneSelection.setDisable(true);

                } else {
                    //highlight items into arrayList and tell user to reselect
                    System.out.println("Changing colours for not available children in tableview...");
                    ArrayList<String> notAvailableChildStrings = new ArrayList<>(notAvailableChildArrayList.size());
                    for (CodRifChildDbDetails object : notAvailableChildArrayList) {
                        notAvailableChildStrings.add(Objects.toString(object, null));
                    }
                    for(String s : notAvailableChildStrings)
                        System.out.println("Change colour for: " + s);

                    colCfChild.setCellFactory(column -> new TableCell<ChildSelectedTripGuiDetails, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? "" : getItem());
                            setGraphic(null);
                            TableRow<ChildSelectedTripGuiDetails> currentRow = getTableRow();
                            for (CodRifChildDbDetails child : notAvailableChildArrayList) {
                                if (!currentRow.isEmpty() && item.equals(child.getCodRif())) {
                                    currentRow.setStyle("-fx-background-color:lightcoral");
                                }
                            }
                        }
                    });


                    System.out.println("Changing colours for not available staff members in tableview...");
                    ArrayList<String> notAvailableStaffStrings = new ArrayList<>(notAvailableStaffArrayList.size());
                    for (CodRifChildDbDetails object : notAvailableStaffArrayList) {
                        notAvailableStaffStrings.add(Objects.toString(object, null));
                    }
                    for(String s : notAvailableStaffStrings)
                        System.out.println("Change colour for: " + s);

                    colCfStaff.setCellFactory(column -> new TableCell<StaffSelectedTripGuiDetails, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? "" : getItem());
                            setGraphic(null);
                            TableRow<StaffSelectedTripGuiDetails> currentRow = getTableRow();
                            for (CodRifChildDbDetails staff : notAvailableStaffArrayList) {
                                if (!currentRow.isEmpty() && item.equals(staff.getCodRif())) {
                                    currentRow.setStyle("-fx-background-color:lightcoral");
                                }
                            }
                        }
                    });

                    System.out.println("Reselect participants.");
                    this.renameLabel("Red ones are not available during trip period. Exit and redo.");
//BLOCCA TUTTO TRANNE HOMEPAGE ************************************************************************************************************
                btnBus.setDisable(true);
                btnDoneSelection.setDisable(true);
                btnLoadChildren.setDisable(true);
                btnLoadStaff.setDisable(true);
                btnLoadTrip.setDisable(true);
                btnDeselect.setDisable(true);
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    public void handleCalculateBus() {
        System.out.println("Calculating necessary buses...");
        try{
            //calculate who goes on which bus -> HashMap<plateBus, who>
            busForParticipants = u.associateBusToParticipants(selectedChildCfArrayList, totChildren, selectedStaffCfArrayList, totStaff, selectedTripDepFrom, selectedTripDep, selectedTripCom, selectedTripAccomodation, selectedTripArr, selectedTripArrTo);

            if(busForParticipants == null){
                this.renameLabel("ERROR: no buses available.");
            } else {
                this.renameLabel("Ready to go.");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    public void handleDeselectAll() {
        tableTrip.getSelectionModel().clearSelection();
        tableActualChildren.getSelectionModel().clearSelection();
        tableActualStaff.getSelectionModel().clearSelection();

        this.renameLabel("Status");
        this.renameLabelTotChildren(0);
        this.renameLabelTotStaff(0);

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

    public void renameLabelTotChildren(int st) {lblTotChildren.setText("" + st + "");}

    public void renameLabelTotStaff(int st) {lblTotStaff.setText("" + st + "");}


}
