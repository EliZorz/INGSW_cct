package application.contr;

import application.Interfaces.UserRemote;
import application.LookupCall;
import application.details.BusPlateCapacityDbDetails;
import application.details.BusPlateCapacityGuiDetails;
import application.details.SupplierDbDetails;
import application.details.SupplierGuiDetails;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by ELISA on 09/05/2018.
 */
public class CoachOperatorsController implements Initializable {

    private ObservableList<SupplierGuiDetails> dataObsList = FXCollections.observableArrayList();
    private ObservableList<BusPlateCapacityGuiDetails> busObsList = FXCollections.observableArrayList();

    private ObservableList<SupplierGuiDetails> searchedSuppliers = FXCollections.observableArrayList();

    private ArrayList<String> selectedSupplier = new ArrayList<>();
    private String oldPiva;
    private String selectedPlate;

    @FXML
    public TableView<SupplierGuiDetails> tableSuppliers;
    @FXML
    public TableColumn<SupplierGuiDetails, String> colPiva;
    @FXML
    public TableColumn<SupplierGuiDetails, String> colName;
    @FXML
    public TableColumn<SupplierGuiDetails, String> colTel;
    @FXML
    public TableColumn<SupplierGuiDetails, String> colMail;
    @FXML
    public TableColumn<SupplierGuiDetails, String> colAddress;
    @FXML
    public TableColumn<SupplierGuiDetails, String> colCap;
    @FXML
    public TableColumn<SupplierGuiDetails, String> colProvince;
    @FXML
    public Label lblWarning;
    @FXML
    public Button btnLoad;
    @FXML
    public Button btnAdd;
    @FXML
    public Button btnUpdate;
    @FXML
    public Button btnDeleteBus;
    @FXML
    public Button btnBack;
    @FXML
    public Button btnDeselect;
    @FXML
    public Button btnAddBus;
    @FXML
    public Button btnDelete;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtPiva;
    @FXML
    public TextField txtTel;
    @FXML
    public TextField txtMail;
    @FXML
    public TextField txtAddress;
    @FXML
    public TextField txtCap;
    @FXML
    public TextField txtProvince;
    @FXML
    public Button btnLoadBus;
    @FXML
    public TableView<BusPlateCapacityGuiDetails> tableBus;
    @FXML
    public TableColumn<BusPlateCapacityGuiDetails, String> colPlate;
    @FXML
    public TableColumn<BusPlateCapacityGuiDetails, String> colCapacity;
    @FXML
    public TextField txtPlate;
    @FXML
    public TextField txtCapacity;

    @FXML
    public Button back;
    @FXML
    public TextField searchTF;
    @FXML
    public Button search;


    UserRemote u;

    public CoachOperatorsController(){
        if(MainControllerLogin.selected.equals("RMI"))
            u= LookupCall.getInstance().methodRmi();
        else
            u= LookupCall.getInstance().methodSocket();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLoad.setDisable(false);
        btnBack.setDisable(false);
        btnAdd.setDisable(true);
        btnDeleteBus.setDisable(true);
        btnUpdate.setDisable(true);
        btnDeselect.setDisable(true);
        btnLoadBus.setDisable(true);
        btnAddBus.setDisable(true);
        search.setDisable(true);
        back.setDisable(true);

        colName.setCellValueFactory(cellData -> cellData.getValue().nameazProperty());
        colPiva.setCellValueFactory(cellData -> cellData.getValue().pivaProperty());
        colMail.setCellValueFactory(cellData -> cellData.getValue().mailProperty());
        colTel.setCellValueFactory(cellData -> cellData.getValue().telProperty());
        colAddress.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        colCap.setCellValueFactory(cellData -> cellData.getValue().capProperty());
        colProvince.setCellValueFactory(cellData -> cellData.getValue().provinceProperty());

        tableSuppliers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableSuppliers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                selectedSupplier.add(newSelection.getNameaz());
                selectedSupplier.add(newSelection.getPiva());
                selectedSupplier.add(newSelection.getMail());
                selectedSupplier.add(newSelection.getTel());
                selectedSupplier.add(newSelection.getAddress());
                selectedSupplier.add(newSelection.getCap());
                selectedSupplier.add(newSelection.getProvince());

                oldPiva = newSelection.getPiva();

                txtName.setText(newSelection.getNameaz());
                txtPiva.setText(newSelection.getPiva());
                txtMail.setText(newSelection.getMail());
                txtTel.setText(newSelection.getTel());
                txtAddress.setText(newSelection.getAddress());
                txtCap.setText(newSelection.getCap());
                txtProvince.setText(newSelection.getProvince());

                btnAdd.setDisable(true);

            }
        });

        tableSuppliers.getItems().clear();

        colPlate.setCellValueFactory(cellData -> cellData.getValue().plateProperty());
        colCapacity.setCellValueFactory(cellData -> cellData.getValue().capacityProperty());
        tableBus.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableBus.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                selectedPlate = newSelection.getPlate();
            }
        });
        tableBus.getItems().clear();

    }

    public void handleLoadSuppliers() {
        System.out.println("Loading data...");
        try {
            ArrayList<SupplierDbDetails> staffDbArrayList = u.loadDataCoachOperator();  //call method in Server Impl
            dataObsList.clear();

            if (staffDbArrayList != null){
                for(SupplierDbDetails c : staffDbArrayList){
                    SupplierGuiDetails tmp = new SupplierGuiDetails(c);
                    dataObsList.add(tmp);
                }
                tableSuppliers.setItems(null);
                tableSuppliers.setItems(dataObsList);
                this.renameLabel("Table loaded!");
            }else{
                this.renameLabel("Error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnLoad.setDisable(false);
        btnBack.setDisable(false);
        btnAdd.setDisable(false);
        btnDeleteBus.setDisable(true);
        btnUpdate.setDisable(false);
        btnDeselect.setDisable(false);
        btnLoadBus.setDisable(false);
        btnAddBus.setDisable(true);
        search.setDisable(false);
        back.setDisable(false);
    }

    public void handleAddSupplier() throws RemoteException {
        System.out.println("Adding new staff member to database...");

        String name = txtName.getText();
        String piva = txtPiva.getText();
        String mail = txtMail.getText();
        String tel = txtTel.getText();
        String address = txtAddress.getText();
        String cap = txtCap.getText();
        String province = txtProvince.getText();

        if (name.trim().isEmpty() || piva.trim().isEmpty() || mail.trim().isEmpty() || tel.trim().isEmpty()
                || address.trim().isEmpty() || cap.trim().isEmpty() || province.trim().isEmpty()) {
            //this verifies there are no void fields
            this.renameLabel("Insert data.");
        }else if(!u.controllPiva(piva)){
            this.renameLabel("Change piva");
        } else {
            System.out.println("Adding data to database...");
            try {
                boolean isAddOk = u.addDataCoachOperator(name, piva, mail, tel, address, cap, province);  //call method in Server Impl

                if (isAddOk) {
                    this.renameLabel("Congrats! Coach operator added.");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleUpdateSupplier() throws RemoteException {
        System.out.println("Loading data...");
        String name = txtName.getText();
        String piva = txtPiva.getText();
        String mail = txtMail.getText();
        String tel = txtTel.getText();
        String address = txtAddress.getText();
        String cap = txtCap.getText();
        String province = txtProvince.getText();

        if (name.trim().isEmpty() || piva.trim().isEmpty() || mail.trim().isEmpty() || tel.trim().isEmpty()
                || address.trim().isEmpty() || cap.trim().isEmpty() || province.trim().isEmpty()) {
            //this verifies there are no void fields
            this.renameLabel("Insert data.");
        } else if(!oldPiva.equals(piva) && !u.controllPiva(piva)){
            this.renameLabel("Change fiscal code");
        }else {
            System.out.println("Adding data to database...");
            try {
                boolean isEditOk = u.updateCoachOperator(name, oldPiva, piva, mail, tel, address, cap, province);  //call method in Server Impl

                if (isEditOk) {
                    lblWarning.setText("Congrats! Coach operator edited.");
                    selectedSupplier.clear();
                }
                btnAdd.setDisable(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }


    public void handleDeleteBus() {
        /*
        1. controllo se per il noleggio associato al bus che sto eliminando esistono altri bus
        2. se ResultSet della SELECT .next() != null, elimino solo quella targa dal db bus
        3. else elimina l'interno fornitore da db noleggio
        4. in ramo if(deleted) apro gui TRIP_ACTUAL_PARTICIPANTS,
        azzero partecipanti effettivi gita e così posso reinserirli,
        cancello persone collegate alle gite connesse al bus da interni_is_here
        e richiedo selezione partecipanti e calcolo bus
        */
        System.out.println("Loading data...");
        try {
            //cancello persone collegate alle gite connesse al bus da interni_is_here
            boolean isBusConnectedToSomeone = u.deleteIsHere(selectedPlate);
            if( !isBusConnectedToSomeone ){
                boolean deleted = u.deleteCoachOperatorBus(selectedPlate);
                if(deleted){
                    System.out.println("Deleted bus.");
                    this.renameLabel("Deleted.");
                } else {
                    System.out.println("Error deleting bus.");
                    this.renameLabel("Error deleting.");
                }
            } else {
                //azzero partecipanti effettivi gita
                boolean effective = u.zeroActualParticipants(selectedPlate);
                if (effective) {
                    //elimino da gita_has_bus
                    u.deleteFromGitaHasBus(selectedPlate);
                    //cancello bus
                    boolean deleted = u.deleteCoachOperatorBus(selectedPlate);
                    if (deleted) {
                        this.renameLabel("Edit trip before!");
                        try {
                            //richiedo riselezione partecipanti e calcolo bus (come visualizzo solo gite interessanti?????????????????????????)
                            new GuiNew("TripActualParticipants");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        this.renameLabel("Error deleting who is on.");
                    }
                } else {
                    this.renameLabel("Error zero actual participants");
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void handleDelete() {
        System.out.println("Loading data...");
        try {
            boolean deleted = u.deleteCoachOperator(oldPiva);
            if(deleted){
                this.renameLabel("Deleted.");
                selectedSupplier.clear();
            } else {
                this.renameLabel("Error deleting.");
            }
            btnAdd.setDisable(false);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void handleBackHomepage() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
        try {
            new GuiNew("Information");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDeselect(){
        tableSuppliers.getSelectionModel().clearSelection();

        tableBus.getSelectionModel().clearSelection();

        txtPlate.clear();
        txtCapacity.clear();

        txtName.clear();
        txtPiva.clear();
        txtTel.clear();
        txtMail.clear();
        txtAddress.clear();
        txtProvince.clear();
        txtCap.clear();
        btnAdd.setDisable(false);
    }


    public void handleAddBusToDb() {
        String plate = txtPlate.getText();
        int capacity = Integer.parseInt(txtCapacity.getText());

        if (plate.trim().isEmpty() || txtCapacity.getText().isEmpty()) {
            this.renameLabel("Insert data.");
        } else {
            System.out.println("Adding data to database...");
            try {
                boolean isEditOk = u.addBusToDb(plate, capacity, oldPiva);  //call method in Server Impl

                if (isEditOk) {
                    lblWarning.setText("Congrats! Bus added.");
                    txtCapacity.clear();
                    txtPlate.clear();
                    selectedSupplier.clear();
                }else if(!u.controllBus(plate)){
                    this.renameLabel("Change plate");
                } else  {
                    this.renameLabel("Plate already inserted. Redo.");
                    txtCapacity.clear();
                    txtPlate.clear();
                    selectedSupplier.clear();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    public void handleLoadBus() {
        System.out.println("Loading data...");
        try {
            ArrayList<BusPlateCapacityDbDetails> staffDbArrayList = u.loadDataBus(oldPiva);  //call method in Server Impl
            busObsList.clear();

            if (staffDbArrayList != null){
                for(BusPlateCapacityDbDetails c : staffDbArrayList){
                    BusPlateCapacityGuiDetails tmp = new BusPlateCapacityGuiDetails(c);
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
        btnLoad.setDisable(false);
        btnBack.setDisable(false);
        btnAdd.setDisable(false);
        btnDeleteBus.setDisable(false);
        btnUpdate.setDisable(false);
        btnDeselect.setDisable(false);
        btnLoadBus.setDisable(false);
        btnAddBus.setDisable(false);
        search.setDisable(false);
        back.setDisable(false);

    }

    public void renameLabel(String st){
        lblWarning.setText(st);
    }

    public void reload(){
        searchedSuppliers = FXCollections.observableArrayList();
        searchTF.setText("");
        tableSuppliers.setItems(null);
        tableSuppliers.setItems(dataObsList);
    }

    public void search(){
        searchedSuppliers = FXCollections.observableArrayList();
        if(searchTF.getText().trim().length() != 0){
            if(dataObsList != null){
                for(SupplierGuiDetails x : dataObsList){
                    if(x.getProvince().contains(searchTF.getText()) || x.getNameaz().contains(searchTF.getText()) || x.getMail().contains(searchTF.getText()) || x.getCap().contains(searchTF.getText()) || x.getAddress().contains(searchTF.getText()) || x.getPiva().contains(searchTF.getText()) || x.getTel().contains(searchTF.getText()) )
                        searchedSuppliers.add(x);
                }
            }
            tableSuppliers.setItems(null);
            tableSuppliers.setItems(searchedSuppliers);
        }else{
            tableSuppliers.setItems(null);
            tableSuppliers.setItems(dataObsList);
        }
    }

}