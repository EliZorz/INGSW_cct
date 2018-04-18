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
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddIngrController implements Initializable {

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
                UserRemote u = Singleton.getInstance().methodRmi();
                ArrayList<IngredientsDbDetails> ing = u.loadIngr();
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
