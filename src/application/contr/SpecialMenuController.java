package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SpecialMenuController implements Initializable {

    private ObservableList<IngredientsGuiDetails> ingredients = FXCollections.observableArrayList();

    private ObservableList<SpecialGuiDetails> specialInterni = FXCollections.observableArrayList();

    public ArrayList<String> selectedIngr = new ArrayList<>();

    private LocalDate dateSpecialMenu = null;

    private String selectedDish = null;

    private ArrayList<String> selectedInterno = new ArrayList<>();

    private boolean correctDate = false;

    public static String[] selectedMenu = null;

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

    @FXML
    public TableColumn<SpecialGuiDetails, String> FC;

    @FXML
    public TableColumn<SpecialGuiDetails, String> All;

    @FXML
    public TableView<SpecialGuiDetails> tabInterni;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Ingredients.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());
        tabIngr.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        FC.setCellValueFactory(cellData-> cellData.getValue().CFProperty());
        All.setCellValueFactory(cellData -> cellData.getValue().allergieProperty());
        tabInterni.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);



        tabIngr.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedIngr.add(newSelection.getIngr());
            }
        });

        tabInterni.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection, newSelection)->{
            if(newSelection != null){
                selectedInterno.add(newSelection.getCF());
            }
        });

        tabIngr.getItems().clear();
        tabInterni.getItems().clear();

        if(selectedMenu != null){
            entreeTF.setText(selectedMenu[1]);
            dateMenu.setValue(LocalDate.parse(selectedMenu[0]));
            mainTF.setText(selectedMenu[2]);
            dessertTF.setText(selectedMenu[3]);
            sideTF.setText(selectedMenu[4]);
            drinkTF.setText(selectedMenu[5]);
            specialInterni.add(new SpecialGuiDetails(new SpecialDbDetails(selectedMenu[6],selectedMenu[7])));
            tabInterni.setItems(null);
            tabInterni.setItems(specialInterni);

        }
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
    public void searchMenuDate(){
        dateSpecialMenu = dateMenu.getValue();
        //cleaning the text fields and set false correctDate for a new search
        correctDate = false;
        entreeTF.setText("");
        mainTF.setText("");
        dessertTF.setText("");
        drinkTF.setText("");
        sideTF.setText("");

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
                    correctDate = true;
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
        if(!correctDate) {
            controllSearchedDate.setText("Insert a valid date");
        }
        else{
            selectedDish = entreeTF.getText();
            if(selectedDish.trim().length() != 0) {
                if (showSelection(selectedDish))
                    status.setText("Entree ingredients");
                else status.setText("Select the ingredients for the entree");
            }else {
                status.setText("Insert an entree");
                tabIngr.setItems(null);
            }
        }

    }

    @FXML
    public void showSideIngredients()throws RemoteException {
        if(!correctDate) controllSearchedDate.setText("Insert a valid date");
        else {
            selectedDish = sideTF.getText();
            if (selectedDish.trim().length() != 0) {
                if (showSelection(selectedDish))
                    status.setText("Side dish ingredients");
                else status.setText("Select the ingredients for the side dish");
            } else {
                status.setText("Insert a side dish");
                tabIngr.setItems(null);
            }
        }
    }

    @FXML
    public void showMainIngredients() throws RemoteException{
        if(!correctDate) controllSearchedDate.setText("Insert a valid date");
        else {
            selectedDish = mainTF.getText();
            if (selectedDish.trim().length() != 0) {
                if (showSelection(selectedDish))
                    status.setText("Main course ingredients");
                else status.setText("Select the ingredients for the main course");
            } else {
                status.setText("Insert a main course");
                tabIngr.setItems(null);
            }
        }
    }

    @FXML
    public void showDessertIngredients()throws RemoteException{
        if(!correctDate) controllSearchedDate.setText("Insert a valid date");
        else {
            selectedDish = dessertTF.getText();
            if (selectedDish.trim().length() != 0) {
                if (showSelection(selectedDish))
                    status.setText("Dessert ingredients");
                else status.setText("Select the ingredients for the dessert");
            } else {
                status.setText("Insert a dessert");
                tabIngr.setItems(null);
            }
        }
    }

    @FXML
    public void showDrinkIngredients()throws RemoteException{
        if(!correctDate) controllSearchedDate.setText("Insert a valid date");
        else {
            selectedDish = drinkTF.getText();
            if (selectedDish.trim().length() != 0) {
                if (showSelection(selectedDish))
                    status.setText("Drink");
                else status.setText("Select the ingredients for the drink");
            } else {
                status.setText("Insert a drink");
                tabIngr.setItems(null);
            }
        }
    }

    @FXML
    public void saveSpecialMenu(){}

    @FXML
    public void saveIngredients() throws RemoteException {

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

           if(specialInterni != null){
                for(SpecialDbDetails x : loadInterni){
                    SpecialGuiDetails tmp = new SpecialGuiDetails(x);
                    specialInterni.add(tmp);

                }

            }
            else controllSearchedDate.setText("No allergicals for this menu");

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void exit(ActionEvent event) {
        selectedMenu = null;
        tabIngr.setItems(null);
        tabInterni.setItems(null);
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }


    private void saveIngredientsForThisDish(String dishName, ArrayList<String> ingredients){
        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            if(u.saveIngredients(dishName, ingredients)){
                status.setText("Success!!");
                selectedIngr = new ArrayList<>();
            }else{
                status.setText("There is a problem with this dish");
            }
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void saveIngredients(ActionEvent event) {
        if(status.getText().equals("Select the ingredients for the entree")){
            saveIngredientsForThisDish(entreeTF.getText(),selectedIngr);
            deselect();
            selectedIngr = new ArrayList<>();

        }
        else if(status.getText().equals("Select the ingredients for the main course")){
            saveIngredientsForThisDish(mainTF.getText(),selectedIngr);
            deselect();
            selectedIngr= new ArrayList<>();
        }
        else if(status.getText().equals("Select the ingredients for the dessert")){
            saveIngredientsForThisDish(dessertTF.getText(),selectedIngr);
            deselect();
            selectedIngr = new ArrayList<>();
        }
        else if(status.getText().equals("Select the ingredients for the drink")){
            saveIngredientsForThisDish(drinkTF.getText(),selectedIngr);
            deselect();
            selectedIngr = new ArrayList<>();
        }
        else if(status.getText().equals("Select the ingredients for the side")) {
            saveIngredientsForThisDish(sideTF.getText(),selectedIngr);
            deselect();
            selectedIngr = new ArrayList<>();
        }
        else {
            status.getText().equals("This plate already exists");
            deselect();
            selectedIngr = new ArrayList<>();
        }
    }

    public void deselect() {
        tabIngr.getSelectionModel().clearSelection();
        selectedIngr = new ArrayList<>();
    }


}


    


