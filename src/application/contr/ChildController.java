package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.ChildDbDetails;
import application.details.ChildGuiDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChildController implements Initializable {
    private ObservableList<ChildGuiDetails> data = FXCollections.observableArrayList();

    @FXML
    public Button btnBack;
    @FXML
    public Button btnLoad;
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

    }


    @FXML
    public void handleLoadData() {

        System.out.println("Loading data...");

        try {
            UserRemote u = Singleton.getInstance().methodRmi();  //lookup

            ArrayList<ChildDbDetails> childDbArrayList = u.loadData();  //call method in Server Impl

            if (childDbArrayList != null){
                for(ChildDbDetails c : childDbArrayList){
                    ChildGuiDetails tmp = new ChildGuiDetails(c);
                    data.add(tmp);

                }

                tableChild.setItems(null);
                tableChild.setItems(data);

                this.renameLabel("Table loaded!");

            }else{
                this.renameLabel("Error.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @FXML
    public void handleBackHomepage() {
        //exit window (the previous window was MenuIniziale.fxml
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }


    public void renameLabel(String st){

        lblWarning.setText(st);
    }




}
