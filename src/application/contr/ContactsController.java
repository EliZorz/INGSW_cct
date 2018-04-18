package application.contr;

/**
 * Created by ELISA on 18/04/2018.
 */
import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.ContactsDbDetails;
import application.details.ContactsGuiDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ContactsController implements Initializable {
    private ObservableList<ContactsGuiDetails> dataObsList = FXCollections.observableArrayList();

    @FXML
    public TableView<ContactsGuiDetails> tableContacts;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colSurname;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colName;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colCf;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colPhone;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colMail;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colAddress;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colCap;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colProvince;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colBornOn;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colBornWhere;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colIsDoc;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colIsGuardian;
    @FXML
    public TableColumn<ContactsGuiDetails, String> colIsContact;
    @FXML
    public Button btnLoad;
    @FXML
    public Button btnAddContact;
    @FXML
    public Button btnUpdateContact;
    @FXML
    public Button btnDeleteContact;
    @FXML
    public TextField txtSurname;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtCf;
    @FXML
    public TextField txtPhone;
    @FXML
    public TextField txtMail;
    @FXML
    public TextField txtAddress;
    @FXML
    public TextField txtCap;
    @FXML
    public TextField txtProvince;
    @FXML
    public DatePicker dpBirthday;
    @FXML
    public TextField txtHometown;
    @FXML
    public CheckBox checkDoc;
    @FXML
    public CheckBox checkGuardian;
    @FXML
    public CheckBox checkContact;
    @FXML
    public Label lblStatus;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colSurname.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colCf.setCellValueFactory(cellData -> cellData.getValue().cfProperty());
        colPhone.setCellValueFactory(cellData -> cellData.getValue().telProperty());
        colMail.setCellValueFactory(cellData -> cellData.getValue().mailProperty());
        colBornOn.setCellValueFactory(cellData -> cellData.getValue().bornOnProperty());
        colBornWhere.setCellValueFactory(cellData -> cellData.getValue().bornWhereProperty());
        colAddress.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        colCap.setCellValueFactory(cellData -> cellData.getValue().capProperty());
        colProvince.setCellValueFactory(cellData -> cellData.getValue().provinceProperty());
        colIsDoc.setCellValueFactory(cellData -> cellData.getValue().isDocProperty());
        colIsGuardian.setCellValueFactory(cellData -> cellData.getValue().isGuardianProperty());
        colIsContact.setCellValueFactory(cellData -> cellData.getValue().isContactProperty());

        tableContacts.getItems().clear();

    }

    public void handleLoadContacts() {

        System.out.println("Loading data...");

        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup

            ArrayList<ContactsDbDetails> contactsDbArrayList = u.loadDataContacts();

            dataObsList.clear();

            if (contactsDbArrayList != null){
                for(ContactsDbDetails c : contactsDbArrayList){
                    ContactsGuiDetails tmp = new ContactsGuiDetails(c);
                    dataObsList.add(tmp);

                }

                tableContacts.setItems(null);
                tableContacts.setItems(dataObsList);

                this.renameLabel("Table loaded!");

            }else{
                this.renameLabel("Error.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void handleAddContact() {
        System.out.println("Adding new child to database...");

        String name = txtName.getText().toString();
        String surname = txtSurname.getText().toString();
        String cf = txtCf.getText().toString();
        String mail = txtMail.toString();
        String tel = txtPhone.toString();
        LocalDate birthday = dpBirthday.getValue();
        String bornWhere = txtHometown.getText().toString();
        String address = txtAddress.getText().toString();
        String cap = txtCap.getText().toString();
        String province = txtProvince.getText().toString();
        boolean isDoc = checkDoc.isSelected();
        boolean isGuardian = checkGuardian.isSelected();
        boolean isContact = checkContact.isSelected();

        if(isDoc){
            checkDoc.setSelected(true);
        } else {
            checkDoc.setSelected(false);
        }

        if(isGuardian){
            checkGuardian.setSelected(true);
        } else {
            checkGuardian.setSelected(false);
        }

        if(isContact){
            checkContact.setSelected(true);
        } else {
            checkContact.setSelected(false);
        }

        if (name.trim().isEmpty() || surname.trim().isEmpty() || cf.trim().isEmpty() || mail.trim().isEmpty() || tel.trim().isEmpty()
                || birthday == null || bornWhere.trim().isEmpty() || address.trim().isEmpty()
                || cap.trim().isEmpty() || province.trim().isEmpty()) {
            //this verifies there are no void fields (isSth are boolean with default value '0')
            this.renameLabel("Insert data.");

        } else {
            System.out.println("Adding data to database...");
            try {
                UserRemote u = Singleton.getInstance().methodRmi();  //lookup

                boolean isAddOk = u.addContact(surname, name, cf, mail, tel, birthday, bornWhere, address, cap, province, isDoc, isGuardian, isContact);

                if (isAddOk) {
                    lblStatus.setText("Congrats! Contact added.");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


    }

    public void handleUpdateContact() {


    }

    public void handleDeleteContact() {


    }

    public void renameLabel(String st){
        lblStatus.setText(st);
    }

}