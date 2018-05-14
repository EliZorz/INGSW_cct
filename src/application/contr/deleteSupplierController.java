package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.DishesDbDetails;
import application.details.DishesDetails;
import application.details.IngredientsDbDetails;
import application.details.IngredientsGuiDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class deleteSupplierController implements Initializable{

    private ObservableList<DishesDetails> dishes = FXCollections.observableArrayList();
    private ObservableList<IngredientsGuiDetails> ingredientsNo = FXCollections.observableArrayList();
    private ObservableList<IngredientsGuiDetails> ingredients = FXCollections.observableArrayList();
    private String[] selectedMenu = new String[7];
    public static String selectedSupplier;

    @FXML
    public TextField entreeTF;
    @FXML
    public TextField mainTF;
    @FXML
    public TextField sideTF;
    @FXML
    public TextField dessertTF;
    @FXML
    public TextField drinkTF;
    @FXML
    public Button showEntree;
    @FXML
    public Button showMain;
    @FXML
    public Button showSide;
    @FXML
    public Button showDessert;
    @FXML
    public Button showDrink;
    @FXML
    public TableView<DishesDetails> tabMenu;
    @FXML
    public TableColumn<DishesDetails, String> colNum;
    @FXML
    public TableColumn<DishesDetails, String> colEntree;
    @FXML
    public TableColumn<DishesDetails, String> colMain;
    @FXML
    public TableColumn<DishesDetails, String> colSide;
    @FXML
    public TableColumn<DishesDetails, String> colDrink;
    @FXML
    public TableColumn<DishesDetails, String> colDessert;
    @FXML
    public TableColumn<DishesDetails, String> colDate;
    @FXML
    public TableView<IngredientsGuiDetails> tabNoIngr;
    @FXML
    public TableColumn<IngredientsGuiDetails, String> colIngrNO;
    @FXML
    public TableColumn<IngredientsGuiDetails, String> colIngr;
    @FXML
    public TableView<IngredientsGuiDetails> tabIngr;
    @FXML
    public Label status;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNum.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        colEntree.setCellValueFactory(cellData -> cellData.getValue().entreeProperty());
        colMain.setCellValueFactory(cellData -> cellData.getValue().mainCourseProperty());
        colSide.setCellValueFactory(cellData -> cellData.getValue().sideDishProperty());
        colDessert.setCellValueFactory(cellData -> cellData.getValue().dessertProperty());
        colDrink.setCellValueFactory(cellData -> cellData.getValue().drinkProperty());
        colDate.setCellValueFactory(cellData -> cellData.getValue().dayProperty());
        colIngrNO.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());
        colIngr.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());
        tabNoIngr.setEditable(false);
        tabNoIngr.setSelectionModel(null);
        tabMenu.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tabMenu.getSelectionModel().setCellSelectionEnabled(false);
        tabMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedMenu = new String[7];
            selectedMenu[0] = newSelection.getNumber();
            selectedMenu[1] = newSelection.getEntree();
            selectedMenu[2] = newSelection.getMainCourse();
            selectedMenu[3] = newSelection.getSideDish();
            selectedMenu[4] = newSelection.getDessert();
            selectedMenu[5] = newSelection.getDrink();
            selectedMenu[6] = newSelection.getDay();
            entreeTF.setText(selectedMenu[1]);
            mainTF.setText(selectedMenu[2]);
            sideTF.setText(selectedMenu[3]);
            dessertTF.setText(selectedMenu[4]);
            drinkTF.setText(selectedMenu[5]);
        });
        tabIngr.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tabIngr.setEditable(false);
        tabIngr.getSelectionModel().setCellSelectionEnabled(false);
        tabIngr.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{

        });
        tabMenu.getItems().clear();
        tabNoIngr.getItems().clear();
        tabIngr.getItems().clear();
        handleLoad();
        loadNoIngr();
    }

    public void handleLoad(){
        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<DishesDbDetails> menuDbArray = u.loadMenuWithThisSupplier(selectedSupplier);
            dishes.clear();
            if(menuDbArray != null) {
                for (DishesDbDetails x : menuDbArray) {
                    DishesDetails tmp = new DishesDetails(x);
                    dishes.add(tmp);
                }
                tabMenu.setItems(null);
                tabMenu.setItems(dishes);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void loadNoIngr()  {
        try {
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<IngredientsDbDetails> ingrNo = u.loadNoIngr(selectedSupplier);
            ingredientsNo.clear();
            if (ingrNo != null) {
                for (IngredientsDbDetails x : ingrNo) {
                    IngredientsGuiDetails tmp = new IngredientsGuiDetails(x);
                    ingredientsNo.add(tmp);
                }
                tabNoIngr.setItems(null);
                tabNoIngr.setItems(ingredientsNo);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showEntreeIngr(){
        tabIngr.setItems(null);
        ingredients = FXCollections.observableArrayList();
        if(entreeTF.getText().trim().length() != 0){
            if(controllIngr(entreeTF.getText())) {
                entreeTF.setText("");
                status.setText("Change this dish");
            }else
                status.setText("Loaded");

        }
    }
    public void showMainIngr(){
        tabIngr.setItems(null);
        ingredients = FXCollections.observableArrayList();
        if(mainTF.getText().trim().length() != 0)
            if(controllIngr(mainTF.getText())){
                mainTF.setText("");
                status.setText("Change this dish");
            }else
                status.setText("Loaded");
    }
    public void showSideIngr(){
        tabIngr.setItems(null);
        ingredients = FXCollections.observableArrayList();
        if(sideTF.getText().trim().length() != 0)
            if(controllIngr(sideTF.getText())){
                sideTF.setText("");
                status.setText("Change this dish");
            }else
                status.setText("Loaded");
    }
    public void showDrinkIngr(){
        tabIngr.setItems(null);
        ingredients = FXCollections.observableArrayList();
        if(drinkTF.getText().trim().length() != 0)
            if(controllIngr(drinkTF.getText())){
                drinkTF.setText("");
                status.setText("Change this dish");
            }else
                status.setText("Loaded");
    }
    public void showDessertIngr(){
        tabIngr.setItems(null);
        ingredients = FXCollections.observableArrayList();
        if(dessertTF.getText().trim().length() != 0)
            if(controllIngr(dessertTF.getText())){
                dessertTF.setText("");
                status.setText("Change this dish");
            }else
                status.setText("Loaded");
    }

    public boolean controllIngr(String dish){

        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<IngredientsDbDetails> ingredientsDbArray = u.searchIngredients(dish);
            if(ingredientsDbArray == null)
                loadIngredients();
            else {
                for (IngredientsDbDetails x : ingredientsDbArray) {
                    for (IngredientsGuiDetails y : ingredientsNo) {
                        if (x.getIngr().equals(y.getIngr()))
                            return true;
                    }

                    ingredients.add(new IngredientsGuiDetails(x));

                }
                tabIngr.setItems(null);
                tabIngr.setItems(ingredients);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;

    }

    private void loadIngredients(){  // cosi non va bene perché non fa il controllo
        try{
            UserRemote u = Singleton.getInstance().methodRmi();
            ArrayList<IngredientsDbDetails> ingArray = u.loadIngr();
            ingredients.clear();
               /* for(IngredientsDbDetails x : ingArray) {
                    for (IngredientsGuiDetails y : ingredientsNo)
                        if(y.getIngr().trim().length() != 0 )
                        if (!y.getIngr().equals(x.getIngr()))
                            x.setIngr(null);
                    ingredients.add(new IngredientsGuiDetails(x));*/
               for(IngredientsDbDetails x : ingArray)
                   ingredients.add(new IngredientsGuiDetails(x));
               for(int i = 0; i<ingredients.size(); i++)
                   for(IngredientsGuiDetails y : ingredientsNo)
                       if(ingredients.get(i).getIngr().equals(y.getIngr()))
                           ingredients.remove(i);


                tabIngr.setItems(null);
                tabIngr.setItems(ingredients);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
