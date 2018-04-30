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

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;


public class CreationMenuController implements Initializable{

    private ObservableList<IngredientsGuiDetails> ingredients = FXCollections.observableArrayList();

    private ArrayList<String> selectedIngr = new ArrayList<>();

    static ArrayList<String> selectedMenu = null;

    private String selectedDish = null;

    private boolean addedIngredients = false;

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
    public TableColumn<IngredientsGuiDetails,String>  col1;

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


    @FXML
    public void deselect(){
        tabIng.getSelectionModel().clearSelection();
    }


    @FXML
    public void saveMenu(ActionEvent event) {
        System.out.println("adding the information on db");

        String num = numTF.getText();
        String entree = entreeTF.getText();
        String side = sideTF.getText();
        String main = mainTF.getText();
        String drink = drinkTF.getText();
        LocalDate day = dayTF.getValue();
        String dessert = dessertTF.getText();


    if(selectedMenu == null) {
        //mancano da fare i controlli sui dati inseriti e la verifica che ci sia almeno un ingrediente inserito
        if (num.trim().isEmpty() || num.equals("0")) label1.setText("Insert a number");
        else if (entree.trim().isEmpty() && main.trim().isEmpty()) label1.setText("Insert an entree or a main course");
        else if (dessert.trim().isEmpty()) label1.setText("Insert a dessert");
        else if (drink.trim().isEmpty()) label1.setText("Insert a drink");
        else if (day == null) label1.setText("Insert a date");
//        else if (selectedIngr.isEmpty()) label1.setText("Insert an ingredient");
        else if (!controllData(day)) label1.setText("Change the date");
        else if(day.isBefore(LocalDate.now())) label1.setText("This date is already past");
        else {
            if (MainControllerLogin.selected.equals("RMI")) {

                try {
                    UserRemote u = Singleton.getInstance().methodRmi();
                    boolean addSuccess = u.addMenu(num, entree, main, dessert, side, drink, day);

                    if (addSuccess) {
                        label1.setText("Success!! ");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("add SOCKET menu");
                try {
                    UserRemote u = Singleton.getInstance().methodSocket();
                    boolean addSuccess = u.addMenu(num, entree, main, dessert, side, drink, day);
                    if (addSuccess)
                        label1.setText("Success!!");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    } else{
        if (num.trim().isEmpty() || num.equals("0")) label1.setText("Insert a number");
        else if (entree.trim().isEmpty() && main.trim().isEmpty()) label1.setText("Insert an entree or a main course");
        else if (dessert.trim().isEmpty()) label1.setText("Insert a dessert");
        else if (drink.trim().isEmpty()) label1.setText("Insert a drink");
        else if (day == null) label1.setText("Insert a date");
        else if (!addedIngredients) label1.setText("Insert an ingredient");
        else if(controllData(day) || day.equals(LocalDate.parse(selectedMenu.get(6)))){
            try{
                UserRemote u = Singleton.getInstance().methodRmi();
                boolean updateSuccess = u.updateMenu(num,entree,main,dessert,side,drink,day,selectedIngr,LocalDate.parse(selectedMenu.get(6)));
                if(updateSuccess) label1.setText("Success!!");
            }catch( RemoteException e){
                e.printStackTrace();
            }
        }
        else label1.setText("Change the date");
    }
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
    public void backHome(ActionEvent event){
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());
        tabIng.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tabIng.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && selectedIngr!= null) {
                selectedIngr.add(newSelection.getIngr());
            }
        });

        tabIng.getItems().clear();
       if(selectedMenu != null) {
           entreeTF.setText(selectedMenu.get(1));
           numTF.setText(selectedMenu.get(0));
           drinkTF.setText(selectedMenu.get(5));
           dessertTF.setText(selectedMenu.get(3));
           sideTF.setText(selectedMenu.get(4));
           mainTF.setText(selectedMenu.get(2));
           dayTF.setValue(LocalDate.parse(selectedMenu.get(6)));


       }


    }


    private void loadIngredients(){
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



    public void entreeIngr () throws RemoteException {
            selectedDish = entreeTF.getText();
            if (showSelection(selectedDish)) label1.setText("Entree ingredients loaded");
            else{
                label1.setText("Select the ingredients for the entree");
            }

    }

    public void mainIngr() throws RemoteException{
        selectedDish = mainTF.getText();
        if(showSelection(selectedDish)) label1.setText("Main course ingredients loaded");
        else {
            label1.setText("Select the ingredients for the main course");

        }
    }

    public void dessertIngr() throws RemoteException{
        selectedDish = dessertTF.getText();
        if(showSelection(selectedDish)) label1.setText("Dessert ingredients loaded");
        else{
            label1.setText("Select the ingredients for the dessert");
        }
    }

    public void drinkIngr() throws RemoteException{
        selectedDish = drinkTF.getText();
        if(showSelection(selectedDish)) label1.setText("Drink ingredients loaded");
        else {
            label1.setText("Select the ingredients for the drink");

        }
    }

    public void sideIngr() throws RemoteException{
        selectedDish = sideTF.getText();
        if(showSelection(selectedDish)) label1.setText("Side dish ingredients loaded");
        else {

            label1.setText("Select the ingredients for the side");

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
            tabIng.setItems(null);
            tabIng.setItems(ingredients);
            return true;

        }catch (RemoteException e){
            e.printStackTrace();
            return false;
        }
    }


    @FXML
    private void saveIngredients(){
        if(label1.getText().equals("Select the ingredients for the entree")){
            saveIngredientsForThisDish(entreeTF.getText(),selectedIngr);
            deselect();
            selectedIngr = null;
            addedIngredients = true;

        }
        else if(label1.getText().equals("Select the ingredients for the main course")){
            saveIngredientsForThisDish(mainTF.getText(),selectedIngr);
            deselect();
            selectedIngr = null;
            addedIngredients = true;
        }
        else if(label1.getText().equals("Select the ingredients for the dessert")){
            saveIngredientsForThisDish(dessertTF.getText(),selectedIngr);
            deselect();
            selectedIngr = null;
            addedIngredients = true;
        }
        else if(label1.getText().equals("Select the ingredients for the drink")){
            saveIngredientsForThisDish(drinkTF.getText(),selectedIngr);
            deselect();
            selectedIngr = null;
            addedIngredients = true;
        }
        else if(label1.getText().equals("Select the ingredients for the side")) {
            saveIngredientsForThisDish(sideTF.getText(),selectedIngr);
            deselect();
            selectedIngr = null;
            addedIngredients = true;
        }
        else {
            label1.getText().equals("This plate already exists");
            deselect();
            selectedIngr = null;
            addedIngredients = true;
        }

    }

    private void saveIngredientsForThisDish(String dish, ArrayList<String> selection){
        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            if(u.saveIngredients(dish, selection))
                label1.setText("Success!!!");
            else
                label1.setText("There is a problem with this plate");
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }



}
