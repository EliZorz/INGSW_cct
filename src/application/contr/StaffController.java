package application.contr;

import application.Interfaces.UserRemote;
import application.LookupCall;
import application.gui.GuiNew;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import application.details.StaffDbDetails;
import application.details.StaffGuiDetails;
import application.details.IngredientsDbDetails;
import application.details.IngredientsGuiDetails;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by ELISA on 18/04/2018.
 */
public class StaffController implements Initializable {

    private ObservableList<StaffGuiDetails> dataObsList = FXCollections.observableArrayList();
    private ObservableList<IngredientsGuiDetails> ingredientsObsList = FXCollections.observableArrayList();
    private ObservableList<StaffGuiDetails> searchedStaff = FXCollections.observableArrayList();
    private ObservableList<IngredientsGuiDetails> searchedAllergies = FXCollections.observableArrayList();

    private ArrayList<String> selectedAllergy = new ArrayList<>();
    private ArrayList<String> selectedStaff = new ArrayList<>();
    private String oldcf = null;

    @FXML
    public Button btnBack;
    @FXML
    public Button btnLoad;
    @FXML
    public Button btnAdd;
    @FXML
    public Button btnUpdate;
    @FXML
    public Button btnDelete;
    @FXML
    public Label lblWarning;
    @FXML
    public TableColumn<StaffGuiDetails, String> colName;
    @FXML
    public TableColumn<StaffGuiDetails, String> colSurname;
    @FXML
    public TableColumn<StaffGuiDetails, String> colCf;
    @FXML
    public TableColumn<StaffGuiDetails, String> colMail;
    @FXML
    public TableColumn<StaffGuiDetails, String> colBornOn;
    @FXML
    public TableColumn<StaffGuiDetails, String> colBornWhere;
    @FXML
    public TableColumn<StaffGuiDetails, String> colResidence;
    @FXML
    public TableColumn<StaffGuiDetails, String> colAddress;
    @FXML
    public TableColumn<StaffGuiDetails, String> colCap;
    @FXML
    public TableColumn<StaffGuiDetails, String> colProvince;
    @FXML
    public TableView<StaffGuiDetails> tableStaff;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtSurname;
    @FXML
    public TextField txtCf;
    @FXML
    public TextField txtMail;
    @FXML
    public DatePicker dpBirthday;
    @FXML
    public TextField txtBornWhere;
    @FXML
    public TextField txtResidence;
    @FXML
    public TextField txtAddress;
    @FXML
    public TextField txtCap;
    @FXML
    public TextField txtProvince;
    @FXML
    public TableView<IngredientsGuiDetails> tableIngr;
    @FXML
    public Button btnLoadIngredients;
    @FXML
    public Button btnDeselect;
    @FXML
    public TableColumn<IngredientsGuiDetails, String> colIngr;

    @FXML
    public TextField searchTF;

    @FXML
    public Button search;

    @FXML
    public Button back;

    @FXML
    public Button searchSt;

    @FXML
    public Button backStaff;

    @FXML
    public TextField searchSTF;

    private UserRemote  u;

    public StaffController(){
        if(MainControllerLogin.selected.equals("RMI"))
            u= LookupCall.getInstance().methodRmi();
        else
            u= LookupCall.getInstance().methodSocket();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colSurname.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        colCf.setCellValueFactory(cellData -> cellData.getValue().cfProperty());
        colMail.setCellValueFactory(cellData -> cellData.getValue().mailProperty());
        colBornOn.setCellValueFactory(cellData -> cellData.getValue().bornOnProperty());
        colBornWhere.setCellValueFactory(cellData -> cellData.getValue().bornWhereProperty());
        colResidence.setCellValueFactory(cellData -> cellData.getValue().residenceProperty());
        colAddress.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        colCap.setCellValueFactory(cellData -> cellData.getValue().capProperty());
        colProvince.setCellValueFactory(cellData -> cellData.getValue().provinceProperty());

        tableStaff.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableStaff.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                selectedStaff.add(newSelection.getName());
                selectedStaff.add(newSelection.getSurname());
                selectedStaff.add(newSelection.getCf());
                selectedStaff.add(newSelection.getMail());
                selectedStaff.add(newSelection.getBornOn());
                selectedStaff.add(newSelection.getBornWhere());
                selectedStaff.add(newSelection.getResidence());
                selectedStaff.add(newSelection.getAddress());
                selectedStaff.add(newSelection.getCap());
                selectedStaff.add(newSelection.getProvince());

                oldcf = newSelection.getCf();

                //Per allergia serve query che cerchi allergia in DB connessa a quel CF
                txtName.setText(newSelection.getName());
                txtSurname.setText(newSelection.getSurname());
                txtCf.setText(newSelection.getCf());
                txtMail.setText(newSelection.getMail());
                dpBirthday.setValue(LocalDate.parse(newSelection.getBornOn()));
                txtBornWhere.setText(newSelection.getBornWhere());
                txtResidence.setText(newSelection.getResidence());
                txtAddress.setText(newSelection.getAddress());
                txtCap.setText(newSelection.getCap());
                txtProvince.setText(newSelection.getProvince());

                btnAdd.setDisable(true);

            }
        });

        tableStaff.getItems().clear();

        colIngr.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());

        tableIngr.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableIngr.getSelectionModel().setCellSelectionEnabled(true);

        tableIngr.getItems().clear();

    }

    @FXML
    public void handleLoadStaff() {
        System.out.println("Loading data...");
        try {
            ArrayList<StaffDbDetails> staffDbArrayList = u.loadDataStaff();  //call method in Server Impl
            dataObsList.clear();

            if (staffDbArrayList != null){
                for(StaffDbDetails c : staffDbArrayList){
                    StaffGuiDetails tmp = new StaffGuiDetails(c);
                    dataObsList.add(tmp);
                }
                tableStaff.setItems(null);
                tableStaff.setItems(dataObsList);
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
        btnDelete.setDisable(false);
        btnDeselect.setDisable(false);
        btnUpdate.setDisable(false);
        btnLoadIngredients.setDisable(false);
        search.setDisable(false);
        searchSt.setDisable(false);
        back.setDisable(false);
        backStaff.setDisable(false);
    }



    @FXML
    public void handleAddStaff() throws RemoteException {
        System.out.println("Adding new staff member to database...");

        String name = txtName.getText();
        String surname = txtSurname.getText();
        String cf = txtCf.getText();
        String mail = txtMail.getText();
        LocalDate birthday = dpBirthday.getValue();
        String bornWhere = txtBornWhere.getText();
        String residence = txtResidence.getText();
        String address = txtAddress.getText();
        String cap = txtCap.getText();
        String province = txtProvince.getText();

        if (name.trim().isEmpty() || surname.trim().isEmpty() || cf.trim().isEmpty() || mail.trim().isEmpty() || birthday == null
                || bornWhere.trim().isEmpty() || residence.trim().isEmpty() || address.trim().isEmpty()
                || cap.trim().isEmpty() || province.trim().isEmpty()) {
            //this verifies there are no void fields
            this.renameLabel("Insert data.");

            //X ALLERGIES: IN MANUALE UTENTE -> "Se l'utente non seleziona nulla dal campo allergia,
            //il sistema vede tale scelta come se non ci fossero allergie da segnalare. Modificare il campo in seguito se necessario (i.g. per dimenticanza)

        }else if(!u.controllCF(cf)){
            this.renameLabel("Change Staff fiscal code");
        }else if(cf.length() != 16 || surname.length()>25 || name.length()>15 || bornWhere.length() > 45 || residence.length() > 45 || cap.length() != 5 || province.length()>45) {
            this.renameLabel("Control length of informations");
        } else {
            System.out.println("Adding data to database...");
            try {
                boolean isAddOk = u.addDataStaff(surname, name, cf, mail, birthday, bornWhere, residence, address, cap, province, selectedAllergy);  //call method in Server Impl

                //IN SERVERIMPL: pick every field content and save into list (1 list for staff -> interni + 1 list for allergy)

                if (isAddOk) {
                    lblWarning.setText("Congrats! Staff memeber added.");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleLoadIngredients(){
        System.out.println("Loading data...");
        try {
            ArrayList<IngredientsDbDetails> ingrArrayList = u.loadIngr();  //call method in Server Impl
            ingredientsObsList.clear();

            if (ingrArrayList != null){
                for(IngredientsDbDetails c : ingrArrayList){
                    IngredientsGuiDetails tmp = new IngredientsGuiDetails(c);
                    ingredientsObsList.add(tmp);
                }
                tableIngr.setItems(null);
                tableIngr.setItems(ingredientsObsList);
            } else {
                System.out.println("Error loading Ingredients");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnLoad.setDisable(false);
        btnBack.setDisable(false);
        btnAdd.setDisable(false);
        btnDelete.setDisable(false);
        btnDeselect.setDisable(false);
        btnUpdate.setDisable(false);
        btnLoadIngredients.setDisable(false);
        search.setDisable(false);
        searchSt.setDisable(false);
        back.setDisable(false);
        backStaff.setDisable(false);
    }


    public void handleDeselect() {
        tableIngr.getSelectionModel().clearSelection();

        txtName.clear();
        txtSurname.clear();
        txtCf.clear();
        txtBornWhere.clear();
        txtResidence.clear();
        dpBirthday.setValue(LocalDate.now());
        txtAddress.clear();
        txtProvince.clear();
        txtCap.clear();

    }



    @FXML
    public void handleBackHomepage() {
        //exit window (the previous window was MenuIniziale.fxml)
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
        try {
            new GuiNew("Information");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleUpdateStaff() throws RemoteException {
        System.out.println("Loading data...");

        String name = txtName.getText();
        String surname = txtSurname.getText();
        String cf = txtCf.getText();
        LocalDate birthday = dpBirthday.getValue();
        String mail = txtMail.getText();
        String bornWhere = txtBornWhere.getText();
        String residence = txtResidence.getText();
        String address = txtAddress.getText();
        String cap = txtCap.getText();
        String province = txtProvince.getText();

        if (name.trim().isEmpty() || surname.trim().isEmpty() || cf.trim().isEmpty() || birthday == null
                || mail.trim().isEmpty() || bornWhere.trim().isEmpty() || residence.trim().isEmpty() || address.trim().isEmpty()
                || cap.trim().isEmpty() || province.trim().isEmpty()) {
            //this verifies there are no void fields
            this.renameLabel("Insert data.");
        }else if(!oldcf.equals(cf) && !u.controllCF(cf)) {
            this.renameLabel("Change fiscal code");
        }else if(cf.length() != 16 || surname.length()>25 || name.length()>15 || bornWhere.length() > 45 || residence.length() > 45 || cap.length() != 5 || province.length()>45) {
            this.renameLabel("Control length of informations");
        }else {
            System.out.println("Adding data to database...");
            try {
                boolean isEditOk = u.updateStaff(surname, name, oldcf, cf, mail, birthday, bornWhere, residence, address, cap, province, selectedAllergy);  //call method in Server Impl

                if (isEditOk) {
                    lblWarning.setText("Congrats! Staff member edited.");
                    selectedStaff.clear();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            btnAdd.setDisable(false);
        }

    }

    public void handleDeleteStaff() {
        System.out.println("Loading data...");

        try {
            boolean deleted = u.deleteStaff(selectedStaff.get(2));
            if(deleted){
                this.renameLabel("Deleted.");
                selectedStaff.clear();
            } else {
                this.renameLabel("Error deleting.");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void renameLabel(String st){
        lblWarning.setText(st);
    }


    public void reloadAll(){
        searchedAllergies = FXCollections.observableArrayList();
        searchTF.setText("");
        tableIngr.setItems(null);
        tableIngr.setItems(ingredientsObsList);
    }

    public void reloadStaff(){
        searchedStaff = FXCollections.observableArrayList();
        searchSTF.setText("");
        tableStaff.setItems(null);
        tableStaff.setItems(dataObsList);
    }

    public void searchAll(){
        searchedAllergies = FXCollections.observableArrayList();
        if(searchTF.getText().trim().length() != 0){
            if(ingredientsObsList != null)
                for(IngredientsGuiDetails x : ingredientsObsList){
                    if(x.getIngr().contains(searchTF.getText()))
                        searchedAllergies.add(x);
                }
            tableIngr.setItems(null);
            tableIngr.setItems(searchedAllergies);
        }else{
            tableIngr.setItems(null);
            tableIngr.setItems(ingredientsObsList);
        }
    }

    public void searchStaff(){
        searchedStaff = FXCollections.observableArrayList();
        if(searchSTF.getText().trim().length() != 0){
            if(dataObsList != null)
                for(StaffGuiDetails x : dataObsList){
                    if(x.getCf().contains(searchSTF.getText()) || x.getAddress().contains(searchSTF.getText()) || x.getBornOn().contains(searchSTF.getText()) || x.getBornWhere().contains(searchSTF.getText()) || x.getCap().contains(searchSTF.getText()) || x.getMail().contains(searchSTF.getText()) || x.getName().contains(searchSTF.getText()) || x.getProvince().contains(searchSTF.getText()) || x.getResidence().contains(searchSTF.getText()) || x.getSurname().contains(searchSTF.getText()))
                        searchedStaff.add(x);
                }
            tableStaff.setItems(null);
            tableStaff.setItems(searchedStaff);
        }else{
            tableStaff.setItems(null);
            tableStaff.setItems(dataObsList);
        }
    }
}