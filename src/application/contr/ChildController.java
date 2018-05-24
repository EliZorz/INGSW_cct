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
import java.util.*;



public class ChildController implements Initializable {

    private ObservableList<ChildGuiDetails> dataObsList = FXCollections.observableArrayList();
    private ObservableList<IngredientsGuiDetails> ingredientsObsList = FXCollections.observableArrayList();
    private ObservableList<ContactsGuiDetails> dataContactObsList = FXCollections.observableArrayList();

    private ObservableList<ContactsGuiDetails> searchedContacts = FXCollections.observableArrayList();
    private ObservableList<IngredientsGuiDetails> searchedAllergies = FXCollections.observableArrayList();
    private ObservableList<ChildGuiDetails> searchedChildren = FXCollections.observableArrayList();


    private ArrayList<String> selectedAllergy = new ArrayList<>();
    private ArrayList<String> selectedChild = new ArrayList<>();
    private ArrayList<String> selectedContact = new ArrayList<>();
    private String oldcf;
    private String oldcfContact;
    private int isDocint = 0;
    private int isContactint = 0;
    private int isGuardianint = 0;

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
    public TableView<ContactsGuiDetails> tableContacts;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colSurnameContact;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colCfContact;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colDoc;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colGuardian;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colContact;
    @FXML
    public Button btnDeleteContact;
    @FXML
    public Button btnAddContact;
    @FXML
    public Button btnUpdateContact;
    @FXML
    public Button btnLoadContact;
    @FXML
    public TextField txtSurnameContact;
    @FXML
    public TextField txtNameContact;
    @FXML
    public TextField txtCfContact;
    @FXML
    public TextField txtTelContact;
    @FXML
    public TextField txtMailContact;
    @FXML
    public TextField txtAddressContact;
    @FXML
    public TextField txtCapContact;
    @FXML
    public TextField txtProvinceContact;
    @FXML
    public DatePicker dpBirthdayContact;
    @FXML
    public TextField txtBornWhereContact;
    @FXML
    public CheckBox cbDoc;
    @FXML
    public CheckBox cbGuardian;
    @FXML
    public CheckBox cbContact;

    @FXML
    public Button btnLoadIngredients;
    @FXML
    public Button btnDeselect;
    @FXML
    public TableView<IngredientsGuiDetails> tableIngr;
    @FXML
    public TableColumn<IngredientsGuiDetails, String> colIngr;

    @FXML
    public TextField searchContact;
    @FXML
    public Button searchC;
    @FXML
    public Button backAll;
    @FXML
    public Button searchA;
    @FXML
    public TextField searchAll;
    @FXML
    public TextField searchCH;
    @FXML
    public Button backChildren;
    @FXML
    public Button searchChild;
    @FXML
    public Button backContact;

    private UserRemote u;

    public ChildController(){
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
        btnDelete.setDisable(true);
        btnDeselect.setDisable(true);
        btnUpdate.setDisable(true);
        btnLoadIngredients.setDisable(true);
        btnLoadContact.setDisable(true);
        btnAddContact.setDisable(true);
        btnUpdateContact.setDisable(true);
        btnDeleteContact.setDisable(true);
        searchC.setDisable(true);
        searchA.setDisable(true);
        searchChild.setDisable(true);
        backChildren.setDisable(true);
        backAll.setDisable(true);
        backContact.setDisable(true);

        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colSurname.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        colCf.setCellValueFactory(cellData -> cellData.getValue().cfProperty());
        colBornOn.setCellValueFactory(cellData -> cellData.getValue().bornOnProperty());
        colBornWhere.setCellValueFactory(cellData -> cellData.getValue().bornWhereProperty());
        colResidence.setCellValueFactory(cellData -> cellData.getValue().residenceProperty());
        colAddress.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        colCap.setCellValueFactory(cellData -> cellData.getValue().capProperty());
        colProvince.setCellValueFactory(cellData -> cellData.getValue().provinceProperty());

        tableChild.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableChild.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedChild.add(newSelection.getName());
                selectedChild.add(newSelection.getSurname());
                selectedChild.add(newSelection.getCf());
                selectedChild.add(newSelection.getBornOn());
                selectedChild.add(newSelection.getBornWhere());
                selectedChild.add(newSelection.getResidence());
                selectedChild.add(newSelection.getAddress());
                selectedChild.add(newSelection.getCap());
                selectedChild.add(newSelection.getProvince());

                btnAdd.setDisable(true);
                //PER EVIDENZIARE ALLERGIA QUANDO SELEZIONO RIGA SERVE QUERY CHE CERCHI NEL DB LE ALLERGIE COLLEGATE AL CF DEL SELEZIONATO - per ora lasciamo che riselezioni quelle che vuole

                oldcf = newSelection.getCf();



                txtName.setText(newSelection.getName());
                txtSurname.setText(newSelection.getSurname());
                txtCf.setText(newSelection.getCf());
                dpBirthday.setValue(LocalDate.parse(newSelection.getBornOn()));
                txtBornWhere.setText(newSelection.getBornWhere());
                txtResidence.setText(newSelection.getResidence());
                txtAddress.setText(newSelection.getAddress());
                txtCap.setText(newSelection.getCap());
                txtProvince.setText(newSelection.getProvince());

                //LOAD TABLE CONTACTS
                colSurnameContact.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
                colCfContact.setCellValueFactory(cellData -> cellData.getValue().cfProperty());
                colDoc.setCellValueFactory(cellData -> cellData.getValue().isDocProperty());
                colGuardian.setCellValueFactory(cellData -> cellData.getValue().isGuardianProperty());
                colContact.setCellValueFactory(cellData -> cellData.getValue().isContactProperty());

                tableContacts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                tableContacts.getSelectionModel().selectedItemProperty().addListener((obsContact, oldSelectionContact, newSelectionContact) -> {
                    if (newSelectionContact != null) {
                        selectedContact.add(newSelectionContact.getName());
                        selectedContact.add(newSelectionContact.getSurname());
                        selectedContact.add(newSelectionContact.getCf());
                        selectedContact.add(newSelectionContact.getMail());
                        selectedContact.add(newSelectionContact.getTel());
                        selectedContact.add(newSelectionContact.getBornOn());
                        selectedContact.add(newSelectionContact.getBornWhere());
                        selectedContact.add(newSelectionContact.getAddress());
                        selectedContact.add(newSelectionContact.getCap());
                        selectedContact.add(newSelectionContact.getProvince());
                        selectedContact.add(newSelectionContact.getIsDoc());
                        selectedContact.add(newSelectionContact.getIsGuardian());
                        selectedContact.add(newSelectionContact.getIsContact());

                        txtNameContact.setText(newSelectionContact.getName());
                        txtSurnameContact.setText(newSelectionContact.getSurname());
                        txtCfContact.setText(newSelectionContact.getCf());
                        txtMailContact.setText(newSelectionContact.getMail());
                        txtTelContact.setText(newSelectionContact.getTel());
                        dpBirthdayContact.setValue(LocalDate.parse(newSelectionContact.getBornOn()));
                        txtBornWhereContact.setText(newSelectionContact.getBornWhere());
                        txtAddressContact.setText(newSelectionContact.getAddress());
                        txtCapContact.setText(newSelectionContact.getCap());
                        txtProvinceContact.setText(newSelectionContact.getProvince());
                        if (newSelectionContact.getIsDoc().equals("1")) {
                            cbDoc.setSelected(true);
                            isDocint = 1;
                        } else {
                            cbDoc.setSelected(false);
                            isDocint = 0;
                        }
                        if (newSelectionContact.getIsGuardian().equals("1")) {
                            cbGuardian.setSelected(true);
                            isGuardianint = 1;
                        } else{
                            cbGuardian.setSelected(false);
                            isGuardianint = 0;
                        }
                        if (newSelectionContact.getIsContact().equals("1")){
                            cbContact.setSelected(true);
                            isContactint = 1;
                        }else {
                            cbContact.setSelected(false);
                            isContactint = 0;
                        }
                        oldcfContact = newSelectionContact.getCf();
                    }
                });
                tableContacts.getItems().clear();
            }
        });
        tableChild.getItems().clear();

        colIngr.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());

        tableIngr.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableIngr.getSelectionModel().setCellSelectionEnabled(true);  //could have been deleted, as there's just 1 cell for each row

        tableIngr.getSelectionModel().selectedItemProperty().addListener((obsIngr, oldSelectionIngr, newSelectionIngr) -> {
            if (newSelectionIngr != null) {
                selectedAllergy.add(newSelectionIngr.getIngr());
                System.out.println(newSelectionIngr.getIngr());
            }
        });

        tableIngr.getItems().clear();

    }


    @FXML
    public void handleLoadData() {
        System.out.println("Loading data from ChildController...");
        try {
            ArrayList<ChildDbDetails> childDbArrayList = u.loadData();  //call method in Server Impl
            dataObsList.clear();
            ingredientsObsList.clear();
            tableIngr.setItems(null);

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
        btnLoad.setDisable(false);
        btnBack.setDisable(false);
        btnAdd.setDisable(false);
        btnDelete.setDisable(false);
        btnUpdate.setDisable(false);
        btnLoadIngredients.setDisable(false);
        btnDeselect.setDisable(true);
        btnLoadContact.setDisable(false);
        btnAddContact.setDisable(true);
        btnUpdateContact.setDisable(true);
        btnDeleteContact.setDisable(true);
        searchC.setDisable(false);
        searchA.setDisable(false);
        searchChild.setDisable(false);
        backChildren.setDisable(false);
        backAll.setDisable(false);
        backContact.setDisable(false);
    }




    @FXML
    public void handleAddChild() throws RemoteException {
        System.out.println("Adding new child to database...");

        String name = txtName.getText();
        String surname = txtSurname.getText();
        String cf = txtCf.getText();
        LocalDate birthday = dpBirthday.getValue();
        String bornWhere = txtBornWhere.getText();
        String residence = txtResidence.getText();
        String address = txtAddress.getText();
        String cap = txtCap.getText();
        String province = txtProvince.getText();

        String nameContact = txtNameContact.getText();
        String surnameContact = txtSurnameContact.getText();
        String cfContact = txtCfContact.getText();
        String mailContact = txtMailContact.getText();
        String telContact = txtTelContact.getText();
        LocalDate birthdayContact = dpBirthdayContact.getValue();
        String bornWhereContact = txtBornWhereContact.getText();
        String addressContact = txtAddressContact.getText();
        String capContact = txtCapContact.getText();
        String provinceContact = txtProvince.getText();
        boolean isDoc = cbDoc.isSelected();
        if(cbDoc.isSelected()){ cbDoc.setSelected(true); }
        else { cbDoc.setSelected(false); }
        boolean isGuardian = cbGuardian.isSelected();
        if(cbGuardian.isSelected()){ cbGuardian.setSelected(true); }
        else { cbGuardian.setSelected(false); }
        boolean isContact = cbContact.isSelected();
        if(cbContact.isSelected()){ cbContact.setSelected(true); }
        else { cbContact.setSelected(false); }

        if (name.trim().isEmpty() || surname.trim().isEmpty() || cf.trim().isEmpty() || birthday == null
                || bornWhere.trim().isEmpty() || residence.trim().isEmpty() || address.trim().isEmpty()
                || cap.trim().isEmpty() || province.trim().isEmpty()
                || nameContact.trim().isEmpty() || surnameContact.trim().isEmpty() || cfContact.trim().isEmpty() || mailContact.trim().isEmpty() || telContact.trim().isEmpty()
                || birthdayContact == null || bornWhereContact.trim().isEmpty() || addressContact.trim().isEmpty()
                || capContact.trim().isEmpty() || provinceContact.trim().isEmpty() || ! isDoc) {

            this.renameLabel("Insert data.");



        }else if( !u.controllCF(cf)){
            this.renameLabel("Change child fiscal code");
        }
        else {
            System.out.println("Adding data to database...");
            try {
                boolean isAddOk = u.addData(surname, name, cf, birthday, bornWhere, residence, address, cap, province, selectedAllergy,
                        nameContact, surnameContact, cfContact, mailContact, telContact, birthdayContact, bornWhereContact, addressContact, capContact, provinceContact, isDoc, isGuardian, isContact);  //call method in Server Impl

                //IN SERVERIMPL: pick every field content and save into list (1 list for child -> interni + 1 list for allergy)

                if (isAddOk) {
                    lblWarning.setText("Congrats! Child added.");
                    txtName.clear();
                    txtSurname.clear();
                    txtCf.clear();
                    dpBirthday.setValue(LocalDate.now());
                    txtBornWhere.clear();
                    txtResidence.clear();
                    txtAddress.clear();
                    txtCap.clear();
                    txtProvince.clear();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleDelete() {
        try {
            boolean deleted = u.deleteChild(oldcf);   //PER EVITARE CHE USER MODIFICHI E POI CANCELLI, PASSO IL CF "ORIGINALE"
            if(deleted){
                this.renameLabel("Deleted.");
                selectedChild.clear();
                txtName.clear();
                txtSurname.clear();
                txtCf.clear();
                dpBirthday.setValue(LocalDate.now());
                txtBornWhere.clear();
                txtResidence.clear();
                txtAddress.clear();
                txtCap.clear();
                txtProvince.clear();
            } else {
                this.renameLabel("Error deleting.");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void handleUpdate() throws RemoteException {
        System.out.println("Handling update...");

        String name = txtName.getText();
        String surname = txtSurname.getText();
        String cf = txtCf.getText();
        LocalDate birthday = dpBirthday.getValue();
        String bornWhere = txtBornWhere.getText();
        String residence = txtResidence.getText();
        String address = txtAddress.getText();
        String cap = txtCap.getText();
        String province = txtProvince.getText();

        if (name.trim().isEmpty() || surname.trim().isEmpty() || cf.trim().isEmpty() || birthday == null
                || bornWhere.trim().isEmpty() || residence.trim().isEmpty() || address.trim().isEmpty()
                || cap.trim().isEmpty() || province.trim().isEmpty()) {

            this.renameLabel("Insert data.");
        } else if(!oldcf.equals(cf) && !u.controllCF(cf)){
            this.renameLabel("Change fiscal code");
        }else {
            System.out.println("Adding data to database...");
            try {
                boolean isEditOk = u.updateChild(surname, name, oldcf, cf, birthday, bornWhere, residence, address, cap, province, selectedAllergy);  //call method in Server Impl

                if (isEditOk) {
                    lblWarning.setText("Congrats! Child edited.");
                    selectedChild.clear();
                    txtName.clear();
                    txtSurname.clear();
                    txtCf.clear();
                    dpBirthday.setValue(LocalDate.now());
                    txtBornWhere.clear();
                    txtResidence.clear();
                    txtAddress.clear();
                    txtCap.clear();
                    txtProvince.clear();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }






    public void handleLoadContacts() {
        System.out.println("Loading data contacts...");
        txtNameContact.clear();
        txtSurnameContact.clear();
        txtCfContact.clear();
        txtMailContact.clear();
        txtTelContact.clear();
        dpBirthdayContact.setValue(LocalDate.now());
        txtBornWhereContact.clear();
        txtAddressContact.clear();
        txtCapContact.clear();
        txtProvinceContact.clear();
        try {
            ArrayList<ContactsDbDetails> contactsDbArrayList = u.loadDataContacts(oldcf);
            dataContactObsList.clear();

            if (contactsDbArrayList != null){
                for(ContactsDbDetails c : contactsDbArrayList){
                    ContactsGuiDetails tmpc = new ContactsGuiDetails(c);
                    dataContactObsList.add(tmpc);
                }
                tableContacts.setItems(null);
                tableContacts.setItems(dataContactObsList);
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
        btnUpdate.setDisable(false);
        btnLoadIngredients.setDisable(false);
        btnDeselect.setDisable(true);
        btnLoadContact.setDisable(false);
        btnAddContact.setDisable(false);
        btnUpdateContact.setDisable(false);
        btnDeleteContact.setDisable(false);
        searchC.setDisable(false);
        searchA.setDisable(false);
        searchChild.setDisable(false);
        backChildren.setDisable(false);
        backAll.setDisable(false);
        backContact.setDisable(false);

    }

    public void handleAddContact() throws RemoteException {
        System.out.println("Adding new contact to database...");

        String name = txtNameContact.getText();
        String surname = txtSurnameContact.getText();
        String cf = txtCfContact.getText();
        String mail = txtMailContact.getText();
        String tel = txtTelContact.getText();
        LocalDate birthday = dpBirthdayContact.getValue();
        String bornWhere = txtBornWhereContact.getText();
        String address = txtAddressContact.getText();
        String cap = txtCapContact.getText();
        String province = txtProvince.getText();
        boolean isDoc = cbDoc.isSelected();
        if(cbDoc.isSelected()){ cbDoc.setSelected(true); }
        else { cbDoc.setSelected(false); }
        boolean isGuardian = cbGuardian.isSelected();
        if(cbGuardian.isSelected()){ cbGuardian.setSelected(true); }
        else { cbGuardian.setSelected(false); }
        boolean isContact = cbContact.isSelected();
        if(cbContact.isSelected()){ cbContact.setSelected(true); }
        else { cbContact.setSelected(false); }

        if (name.trim().isEmpty() || surname.trim().isEmpty() || cf.trim().isEmpty() || mail.trim().isEmpty() || tel.trim().isEmpty()
                || birthday == null || bornWhere.trim().isEmpty() || address.trim().isEmpty()
                || cap.trim().isEmpty() || province.trim().isEmpty()) {
            //this verifies there are no void fields (isSth are boolean with default value '0')
            this.renameLabel("Insert data.");

        }else {

            System.out.println("Adding data to database...");
            try {
                boolean isAddOk = u.addContact(selectedChild, surname, name, cf, mail, tel, birthday, bornWhere, address, cap, province, isDoc, isGuardian, isContact);

                if (isAddOk) {
                    this.renameLabel("Congrats! Contact added.");
                    txtNameContact.clear();
                    txtSurnameContact.clear();
                    txtCfContact.clear();
                    txtMailContact.clear();
                    txtTelContact.clear();
                    dpBirthdayContact.setValue(LocalDate.now());
                    txtBornWhereContact.clear();
                    txtAddressContact.clear();
                    txtCapContact.clear();
                    txtProvinceContact.clear();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


    }

    public void handleUpdateContact() throws RemoteException {
        System.out.println("Loading data...");

        String name = txtNameContact.getText();
        String surname = txtSurnameContact.getText();
        String cf = txtCfContact.getText();
        String mail = txtMailContact.getText();
        String tel = txtTelContact.getText();
        LocalDate birthday = dpBirthdayContact.getValue();
        String bornWhere = txtBornWhereContact.getText();
        String address = txtAddressContact.getText();
        String cap = txtCapContact.getText();
        String province = txtProvince.getText();
        boolean isDoc = cbDoc.isSelected();
        if(cbDoc.isSelected()){ cbDoc.setSelected(true); }
        else { cbDoc.setSelected(false); }
        boolean isGuardian = cbGuardian.isSelected();
        if(cbGuardian.isSelected()){ cbGuardian.setSelected(true); }
        else { cbGuardian.setSelected(false); }
        boolean isContact = cbContact.isSelected();
        if(cbContact.isSelected()){ cbContact.setSelected(true); }
        else { cbContact.setSelected(false); }

        if (name.trim().isEmpty() || surname.trim().isEmpty() || cf.trim().isEmpty() || mail.trim().isEmpty() || tel.trim().isEmpty()
                || birthday == null || bornWhere.trim().isEmpty() || address.trim().isEmpty()
                || cap.trim().isEmpty() || province.trim().isEmpty()) {
            //this verifies there are no void fields (isSth are boolean with default value '0')
            this.renameLabel("Insert data.");

        } else {
            System.out.println("Adding data to database...");
            try {
                boolean isEditOk = u.updateContact(surname, name, oldcfContact, cf, mail, tel, birthday, bornWhere, address, cap, province, isDocint, isGuardianint, isContactint);  //call method in Server Impl

                if (isEditOk) {
                    lblWarning.setText("Congrats! Contact edited.");
                    selectedContact.clear();

                    txtNameContact.clear();
                    txtSurnameContact.clear();
                    txtCfContact.clear();
                    txtMailContact.clear();
                    txtTelContact.clear();
                    dpBirthdayContact.setValue(LocalDate.now());
                    txtBornWhereContact.clear();
                    txtAddressContact.clear();
                    txtCapContact.clear();
                    txtProvinceContact.clear();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    public void handleDeleteContact() {
        try {
            boolean deleted = u.deleteContact(oldcfContact);   //PER EVITARE CHE USER MODIFICHI E POI CANCELLI, PASSO IL CF "ORIGINALE"
            if(deleted){
                this.renameLabel("Deleted.");
                selectedContact.clear();

                txtNameContact.clear();
                txtSurnameContact.clear();
                txtCfContact.clear();
                txtMailContact.clear();
                txtTelContact.clear();
                dpBirthdayContact.setValue(LocalDate.now());
                txtBornWhereContact.clear();
                txtAddressContact.clear();
                txtCapContact.clear();
                txtProvinceContact.clear();
            } else {
                this.renameLabel("Error deleting.");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
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
        btnUpdate.setDisable(false);
        btnLoadIngredients.setDisable(false);
        btnDeselect.setDisable(false);
        btnLoadContact.setDisable(false);
        btnAddContact.setDisable(true);
        btnUpdateContact.setDisable(true);
        btnDeleteContact.setDisable(true);
        searchC.setDisable(false);
        searchA.setDisable(false);
        searchChild.setDisable(false);
        backChildren.setDisable(false);
        backAll.setDisable(false);
        backContact.setDisable(false);

    }


    public void handleDeselect() {
        tableIngr.getSelectionModel().clearSelection();

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


    private void renameLabel(String st){
        lblWarning.setText(st);
    }


    public void reloadContact(){
        searchedContacts = FXCollections.observableArrayList();
        searchContact.setText("");
        tableContacts.setItems(null);
        tableContacts.setItems(dataContactObsList);
    }

    public void searchContact(){
        searchedContacts = FXCollections.observableArrayList();
        if(searchContact.getText().trim().length() != 0){
            if(dataContactObsList != null)
                for(ContactsGuiDetails x : dataContactObsList){
                    if(x.getCf().contains(searchContact.getText()) || x.getSurname().contains(searchContact.getText()))
                        searchedContacts.add(x);
                }
            tableContacts.setItems(null);
            tableContacts.setItems(searchedContacts);
        }
        else{
            tableContacts.setItems(null);
            tableContacts.setItems(searchedContacts);
        }
    }

    public void reloadAllergies(){
        searchedAllergies = FXCollections.observableArrayList();
        searchAll.setText("");
        tableIngr.setItems(null);
        tableIngr.setItems(ingredientsObsList);
    }

    public void searchAllergies(){
        searchedAllergies = FXCollections.observableArrayList();
        if(searchAll.getText().trim().length() != 0){
            if(ingredientsObsList != null)
                for(IngredientsGuiDetails x : ingredientsObsList){
                    if(x.getIngr().contains(searchAll.getText()))
                        searchedAllergies.add(x);
                }
            tableIngr.setItems(null);
            tableIngr.setItems(searchedAllergies);
        }else{
            tableIngr.setItems(null);
            tableIngr.setItems(ingredientsObsList);
        }
    }

    public void searchChildren(){
        searchedChildren = FXCollections.observableArrayList();
        if(searchCH.getText().trim().length() != 0){
            if(dataObsList != null)
                for(ChildGuiDetails x : dataObsList){
                    if(x.getAddress().contains(searchCH.getText()) || x.getBornOn().contains(searchCH.getText()) || x.getBornWhere().contains(searchCH.getText()) || x.getCap().contains(searchCH.getText()) || x.getCf().contains(searchCH.getText()) || x.getName().contains(searchCH.getText()) || x.getProvince().contains(searchCH.getText()) || x.getResidence().contains(searchCH.getText() )|| x.getSurname().contains(searchCH.getText()))
                        searchedChildren.add(x);
                }
            tableChild.setItems(null);
            tableChild.setItems(searchedChildren);
        }else{
            tableChild.setItems(null);
            tableChild.setItems(dataObsList);
        }
    }

    public void reloadChildren(){
        searchedChildren = FXCollections.observableArrayList();
        searchCH.setText("");
        tableChild.setItems(null);
        tableChild.setItems(dataObsList);
    }

}