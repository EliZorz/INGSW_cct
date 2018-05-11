package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.*;
import application.gui.GuiNew;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SpecialMenuController implements Initializable {

    private ObservableList<IngredientsGuiDetails> ingredients = FXCollections.observableArrayList();

    private ObservableList<SpecialGuiDetails> specialInterni = FXCollections.observableArrayList();

    private ObservableList<SpecialGuiDetails> searchedInterni = FXCollections.observableArrayList();

    private ObservableList<IngredientsGuiDetails> searchedIngredients = FXCollections.observableArrayList();

    public ArrayList<String> selectedIngr = new ArrayList<>();

    private LocalDate dateSpecialMenu = null;

    private String selectedDish = null;

    private ArrayList<SpecialDbDetails> selectedInterno = new ArrayList<>();

    private boolean correctDate = false;

    private String whatDish = null;

    private boolean controlIngredients = true;

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

    @FXML
    public TextField searchTF;

    @FXML
    public Button search;

    @FXML
    public Button back;

    @FXML
    public TextField searchIngr;

    @FXML
    public Button searchIng;

    @FXML
    public Button backIngr;

    @FXML
    public Button showWho;

    public void search(){
        searchedInterni = FXCollections.observableArrayList();
        if(searchTF.getText().trim().length() != 0){
            //FA LA RICERCA
            for(SpecialGuiDetails x : specialInterni){
                if(x.getCF().contains(searchTF.getText()) || x.getAllergie().contains(searchTF.getText()))
                    searchedInterni.add(x);
            }
            tabInterni.setItems(null);
            tabInterni.setItems(searchedInterni);
        }else{
            tabInterni.setItems(null);
            tabInterni.setItems(specialInterni);
        }
    }

    public void searchIngredients(){
        searchedIngredients = FXCollections.observableArrayList();
        if(searchIngr.getText().trim().length() != 0){
            for(IngredientsGuiDetails x : ingredients){
                if(x.getIngr().contains(searchIngr.getText()))
                    searchedIngredients.add(x);
            }
            tabIngr.setItems(null);
            tabIngr.setItems(searchedIngredients);
        }
        else{
            tabIngr.setItems(null);
            tabIngr.setItems(ingredients);
        }
    }





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
                selectedInterno.add(new SpecialDbDetails(newSelection.getCF(), newSelection.getAllergie()));
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
            selectedInterno = new ArrayList<>();
            selectedInterno.add(new SpecialDbDetails(selectedMenu[6], selectedMenu[7]));

        }
    }


    @FXML
    private void loadIngredients(){
        controlIngredients = false;
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

        if(selectedMenu != null) {
            status.setText("You can't change the date");
            dateMenu.setValue(LocalDate.parse(selectedMenu[0]));
            correctDate = true;
        }
        //load this menu
       else {
            selectedInterno = new ArrayList<>();
            selectedIngr = new ArrayList<>();
            selectedDish = null;
            dateSpecialMenu = dateMenu.getValue();
            //cleaning the text fields and set false correctDate for a new search
            correctDate = false;
            entreeTF.setText("");
            mainTF.setText("");
            dessertTF.setText("");
            drinkTF.setText("");
            sideTF.setText("");
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
                        status.setText("Status");
                        correctDate = true;
                        showAllergical();
                    }
                } else controllSearchedDate.setText("No menu for this date");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

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
        if(!correctDate && selectedMenu == null) {
            controllSearchedDate.setText("Insert a valid date");
        }
        else{
            selectedDish = entreeTF.getText();
            if(selectedDish.trim().length() != 0) {
                if (showSelection(selectedDish)) {
                    status.setText("Entree ingredients");
                    whatDish = new String();
                }
                else {
                    status.setText("Select the ingredients for the entree");
                    whatDish = "entree";
                }
            }else {
                status.setText("Insert an entree");
                tabIngr.setItems(null);
            }
        }

    }

    @FXML
    public void showSideIngredients()throws RemoteException {
        if(!correctDate && selectedMenu == null) controllSearchedDate.setText("Insert a valid date");
        else {
            selectedDish = sideTF.getText();
            if (selectedDish.trim().length() != 0) {
                if (showSelection(selectedDish)) {
                    status.setText("Side dish ingredients");
                    whatDish = new String();
                }
                else {
                    status.setText("Select the ingredients for the side dish");
                    whatDish = "side";
                }
            } else {
                status.setText("Insert a side dish");
                tabIngr.setItems(null);
            }
        }
    }

    @FXML
    public void showMainIngredients() throws RemoteException{
        if(!correctDate && selectedMenu == null) controllSearchedDate.setText("Insert a valid date");
        else {
            selectedDish = mainTF.getText();
            if (selectedDish.trim().length() != 0) {
                if (showSelection(selectedDish)) {
                    status.setText("Main course ingredients");
                    whatDish = new String();
                }
                else{
                    status.setText("Select the ingredients for the main course");
                    whatDish = "main";
                }
            } else {
                status.setText("Insert a main course");
                tabIngr.setItems(null);
            }
        }
    }

    @FXML
    public void showDessertIngredients()throws RemoteException{
        if(!correctDate && selectedMenu == null) controllSearchedDate.setText("Insert a valid date");
        else {
            selectedDish = dessertTF.getText();
            if (selectedDish.trim().length() != 0) {
                if (showSelection(selectedDish)) {
                    status.setText("Dessert ingredients");
                    whatDish = new String();
                }
                else {
                    status.setText("Select the ingredients for the dessert");
                    whatDish = "dessert";
                }
            } else {
                status.setText("Insert a dessert");
                tabIngr.setItems(null);
            }
        }
    }

    @FXML
    public void showDrinkIngredients()throws RemoteException{
        if(!correctDate && selectedMenu == null) controllSearchedDate.setText("Insert a valid date");
        else {
            selectedDish = drinkTF.getText();
            if (selectedDish.trim().length() != 0) {
                if (showSelection(selectedDish)) {
                    status.setText("Drink");
                    whatDish = new String();
                }
                else {
                    status.setText("Select the ingredients for the drink");
                    whatDish = "drink";
                }
            } else {
                status.setText("Insert a drink");
                tabIngr.setItems(null);
            }
        }
    }

    @FXML
    public void saveSpecialMenu(){

        String entree = entreeTF.getText();
        String main = mainTF.getText();
        String side = sideTF.getText();
        String dessert = dessertTF.getText();
        String drink = drinkTF.getText();
        LocalDate date = dateMenu.getValue();

        if(entree.trim().isEmpty() && main.trim().isEmpty()) status.setText("Insert a main course and/or an entree");
        else if(dessert.trim().isEmpty()) status.setText("Insert a dessert");
        else if(drink.trim().isEmpty()) status.setText("Insert a drink");
        else if(date == null) status.setText("Insert a valid date");
        else if(!controlIngredients && selectedMenu == null) status.setText("Make sure you have added all the ingredients");
        else if(selectedInterno.isEmpty()) status.setText("Select a person");
        else if(tabInterni.getItems() == null) status.setText("No allergical");
        else if(controllAllergicals()) status.setText("A person is allergic to this menu");
        else{
            try{
                UserRemote u = Singleton.getInstance().methodRmi();
                if(selectedMenu == null){
                    for(SpecialDbDetails x : selectedInterno) {
                        boolean addSuccess = u.addSpecialMenu(entree, main, dessert, side, drink, date, x);
                        if (addSuccess) {
                            status.setText("Success!!");
                            searchMenuDate();
                        }
                    }
                }
                else {
                    boolean updateSuccess = u.updateSpecialMenu(entree, main, dessert, side, drink, date, selectedInterno.get(0));
                    if(updateSuccess) {
                        status.setText("Success!!");
                    }
                    else status.setText("ERROR!!");
                }
            }catch(RemoteException e){
                e.printStackTrace();
            }
        }
    }

    public boolean controllAllergicals(){
        ArrayList<IngredientsDbDetails> ingredientsForThisDish = new ArrayList<>();
        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            if(entreeTF.getText().trim().length() != 0) {
                ingredientsForThisDish = u.searchIngredients(entreeTF.getText());
                for(SpecialDbDetails x : selectedInterno)
                    for(IngredientsDbDetails y : ingredientsForThisDish)
                        if(x.getAllergie().contains(y.getIngr())) return true;
            }
            if(mainTF.getText().trim().length() != 0) {
                ingredientsForThisDish = u.searchIngredients(mainTF.getText());
                for(SpecialDbDetails x : selectedInterno)
                    for(IngredientsDbDetails y : ingredientsForThisDish)
                        if(x.getAllergie().contains(y.getIngr())) return true;
            }
            if(sideTF.getText().trim().length() != 0) {
                ingredientsForThisDish = u.searchIngredients(sideTF.getText());
                for(SpecialDbDetails x : selectedInterno)
                    for(IngredientsDbDetails y : ingredientsForThisDish)
                        if(x.getAllergie().contains(y.getIngr())) return true;
            }
            if(dessertTF.getText().trim().length() != 0) {
                ingredientsForThisDish = u.searchIngredients(dessertTF.getText());
                for(SpecialDbDetails x : selectedInterno)
                    for(IngredientsDbDetails y : ingredientsForThisDish)
                        if(x.getAllergie().contains(y.getIngr())) return true;
            }
            if(drinkTF.getText().trim().length() != 0) {
                ingredientsForThisDish = u.searchIngredients(drinkTF.getText());
                for(SpecialDbDetails x : selectedInterno)
                    for(IngredientsDbDetails y : ingredientsForThisDish)
                        if(x.getAllergie().contains(y.getIngr())) return true;
            }

            return false;


        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }





    @FXML
    public void showAllergical(){
        try {
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<SpecialDbDetails> loadInterni = u.loadInterniWithAllergies(dateSpecialMenu);
            specialInterni.clear();

           if(specialInterni != null && loadInterni != null){
                for(SpecialDbDetails x : loadInterni){
                    SpecialGuiDetails tmp = new SpecialGuiDetails(x);
                    specialInterni.add(tmp);
                }
               tabInterni.setItems(null);
               tabInterni.setItems(specialInterni);

           }
           else {
               controllSearchedDate.setText("No allergicals for this menu");
               tabInterni.setItems(null);
           }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void exit(ActionEvent event) throws IOException {
        selectedMenu = null;
        tabIngr.setItems(null);
        tabInterni.setItems(null);
        ((Node)(event.getSource())).getScene().getWindow().hide();
        new GuiNew("LoadSpecialMenu");
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
            controlIngredients = true;
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void saveIngredients(ActionEvent event) {
        if(whatDish.equals("entree")){
            saveIngredientsForThisDish(entreeTF.getText(),selectedIngr);
            deselect();
            selectedIngr = new ArrayList<>();
            whatDish = new String();
            controlIngredients = true;

        }
        else if(whatDish.equals("main")){
            saveIngredientsForThisDish(mainTF.getText(),selectedIngr);
            deselect();
            selectedIngr= new ArrayList<>();
            whatDish = new String();
            controlIngredients = true;
        }
        else if(whatDish.equals("dessert")){
            saveIngredientsForThisDish(dessertTF.getText(),selectedIngr);
            deselect();
            selectedIngr = new ArrayList<>();
            whatDish = new String();
            controlIngredients = true;
        }
        else if(whatDish.equals("drink")){
            saveIngredientsForThisDish(drinkTF.getText(),selectedIngr);
            deselect();
            selectedIngr = new ArrayList<>();
            whatDish = new String();
            controlIngredients = true;
        }
        else if(whatDish.equals("side")) {
            saveIngredientsForThisDish(sideTF.getText(),selectedIngr);
            deselect();
            selectedIngr = new ArrayList<>();
            whatDish = new String();
            controlIngredients = true;
        }
        else {
            status.setText("This plate already exists");
            deselect();
            selectedIngr = new ArrayList<>();
            whatDish = new String();
            controlIngredients = true;
        }
    }

    public void deselect() {
        tabIngr.getSelectionModel().clearSelection();
        selectedIngr = new ArrayList<>();
    }

    public void deselectInterni(){
        tabInterni.getSelectionModel().clearSelection();
        selectedInterno = new ArrayList<>();
    }


    public void reLoad(ActionEvent event) {
        searchedInterni = FXCollections.observableArrayList();
        searchTF.setText("");
        tabInterni.setItems(null);
        tabInterni.setItems(specialInterni);
    }

    public void reLoadIngr(ActionEvent event){
        searchedInterni = FXCollections.observableArrayList();
        searchIngr.setText("");
        tabIngr.setItems(null);
        tabIngr.setItems(ingredients);
    }
}


    


