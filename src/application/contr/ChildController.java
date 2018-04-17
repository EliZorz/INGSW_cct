package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.ChildDbDetails;
import application.details.ChildGuiDetails;
import application.details.IngredientsDbDetails;
import application.details.IngredientsGuiDetails;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChildController implements Initializable {
    private ObservableList<ChildGuiDetails> dataObsList = FXCollections.observableArrayList();
    private ObservableList<IngredientsGuiDetails> ingredientsObsList = FXCollections.observableArrayList();

    ArrayList<String> selectedAllergy = new ArrayList<>();

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
    public Button btnContacts;
    @FXML
    public Label lblWarning;
    @FXML
    public TableColumn<ChildGuiDetails, String> colName;
    @FXML
    public TableColumn<ChildGuiDetails, String> colSurname;
    @FXML
    public TableColumn<ChildGuiDetails, String> colCf;
    @FXML
    public TableColumn<ChildGuiDetails, String> colBornOn;
    @FXML
    public TableColumn<ChildGuiDetails, String> colBornWhere;
    @FXML
    public TableColumn<ChildGuiDetails, String> colResidence;
    @FXML
    public TableColumn<ChildGuiDetails, String> colAddress;
    @FXML
    public TableColumn<ChildGuiDetails, String> colCap;
    @FXML
    public TableColumn<ChildGuiDetails, String> colProvince;
    @FXML
    public TableView<ChildGuiDetails> tableChild;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtSurname;
    @FXML
    public TextField txtCf;
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
    public Button btnLoadIngredients;
    @FXML
    public Button btnDeselect;
    @FXML
    public TableView<IngredientsGuiDetails> tableIngr;
    @FXML
    public TableColumn<IngredientsGuiDetails, String> colIngr;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colSurname.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        colCf.setCellValueFactory(cellData -> cellData.getValue().cfProperty());
        colBornOn.setCellValueFactory(cellData -> cellData.getValue().bornOnProperty());
        colBornWhere.setCellValueFactory(cellData -> cellData.getValue().bornWhereProperty());
        colResidence.setCellValueFactory(cellData -> cellData.getValue().residenceProperty());
        colAddress.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        colCap.setCellValueFactory(cellData -> cellData.getValue().capProperty());
        colProvince.setCellValueFactory(cellData -> cellData.getValue().provinceProperty());

        tableChild.getItems().clear();


        colIngr.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());

        tableIngr.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableIngr.getSelectionModel().setCellSelectionEnabled(true);

        tableIngr.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedAllergy.add(newSelection.getIngr());
                System.out.println(newSelection.getIngr());
            }
        });

        tableIngr.getItems().clear();

    }


    @FXML
    public void handleLoadData() {

        System.out.println("Loading data...");

        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup

            ArrayList<ChildDbDetails> childDbArrayList = u.loadData();  //call method in Server Impl

            dataObsList.clear();

            if (childDbArrayList != null){
                for(ChildDbDetails c : childDbArrayList){
                    ChildGuiDetails tmp = new ChildGuiDetails(c);
                    dataObsList.add(tmp);

                }

                tableChild.setItems(null);
                tableChild.setItems(dataObsList);

                this.renameLabel("Table loaded!");

            }else{
                this.renameLabel("Error.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @FXML
    public void handleAddChild() {
        System.out.println("Adding new child to database...");

        String name = txtName.getText().toString();
        String surname = txtSurname.getText().toString();
        String cf = txtCf.getText().toString();
        LocalDate birthday = dpBirthday.getValue();
        String bornWhere = txtBornWhere.getText().toString();
        String residence = txtResidence.getText().toString();
        String address = txtAddress.getText().toString();
        String cap = txtCap.getText().toString();
        String province = txtProvince.getText().toString();

        if (name.trim().isEmpty() || surname.trim().isEmpty() || cf.trim().isEmpty() || birthday == null
                || bornWhere.trim().isEmpty() || residence.trim().isEmpty() || address.trim().isEmpty()
                || cap.trim().isEmpty() || province.trim().isEmpty()) {
            //this verifies there are no void fields
            this.renameLabel("Insert data.");


            //DA VERIFICARE : CONTACTS -> ha una handleAdd separata! (in database risulta separato..
            // NON DIMENTICARE di collegare ogni nuovo contatto al corrispondente Bambino (CodRif NECESSARIO per associare)

            //X ALLERGIES: IN MANUALE UTENTE -> "Se l'utente non seleziona nulla dal campo allergia,
            //il sistema vede tale scelta come se non ci fossero allergie da segnalare. Modificare il campo in seguito se necessario (i.g. per dimenticanza)

        } else {
            System.out.println("Adding data to database...");
            try {
                UserRemote u = Singleton.getInstance().methodRmi();  //lookup

                boolean isAddOk = u.addData(surname, name, cf, birthday, bornWhere, residence, address, cap, province, selectedAllergy);  //call method in Server Impl

                //IN SERVERIMPL: pick every field content and save into list (1 list for child -> interni + 1 list for allergy)

                if (isAddOk) {
                    lblWarning.setText("Congrats! Child added.");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleLoadIngredients(){
        System.out.println("Loading data...");

        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup

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

    }


    public void handleDeselect() {
        tableIngr.getSelectionModel().clearSelection();
    }



    @FXML
    public void handleBackHomepage() {
        //exit window (the previous window was MenuIniziale.fxml
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }


    @FXML
    public void handleContacts() {
        try {
            new GuiNew("Contacts");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleDelete(){



    }

    @FXML
    public void handleUpdate(){




    }

    public void renameLabel(String st){
        lblWarning.setText(st);
    }




}
