package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
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
 * Created by ELISA on 22/04/2018.
 */
public class SupplierController implements Initializable{
    private ObservableList<SupplierGuiDetails> dataObsList = FXCollections.observableArrayList();
    ArrayList<String> selectedSupplier = new ArrayList<>();
    String oldPiva = null;

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
    public Button btnDelete;
    @FXML
    public Button btnBack;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

            }
        });

        tableSuppliers.getItems().clear();
    }

    public void handleLoadSuppliers() {
        System.out.println("Loading data...");
        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup
            ArrayList<SupplierDbDetails> staffDbArrayList = u.loadDataSuppliers();  //call method in Server Impl
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
    }

    public void handleAddSupplier() {
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
        } else {
            System.out.println("Adding data to database...");
            try {
                UserRemote u = Singleton.getInstance().methodRmi();  //lookup
                boolean isAddOk = u.addDataSupplier(name, piva, mail, tel, address, cap, province);  //call method in Server Impl

                if (isAddOk) {
                    this.renameLabel("Congrats! Supplier added.");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleUpdateSupplier() {
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
        } else {
            System.out.println("Adding data to database...");
            try {
                UserRemote u = Singleton.getInstance().methodRmi();  //lookup
                boolean isEditOk = u.updateSupplier(name, oldPiva, piva, mail, tel, address, cap, province);  //call method in Server Impl

                if (isEditOk) {
                    lblWarning.setText("Congrats! Supplier edited.");
                    selectedSupplier.clear();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    public void handleDeleteSupplier() {
        System.out.println("Loading data...");
        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup
            boolean deleted = u.deleteSupplier(selectedSupplier.get(1));
            if(deleted){
                this.renameLabel("Deleted.");
                selectedSupplier.clear();
            } else {
                this.renameLabel("Error deleting.");
            }

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

    public void renameLabel(String st){
        lblWarning.setText(st);
    }
}
