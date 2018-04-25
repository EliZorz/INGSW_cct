package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SpecialMenuController implements Initializable {

    private ObservableList<IngredientsGuiDetails> ingredients = FXCollections.observableArrayList();

    ArrayList<String> selectedIngr = new ArrayList<>();

    LocalDate dateSpecialMenu = null;

    String selectedDish = null;

    @FXML
    public Button showEntree;

    @FXML
    public Button showMain;

    @FXML
    public Button showSide;

    @FXML
    public Button showDrink;

    @FXML
    public Button showDessert;

    @FXML
    public Button loadDate;

    @FXML
    public DatePicker dateMenu;

    @FXML
    public TableView<SpecialGuiDetails> tabRif;

    @FXML
    public TableColumn<SpecialGuiDetails, String> colRif;

    @FXML
    public TableColumn<SpecialGuiDetails, String> colAller;

    @FXML
    public Button deselectElements;

    @FXML
    public Button deselect;

    @FXML
    public Button loadIngr;

    @FXML
    public TableView<IngredientsGuiDetails> tabIngr;

    @FXML
    public TableColumn<IngredientsGuiDetails, String> Ingredients;

    @FXML
    public TextField entreeTF;

    @FXML
    public TextField dessertTF;

    @FXML
    public TextField drinkTF;

    @FXML
    public TextField mainTF;

    @FXML
    public TextField sideTF;

    @FXML
    public Button save;

    @FXML
    public Label status;

    @FXML
    public Button backHome;

    @FXML
    public Label controllSearchedDate;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Ingredients.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());
        tabIngr.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tabIngr.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedIngr.add(newSelection.getIngr());
            }
        });

        tabIngr.getItems().clear();
    }


    @FXML
    public void loadIngredients(){
        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<IngredientsDbDetails> ingArray = u.loadIngr();
            ingredients.clear();
            if( ingredients != null){
                for(IngredientsDbDetails x : ingArray){
                    IngredientsGuiDetails tmp = new IngredientsGuiDetails(x);
                    ingredients.add(tmp);
                }
                tabIngr.setItems(null);
                tabIngr.setItems(ingredients);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void exit() { //DA MODIFICARE PERCHé NON ACCETTA ACTIONEVENT
        //((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void searchMenuDate(){
        dateSpecialMenu = dateMenu.getValue();

        //load this menu
        try {
            UserRemote u = Singleton.getInstance().methodRmi();
            //controll date
            if (!u.controllDate(dateSpecialMenu)) {
                controllSearchedDate.setText("Loading");

                DishesDbDetails loadedMenu = u.loadThisMenu(dateSpecialMenu);
                if (loadedMenu != null) {
                    entreeTF.setText(loadedMenu.getEntree());
                    mainTF.setText(loadedMenu.getMainCourse());
                    dessertTF.setText(loadedMenu.getDessert());
                    sideTF.setText(loadedMenu.getSideDish());
                    drinkTF.setText(loadedMenu.getDrink());
                    controllSearchedDate.setText("Loaded");
                }
            }else controllSearchedDate.setText("No menu for this date");
        } catch(RemoteException e){
                e.printStackTrace();
        }
    }

    public boolean showSelection(String selection) throws RemoteException {
        try {
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<IngredientsDbDetails> ingredientsForThisDish = u.searchIngredients(selection);
            ingredients.clear();
            for (IngredientsDbDetails x : ingredientsForThisDish) {
                IngredientsGuiDetails tmp = new IngredientsGuiDetails(x);
                ingredients.add(tmp);
            }
            tabIngr.setItems(null);
            tabIngr.setItems(ingredients);
            return true;
        }catch (RemoteException e){
            e.printStackTrace();
            return false;
        }
    }
    @FXML
    public void showEntreeIngredients() throws RemoteException {

                selectedDish = entreeTF.getText().toString();
                showSelection(selectedDish);
                status.setText("Entree ingredients");

    }

    @FXML
    public void showSideIngredients()throws RemoteException{

        selectedDish = sideTF.getText().toString();
        if(showSelection(selectedDish))
            status.setText("Side dish ingredients");
    }

    @FXML
    public void showMainIngredients() throws RemoteException{
        selectedDish = mainTF.getText().toString();
        if(showSelection(selectedDish))
            status.setText("Main course ingredients");
    }

    @FXML
    public void showDessertIngredients()throws RemoteException{
        selectedDish = dessertTF.getText().toString();
        if(showSelection(selectedDish))
            status.setText("Dessert ingredients");
    }

    @FXML
    public void showDrinkIngredients()throws RemoteException{
        selectedDish = drinkTF.getText().toString();
        if(showSelection(selectedDish))
            status.setText("Drink");
    }

    @FXML
    public void saveSpecialMenu(){}

}


    


