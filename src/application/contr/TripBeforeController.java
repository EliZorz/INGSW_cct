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
 * Created by ELISA on 22/04/2018.
 */

public class TripBeforeController implements Initializable{

    private ObservableList<TripTableGuiDetails> dataObsList = FXCollections.observableArrayList();
    private ObservableList<ChildSelectedTripGuiDetails> whoObsList = FXCollections.observableArrayList();
    private ObservableList<CodRifChildGuiDetails> busObsList = FXCollections.observableArrayList();

    private ArrayList<String> selectedChild = new ArrayList<>();
    private ArrayList<String> selectedChildCfArrayList  = new ArrayList<>();
    private ArrayList<String> selectedBus = new ArrayList<>();

    private String selectedTripDepFrom = new String();
    private String selectedTripDep = new String();
    private String selectedTripCom = new String();
    private String selectedTripAccomodation = new String();
    private String selectedTripArrTo = new String();
    private String selectedTripArr = new String();
    
    @FXML
    public TableView<CodRifChildGuiDetails> tableBus;       //uso CodRifChild per plate (unica colonna)
    @FXML
    public TableColumn<CodRifChildGuiDetails, String> colPlate;
    @FXML
    public TableView<ChildSelectedTripGuiDetails> tableWho;
    @FXML
    public TableColumn<ChildSelectedTripGuiDetails, String> colName;
    @FXML
    public TableColumn<ChildSelectedTripGuiDetails, String> colSurname;
    @FXML
    public TableColumn<ChildSelectedTripGuiDetails, String> colCf;
    @FXML
    public Button btnCheck;
    @FXML
    public Button btnDeselectWho;
    @FXML
    public Button btnDeselectBus;
    @FXML
    public Button btnBack;
    @FXML
    public Label lblStatus;
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
    public Button btnSolution;
    @FXML
    public Button btnLoadWho;
    @FXML
    public Button btnLoadBus;


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

        tableWho.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableWho.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedChild.add(newSelection.getName());
                selectedChild.add(newSelection.getSurname());
                selectedChildCfArrayList.add(newSelection.getCf());
            }
        });
        tableWho.getItems().clear();


        colPlate.setCellValueFactory(cellData -> cellData.getValue().codRifProperty());

        tableBus.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableBus.getSelectionModel().selectedItemProperty().addListener((obsBus, oldSelectionBus, newSelectionBus) -> {
            if(newSelectionBus != null) {
                selectedBus.add(newSelectionBus.getCodRif());
            }
        });
        tableBus.getItems().clear();

    }


    public void handleLoadTrip() {
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

    public void handleLoadWho() {
        System.out.println("Loading data...");

        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup
            ArrayList<ChildSelectedTripDbDetails> childDbArrayList = u.loadWhoTrip(selectedTripDepFrom, selectedTripDep, selectedTripCom, selectedTripAccomodation, selectedTripArr, selectedTripArrTo);  //call method in Server Impl
            whoObsList.clear();

            if (childDbArrayList != null){
                for(ChildSelectedTripDbDetails c : childDbArrayList){
                    ChildSelectedTripGuiDetails tmp = new ChildSelectedTripGuiDetails(c);
                    whoObsList.add(tmp);
                }
                tableWho.setItems(null);
                tableWho.setItems(whoObsList);
                this.renameLabel("Table loaded!");
            }else{
                this.renameLabel("Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void handleLoadBus() {
        System.out.println("Loading data...");

        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup
            ArrayList<CodRifChildDbDetails> busDbArrayList = u.loadBusTrip(selectedTripDepFrom, selectedTripDep, selectedTripCom, selectedTripAccomodation, selectedTripArr, selectedTripArrTo);  //call method in Server Impl
            busObsList.clear();

            if (busDbArrayList != null){
                for(CodRifChildDbDetails c : busDbArrayList){
                    CodRifChildGuiDetails tmp = new CodRifChildGuiDetails(c);
                    busObsList.add(tmp);
                }
                tableBus.setItems(null);
                tableBus.setItems(busObsList);
                this.renameLabel("Table loaded!");
            }else{
                this.renameLabel("Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }



    public void handleCheck() {





    }


    public void handleOpenSolutionGui() {
        try{
            new GuiNew("TripSolutionBus");          //NOTA: IL CONTROLLER è LO STESSO ANCHE PER TripSolutionBus
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void handleDeselectWho() {
        tableWho.getSelectionModel().clearSelection();
    }

    public void handleDeselectBus() {
        tableBus.getSelectionModel().clearSelection();
    }

    public void handleBack() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
        try{
            new GuiNew("TripMenu");
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    public void renameLabel(String st){
        lblStatus.setText(st);
    }
}
