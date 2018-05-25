package application.contr;

import application.Interfaces.UserRemote;
import application.LookupCall;
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
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by ELISA on 22/04/2018.
 */
public class TripPlanController implements Initializable {
    private ObservableList<ChildTripGuiDetails> childObsList = FXCollections.observableArrayList();
    private ObservableList<StaffTripGuiDetails> staffObsList = FXCollections.observableArrayList();

    private ArrayList<String> selectedChild = new ArrayList<>();
    private ArrayList<String> selectedStaff = new ArrayList<>();


    @FXML
    public TableView<ChildTripGuiDetails> tableChildren;
    @FXML
    public TableColumn<ChildTripGuiDetails, String> colNameChild;
    @FXML
    public TableColumn<ChildTripGuiDetails, String> colSurnameChild;
    @FXML
    public TableColumn<ChildTripGuiDetails, String> colCfChild;
    @FXML
    public Label lblTotChildren;
    @FXML
    public Label lblTotStaff;
    @FXML
    public Label lblStatusChildren;
    @FXML
    public Label lblStatusStaff;
    @FXML
    public Label lblStatus;
    @FXML
    public TableView<StaffTripGuiDetails> tableStaff;
    @FXML
    public TableColumn<StaffTripGuiDetails, String> colNameStaff;
    @FXML
    public TableColumn<StaffTripGuiDetails, String> colSurnameStaff;
    @FXML
    public TableColumn<StaffTripGuiDetails, String> colCfStaff;
    @FXML
    public DatePicker dpDepTime;
    @FXML
    public DatePicker dpArrTime;
    @FXML
    public DatePicker dpComTime;
    @FXML
    public TextField txtStaying;
    @FXML
    public Button btnAddTrip;
    @FXML
    public Button btnBack;
    @FXML
    public TextField txtDepFrom;
    @FXML
    public TextField txtArrTo;
    @FXML
    public Button btnLoadChildren;
    @FXML
    public Button btnLoadStaff;


    UserRemote  u;

    public TripPlanController(){
        if(MainControllerLogin.selected.equals("RMI"))
            u= LookupCall.getInstance().methodRmi();
        else
            u= LookupCall.getInstance().methodSocket();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNameChild.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colSurnameChild.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        colCfChild.setCellValueFactory(cellData -> cellData.getValue().cfProperty());

        tableChildren.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableChildren.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //NOTE that in selectedChild goes just the CF, as in interni_has_gita only the CF is displayed
                selectedChild.add(newSelection.getCf());
            }
        });
        tableChildren.getItems().clear();

        colNameStaff.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colSurnameStaff.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        colCfStaff.setCellValueFactory(cellData -> cellData.getValue().cfProperty());

        tableStaff.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableStaff.getSelectionModel().selectedItemProperty().addListener((obsstaff, oldSelectionstaff, newSelectionstaff) -> {
            if (newSelectionstaff != null) {
                //as selectedChild
                selectedStaff.add(newSelectionstaff.getCf());
            }
        });
        tableStaff.getItems().clear();

    }

    @FXML
    public void handleBackHome(){
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
        try{
            new GuiNew("TripMenu");
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    @FXML
    public void handleAddTrip() {
        System.out.println("Adding new trip to database...");

        String dateDep = dpDepTime.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String dateArr = dpArrTime.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String dateCom = dpComTime.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String departureFrom = txtDepFrom.getText();
        String arrivalTo = txtArrTo.getText();
        String staying = txtStaying.getText();
        LocalDateTime today = LocalDateTime.now();

        if(departureFrom.trim().isEmpty() || arrivalTo.trim().isEmpty() ||
                dateArr.trim().isEmpty() || dateDep.trim().isEmpty() ||
                dateCom.trim().isEmpty()){
            this.renameLabelStatus("Insert data.");
        } else if (selectedChild == null || selectedStaff == null){
            this.renameLabelStatus("Add at least one child AND one staff member.");
        } else if (dpDepTime.getValue().getDayOfYear() > dpArrTime.getValue().getDayOfYear() ||
                dpArrTime.getValue().getDayOfYear() > dpComTime.getValue().getDayOfYear()){
            this.renameLabelStatus("Insert consequential time.");
        } else if (dpDepTime.getValue().getDayOfYear() <= today.getDayOfYear()) {
            this.renameLabelStatus("Insert future time.");
        } else {
            System.out.println("Adding...");
            try {
                //in serverImpl create NumGita too (as in add for children)

                int[] totParticipantsArray = u.addTrip(selectedChild, selectedStaff, dateDep, dateArr, dateCom, departureFrom, arrivalTo, staying);

                if(totParticipantsArray != null) {
                    int totChildren = totParticipantsArray[0];
                    int totStaff = totParticipantsArray[1];

                    this.renameLabelTotChildren(totChildren);
                    this.renameLabelTotStaff(totStaff);
                    this.renameLabelStatus("Trip added.");
                } else
                    System.out.println("ERROR during calculus");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void handleLoadChildren() {
        System.out.println("Loading data...");

        try {
            ArrayList<ChildTripDbDetails> childDbArrayList = u.loadChildTrip();  //call method in Server Impl
            childObsList.clear();

            if (childDbArrayList != null){
                for(ChildTripDbDetails c : childDbArrayList){
                    ChildTripGuiDetails tmp = new ChildTripGuiDetails(c);
                    childObsList.add(tmp);
                }
                tableChildren.setItems(null);
                tableChildren.setItems(childObsList);
                this.renameLabelChildren("Table loaded!");
            }else{
                this.renameLabelChildren("Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleLoadStaff() {
        System.out.println("Loading data...");

        try {
            ArrayList<StaffTripDbDetails> staffDbArrayList = u.loadStaffTrip();  //call method in Server Impl
            staffObsList.clear();

            if (staffDbArrayList != null){
                for(StaffTripDbDetails c : staffDbArrayList){
                    StaffTripGuiDetails tmp = new StaffTripGuiDetails(c);
                    staffObsList.add(tmp);
                }
                tableStaff.setItems(null);
                tableStaff.setItems(staffObsList);
                this.renameLabelStaff("Table loaded!");
            }else{
                this.renameLabelStaff("Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleDeselect() {
        tableChildren.getSelectionModel().clearSelection();
        tableStaff.getSelectionModel().clearSelection();

        dpDepTime.setValue(LocalDate.now());
        dpArrTime.setValue(LocalDate.now());
        dpComTime.setValue(LocalDate.now());
        txtArrTo.clear();
        txtDepFrom.clear();
        txtStaying.clear();

        this.renameLabelStatus("Status");
        this.renameLabelTotChildren(0);
        this.renameLabelTotStaff(0);
        this.renameLabelChildren("Deselected");
        this.renameLabelStaff("Deselected");
    }



    private void renameLabelChildren(String st){lblStatusChildren.setText(st);}

    private void renameLabelStaff(String st){lblStatusStaff.setText(st);}

    private void renameLabelTotChildren(int st) {lblTotChildren.setText("" + st + "");}

    private void renameLabelTotStaff(int st) {lblTotStaff.setText("" + st + "");}

    private void renameLabelStatus(String st){lblStatus.setText(st);}

}