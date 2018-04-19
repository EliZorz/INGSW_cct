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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class CreationMenuController implements Initializable{

    private ObservableList<IngredientsGuiDetails> ingredients = FXCollections.observableArrayList();

    ArrayList<String> selectedIngr = new ArrayList<>();

    @FXML
    public Button backHome;

    @FXML
    public TextField entreeTF;

    @FXML
    public TextField numTF;

    @FXML
    public TextField sideTF;

    @FXML
    public TextField mainTF;

    @FXML
    public TextField drinkTF;

    @FXML
    public DatePicker dayTF;

    @FXML
    public TextField dessertTF;

    @FXML
    public Button save;

    @FXML
    public Label label1;

    @FXML
    public TableColumn<IngredientsGuiDetails,String>  col1;

    @FXML
    public TableView<IngredientsGuiDetails> tabIng;

    @FXML
    public Button loadIngr;


    @FXML
    public void openIngredients() throws IOException {
        new GuiNew("AddIngr");
    }

    @FXML
    public void saveMenu(ActionEvent event) {
        System.out.println("adding the information on db");

        String num = numTF.getText().toString();
        String entree = entreeTF.getText().toString();
        String side = sideTF.getText().toString();
        String main = mainTF.getText().toString();
        String drink = drinkTF.getText().toString();
        LocalDate day = dayTF.getValue();
        String dessert = dessertTF.getText().toString();

        //mancano da fare i controlli sui dati inseriti e la verifica che ci sia almeno un ingrediente inserito
        if(num.trim().isEmpty() || num.equals("0")) label1.setText("Insert a number");
        else if(entree.trim().isEmpty() && main.trim().isEmpty()) label1.setText("Insert an entree or a main course");
            else if(dessert.trim().isEmpty()) label1.setText("Insert a dessert");
                else if(drink.trim().isEmpty()) label1.setText("Insert a drink");
                    else if(day == null) label1.setText("Insert a date");
                            else {
                                if(MainControllerLogin.selected.equals("RMI")) {

                                    try {
                                          UserRemote u = Singleton.getInstance().methodRmi();
                                          boolean addSuccess = u.addMenu(num, entree, main, dessert, side, drink, day);

                                        if(addSuccess){
                                            label1.setText("Success!! ");
                                        }
                                     } catch (RemoteException e) {
                                         e.printStackTrace();
                                        }
                                }
                                else{
                                    System.out.println("add SOCKET menu");
                                    try{
                                        UserRemote u = Singleton.getInstance().methodSocket();
                                        boolean addSuccess = u.addMenu(num,entree,main,dessert,side,drink,day);
                                        if(addSuccess)
                                            label1.setText("Success!!");
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
    }

    public void backHome(ActionEvent event){
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());
        tabIng.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tabIng.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedIngr.add(newSelection.getIngr());
            }
        });

        tabIng.getItems().clear();
    }

    @FXML
    public void loadIngredients(){
        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<IngredientsDbDetails> ingArray = u.loadIngr();
            ingredients.clear();

            if (ingredients != null){
                for(IngredientsDbDetails c : ingArray){
                    IngredientsGuiDetails tmp = new IngredientsGuiDetails(c);
                    ingredients.add(tmp);

                }

                tabIng.setItems(null);
                tabIng.setItems(ingredients);

                label1.setText("Table loaded!");

            }else{
                label1.setText("Error.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
