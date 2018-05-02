package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.*;
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

public class SpecialMenuController implements Initializable {

    private ObservableList<IngredientsGuiDetails> ingredients = FXCollections.observableArrayList();

    private ObservableList<SpecialGuiDetails> specialInterni = FXCollections.observableArrayList();

    private ArrayList<String> selectedIngr = new ArrayList<>();

    private LocalDate dateSpecialMenu = null;

    private String selectedDish = null;

    private ArrayList<String> selectedInterno = new ArrayList<>();

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
    public Button saveIngr;

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
        colRif.setCellValueFactory(cellData -> cellData.getValue().refCodeProperty());
        colAller.setCellValueFactory(cellData -> cellData.getValue().allergiesProperty());
        tabRif.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tabIngr.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedIngr.add(newSelection.getIngr());
            }
        });

        tabRif.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                selectedInterno.add(newSelection.getRefCode());
            }
        });

        tabIngr.getItems().clear();
        tabRif.getItems().clear();
    }


    @FXML
    private void loadIngredients(){
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

    private boolean showSelection(String selection) throws RemoteException {
        try {
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<IngredientsDbDetails> ingredientsForThisDish = u.searchIngredients(selection);
            ingredients.clear();
            if(ingredientsForThisDish != null) {
                for (IngredientsDbDetails x : ingredientsForThisDish) {
                    IngredientsGuiDetails tmp = new IngredientsGuiDetails(x);
                    ingredients.add(tmp);
                }
            }else{
                loadIngredients(); //load all the ingredients
                return false;
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
        selectedDish = entreeTF.getText();
        if(selectedDish.trim().length() == 0) status.setText("Insert an entree");
        else{
            if (showSelection(selectedDish))
                status.setText("Entree ingredients");
            else status.setText("Select the ingredients for the entree");
        }

    }

    @FXML
    public void showSideIngredients()throws RemoteException{
        selectedDish = sideTF.getText();
        if(selectedDish.trim().length() == 0) status.setText("Insert a side dish");
        if(showSelection(selectedDish))
            status.setText("Side dish ingredients");
        else status.setText("Select the ingredients for the side dish");
    }

    @FXML
    public void showMainIngredients() throws RemoteException{
        selectedDish = mainTF.getText();
        if(selectedDish.trim().length() == 0) status.setText("Insert a main course");
        if(showSelection(selectedDish))
            status.setText("Main course ingredients");
        else status.setText("Select the ingredients for the main course");
    }

    @FXML
    public void showDessertIngredients()throws RemoteException{
        selectedDish = dessertTF.getText();
        if(selectedDish.trim().length() == 0) status.setText("Insert a dessert");
        if(showSelection(selectedDish))
            status.setText("Dessert ingredients");
        else status.setText("Select the ingredients for the dessert");
    }

    @FXML
    public void showDrinkIngredients()throws RemoteException{
        selectedDish = drinkTF.getText();
        if(selectedDish.trim().length() == 0) status.setText("Insert a drink");
        if(showSelection(selectedDish))
            status.setText("Drink");
        else status.setText("Select the ingredients for the drink");
    }

    @FXML
    public void saveSpecialMenu(){}

    @FXML
    public void saveIngredients() throws RemoteException {
        if(status.getText().equals("Select the ingredients for the side dish")){
            saveCall(selectedIngr,sideTF.getText());
        }

    }

    private void saveCall(ArrayList<String> selectedIngr, String what) throws RemoteException {
        try {
            UserRemote u = Singleton.getInstance().methodRmi();
            if (u.saveIngredients(what, selectedIngr)) status.setText("Success!! New dish added!");
            else status.setText("Error with the new dish");
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void showAllergical(){
        try {
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<SpecialDbDetails> loadInterni = u.loadInterniWithAllergies(dateSpecialMenu);
            specialInterni.clear();

            if(loadInterni != null){
                for(SpecialDbDetails x : loadInterni){
                    SpecialGuiDetails tmp = new SpecialGuiDetails(x);
                    specialInterni.add(tmp);

                }
                tabRif.setItems(null);
                tabRif.setItems(specialInterni);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}


    


