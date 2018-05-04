package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.IngredientsDbDetails;
import application.details.IngredientsGuiDetails;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class newMenuController implements Initializable {
    
    private ObservableList<IngredientsGuiDetails> ingredients = FXCollections.observableArrayList();
    public ArrayList<String> selectedIngredients = new ArrayList<>();
    
    private String selectedDish = null;

    private boolean controllIngredients = false;

    public static String[] selectedMenu = null;

    @FXML
    public Button backHome;

    @FXML
    public Button deselectIngr;

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
    public TableColumn<IngredientsGuiDetails,String> colIngr;

    @FXML
    public TableView<IngredientsGuiDetails> tabIng;

    @FXML
    public Button saveIngr;

    @FXML
    public Button entreeOk;

    @FXML
    public Button mainOk;

    @FXML
    public Button dessertOk;

    @FXML
    public Button sideOk;

    @FXML
    public Button drinkOk;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colIngr.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());
        tabIng.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tabIng.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
            if(newSelection != null){
                selectedIngredients.add(newSelection.getIngr());
            }
        });
        
        tabIng.getItems().clear();

        if(selectedMenu != null){
            numTF.setText(selectedMenu[0]);
            entreeTF.setText(selectedMenu[1]);
            mainTF.setText(selectedMenu[2]);
            dessertTF.setText(selectedMenu[3]);
            sideTF.setText(selectedMenu[4]);
            drinkTF.setText(selectedMenu[5]);
            dayTF.setValue(LocalDate.parse(selectedMenu[6]));

        }
    }


    
    private void loadIngredients(){
        controllIngredients = false;
        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<IngredientsDbDetails> ingArray = u.loadIngr();
            ingredients.clear();
            if( ingredients != null){
                for(IngredientsDbDetails x : ingArray){
                    IngredientsGuiDetails tmp = new IngredientsGuiDetails(x);
                    ingredients.add(tmp);
                }
                tabIng.setItems(null);
                tabIng.setItems(ingredients);
                label1.setText("Loaded");
            }else{
                label1.setText("No ingredients");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean showSelection(String selection) throws RemoteException {
        controllIngredients = true;
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
            tabIng.setItems(null);
            tabIng.setItems(ingredients);
            return true;

        }catch (RemoteException e){
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
    public void entreeIngr () throws RemoteException {
        selectedIngredients = new ArrayList<>();
        selectedDish = entreeTF.getText();
        if (showSelection(selectedDish)) label1.setText("Entree ingredients loaded");
        else{
            label1.setText("Select the ingredients for the entree");
        }

    }

    public void mainIngr() throws RemoteException{
        selectedIngredients = new ArrayList<>();
        selectedDish = mainTF.getText();
        if(showSelection(selectedDish)) label1.setText("Main course ingredients loaded");
        else {
            label1.setText("Select the ingredients for the main course");

        }
    }

    public void dessertIngr() throws RemoteException{
        selectedIngredients = new ArrayList<>();
        selectedDish = dessertTF.getText();
        if(showSelection(selectedDish)) label1.setText("Dessert ingredients loaded");
        else{
            label1.setText("Select the ingredients for the dessert");
        }
    }

    public void drinkIngr() throws RemoteException{
        selectedIngredients = new ArrayList<>();
        selectedDish = drinkTF.getText();
        if(showSelection(selectedDish)) label1.setText("Drink ingredients loaded");
        else {
            label1.setText("Select the ingredients for the drink");

        }
    }

    public void sideIngr() throws RemoteException{
        selectedIngredients = new ArrayList<>();
        selectedDish = sideTF.getText();
        if(showSelection(selectedDish)) label1.setText("Side dish ingredients loaded");
        else {

            label1.setText("Select the ingredients for the side");

        }
    }


    private void saveIngredientsForThisDish(String dishName, ArrayList<String> ingredients){
        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            if(u.saveIngredients(dishName, ingredients)){
                label1.setText("Success!!");
                selectedIngredients = new ArrayList<>();
            }else{
                label1.setText("There is a problem with this dish");
            }
            controllIngredients = true;
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }





    @FXML
    public void saveIngredients(ActionEvent event) {
        if(label1.getText().equals("Select the ingredients for the entree")){
            saveIngredientsForThisDish(entreeTF.getText(),selectedIngredients);
            deselect();
            selectedIngredients = null;
            controllIngredients = true;

        }
        else if(label1.getText().equals("Select the ingredients for the main course")){
            saveIngredientsForThisDish(mainTF.getText(),selectedIngredients);
            deselect();
            selectedIngredients = null;
            controllIngredients = true;
        }
        else if(label1.getText().equals("Select the ingredients for the dessert")){
            saveIngredientsForThisDish(dessertTF.getText(),selectedIngredients);
            deselect();
            selectedIngredients = null;
            controllIngredients = true;
        }
        else if(label1.getText().equals("Select the ingredients for the drink")){
            saveIngredientsForThisDish(drinkTF.getText(),selectedIngredients);
            deselect();
            selectedIngredients = null;
            controllIngredients = true;
        }
        else if(label1.getText().equals("Select the ingredients for the side")) {
            saveIngredientsForThisDish(sideTF.getText(),selectedIngredients);
            deselect();
            selectedIngredients = null;
            controllIngredients = true;
        }
        else {
            label1.getText().equals("This plate already exists");
            deselect();
            selectedIngredients = null;
            controllIngredients = true;
        }
    }

    public void deselect() {
        tabIng.getSelectionModel().clearSelection();
        selectedIngredients = new ArrayList<>();
    }

    //CONTROLLO SULLA DATA INSERITA
    private boolean controllData(LocalDate day){
        try {
            UserRemote u = Singleton.getInstance().methodRmi();
            return u.controllDate(day);

        }catch(RemoteException e){
            e.printStackTrace();
        }
        return false;
    }

    public void saveMenu(ActionEvent event) {
        System.out.println("Addinge the info to db");
        String num = numTF.getText();
        String entree = entreeTF.getText();
        String main = mainTF.getText();
        String side = sideTF.getText();
        String dessert = dessertTF.getText();
        String drink = drinkTF.getText();
        LocalDate day = dayTF.getValue();

        if (num.trim().isEmpty() || num.equals("0")) label1.setText("Insert a number");
        else if (entree.trim().isEmpty() && main.trim().isEmpty()) label1.setText("Insert an entree or a main course");
        else if (dessert.trim().isEmpty()) label1.setText("Insert a dessert");
        else if (drink.trim().isEmpty()) label1.setText("Insert a drink");
        else if (day == null) label1.setText("Insert a date");
        else if (!controllData(day) && selectedMenu == null) label1.setText("Change the date");
        else if(!controllIngredients && selectedMenu == null)label1.setText("Controll to have add the ingredients");
        else if(day.isBefore(LocalDate.now()) && selectedMenu == null) label1.setText("This date is already past");
        else{
            try{
                UserRemote u = Singleton.getInstance().methodRmi();
                if(selectedMenu == null) {
                    boolean addSuccess = u.addMenu(num, entree, main, dessert, side, drink, day);
                    if (addSuccess) {
                        label1.setText("Success!!");

                    }

                }
                else
                    if(u.updateMenu(num, entree, main, dessert, side, drink, day, LocalDate.parse(selectedMenu[6]))){
                    label1.setText("success!!");
                    selectedMenu = null;
                    }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            boolean result = false;
            if(entree.trim().length() != 0)result = u.addMenuIngredients(dayTF.getValue(), entree, u.searchIngredients(entree));
            if(side.trim().length() != 0)result = u.addMenuIngredients(dayTF.getValue(), side, u.searchIngredients(side));
            if(main.trim().length() != 0)result = u.addMenuIngredients(dayTF.getValue(), main, u.searchIngredients(main));
            if(drink.trim().length() != 0)result = u.addMenuIngredients(dayTF.getValue(), drink, u.searchIngredients(drink));
            if(dessert.trim().length() != 0)result = u.addMenuIngredients(dayTF.getValue(), dessert, u.searchIngredients(dessert));

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void backHome(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
