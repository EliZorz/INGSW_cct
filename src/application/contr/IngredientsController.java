package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.contr.MainControllerLogin;
import application.details.IngredientsDbDetails;
import application.details.IngredientsGuiDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class IngredientsController implements Initializable {

    ArrayList<String> selectedIngredients = new ArrayList<>();
    private ObservableList<IngredientsGuiDetails> ingr = FXCollections.observableArrayList();

    @FXML
    public Button apply;

    @FXML
    public Button loadIngr;

    @FXML
    public TableColumn<IngredientsGuiDetails,String> col1;

    @FXML
    public TableView<IngredientsGuiDetails> ingTable;


    UserRemote service;

    public IngredientsController(){
        if(MainControllerLogin.selected.equals("RMI"))
            service= Singleton.getInstance().methodRmi();
        else
            service= Singleton.getInstance().methodSocket();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      col1.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());

        ingTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ingTable.getSelectionModel().setCellSelectionEnabled(true);
        ingTable.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)-> {
            if(newSelection != null){
                selectedIngredients.add(newSelection.getIngr());
            }
        });

        ingTable.getItems().clear();

    }



    public void loadIng() {
        if(MainControllerLogin.selected.equals("RMI")){
            System.out.println("open RMI ingredients");
            try{
                ArrayList<IngredientsDbDetails> ing = service.loadIngr();
                ingr.clear();
                if(ing != null){
                    for(IngredientsDbDetails x: ing){
                        IngredientsGuiDetails tmp = new IngredientsGuiDetails(x);
                        ingr.add(tmp);
                    }
                    ingTable.setItems(null);
                    ingTable.setItems(ingr);
                }
                else{
                    System.out.println("Errore nel caricamento degli ingredienti dal db");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }
}
