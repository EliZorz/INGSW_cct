package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import application.details.DishesDbDetails;
import application.details.DishesDetails;
import application.details.IngredientsDbDetails;
import application.details.IngredientsGuiDetails;
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

public class DeleteSupplierController implements Initializable {

    private ObservableList<DishesDetails> dishes = FXCollections.observableArrayList();
    private ObservableList<IngredientsGuiDetails> ingredientsNo = FXCollections.observableArrayList();
    private ObservableList<IngredientsGuiDetails> ingredients = FXCollections.observableArrayList();
    private String[] selectedMenu = new String[7];
    private ObservableList<IngredientsGuiDetails> searchedIngredients = FXCollections.observableArrayList();
    private ArrayList<String> selectedIngredients = new ArrayList<>();
    public static String selectedSupplier;
    private boolean controllAddIngredients = false;

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
    @FXML
    public TextField searchTF;
    @FXML
    public Button searchButton;
    @FXML
    public Button returnButton;
    @FXML
    public Button saveIngr;
    @FXML
    public Button deselectButton;
    @FXML
    public Label dishesStatus;
    @FXML
    public Button back;
    @FXML
    public Button deleteButton;

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
        tabIngr.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedIngredients.add(newSelection.getIngr());
            }
        });
        tabMenu.getItems().clear();
        tabNoIngr.getItems().clear();
        tabIngr.getItems().clear();
        handleLoad();
        loadNoIngr();
        if(dishes.isEmpty())
            back.setDisable(false);
    }

    public void handleLoad() {
        UserRemote u;
        if(MainControllerLogin.selected.equals("RMI"))
            u = Singleton.getInstance().methodRmi();
        else
            u = Singleton.getInstance().methodSocket();
        try {
            ArrayList<DishesDbDetails> menuDbArray = u.loadMenuWithThisSupplier(selectedSupplier);
            dishes.clear();
            if (menuDbArray != null) {
                for (DishesDbDetails x : menuDbArray) {
                    DishesDetails tmp = new DishesDetails(x);
                    dishes.add(tmp);
                }

                tabMenu.setItems(null);
                tabMenu.setItems(dishes);
                back.setDisable(true);
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void loadNoIngr() {
        UserRemote u;
        if(MainControllerLogin.selected.equals("RMI"))
            u = Singleton.getInstance().methodRmi();
        else
            u = Singleton.getInstance().methodSocket();
        try {
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

    public void showEntreeIngr() {
        dishesStatus.setText("Entree");
        selectedIngredients = new ArrayList<>();
        tabIngr.setItems(null);
        ingredients = FXCollections.observableArrayList();
        if (entreeTF.getText().trim().length() != 0) {
            if (controllIngr(entreeTF.getText())) {
                entreeTF.setText("");
                status.setText("Change this dish");
                controllAddIngredients = true;
            } else {
                status.setText("Loaded");
                controllAddIngredients = true;
            }

        }
    }

    public void showMainIngr() {
        dishesStatus.setText("Main course");
        selectedIngredients = new ArrayList<>();
        tabIngr.setItems(null);
        ingredients = FXCollections.observableArrayList();
        if (mainTF.getText().trim().length() != 0)
            if (controllIngr(mainTF.getText())) {
                mainTF.setText("");
                status.setText("Change this dish");
                controllAddIngredients = true;
            } else {
                status.setText("Loaded");
                controllAddIngredients = true;
            }
    }

    public void showSideIngr() {
        dishesStatus.setText("Side dish");
        tabIngr.setItems(null);
        selectedIngredients = new ArrayList<>();
        ingredients = FXCollections.observableArrayList();
        if (sideTF.getText().trim().length() != 0)
            if (controllIngr(sideTF.getText())) {
                sideTF.setText("");
                status.setText("Change this dish");
                controllAddIngredients = true;
            } else {
                status.setText("Loaded");
                controllAddIngredients = true;
            }
    }

    public void showDrinkIngr() {
        dishesStatus.setText("Drink");
        tabIngr.setItems(null);
        selectedIngredients = new ArrayList<>();
        ingredients = FXCollections.observableArrayList();
        if (drinkTF.getText().trim().length() != 0)
            if (controllIngr(drinkTF.getText())) {
                drinkTF.setText("");
                status.setText("Change this dish");
                controllAddIngredients = true;
            } else {
                status.setText("Loaded");
                controllAddIngredients = true;
            }
    }

    public void showDessertIngr() {
        dishesStatus.setText("Dessert");
        tabIngr.setItems(null);
        selectedIngredients = new ArrayList<>();
        ingredients = FXCollections.observableArrayList();
        if (dessertTF.getText().trim().length() != 0)
            if (controllIngr(dessertTF.getText())) {
                dessertTF.setText("");
                status.setText("Change this dish");
                controllAddIngredients = true;
            } else {
                status.setText("Loaded");
                controllAddIngredients = true;
            }
    }

    private boolean controllIngr(String dish) {
        UserRemote u;
        if(MainControllerLogin.selected.equals("RMI"))
             u = Singleton.getInstance().methodRmi();
        else
            u = Singleton.getInstance().methodSocket();
        try {
            ArrayList<IngredientsDbDetails> ingredientsDbArray = u.searchIngredients(dish);
            if (ingredientsDbArray == null)
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
                dishesStatus.setText("Dishes status");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;

    }

    private void loadIngredients() {
        controllAddIngredients = false;
        UserRemote u;
        if(MainControllerLogin.selected.equals("RMI"))
            u = Singleton.getInstance().methodRmi();
        else
            u = Singleton.getInstance().methodSocket();

        try {
            ArrayList<IngredientsDbDetails> ingArray = u.loadIngr();
            ingredients.clear();
            for (IngredientsDbDetails x : ingArray)
                ingredients.add(new IngredientsGuiDetails(x));
            for (int i = 0; i < ingredients.size(); i++)
                for (IngredientsGuiDetails y : ingredientsNo)
                    if (ingredients.get(i).getIngr().equals(y.getIngr()))
                        ingredients.remove(i);


            tabIngr.setItems(null);
            tabIngr.setItems(ingredients);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void search() {
        searchedIngredients = FXCollections.observableArrayList();
        if (searchTF.getText().trim().length() != 0) {
            for (IngredientsGuiDetails x : ingredients) {
                if (x.getIngr().contains(searchTF.getText()))
                    searchedIngredients.add(x);
            }
            tabIngr.setItems(null);
            tabIngr.setItems(searchedIngredients);
        } else {
            tabIngr.setItems(null);
            tabIngr.setItems(ingredients);
        }
    }

    public void reload() {
        searchedIngredients = FXCollections.observableArrayList();
        searchTF.setText("");
        tabIngr.setItems(null);
        tabIngr.setItems(ingredients);
    }

    private void saveIngredientsForThisDish(String dishName, ArrayList<String> ingredients) {
        UserRemote u;
        if(MainControllerLogin.selected.equals("RMI"))
            u = Singleton.getInstance().methodRmi();
        else
            u = Singleton.getInstance().methodSocket();
        try {
            if (u.saveIngredients(dishName, ingredients)) {
                status.setText("Success!!");
                selectedIngredients = new ArrayList<>();
            } else {
                status.setText("There is a problem with this dish");
            }
            // controllIngredients = true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveIngredients(ActionEvent event) {
        if (dishesStatus.getText().equals("Entree")) {
            saveIngredientsForThisDish(entreeTF.getText(), selectedIngredients);
            deselect();
            selectedIngredients = new ArrayList<>();
            controllAddIngredients = true;
        } else if (dishesStatus.getText().equals("Main course")) {
            saveIngredientsForThisDish(mainTF.getText(), selectedIngredients);
            deselect();
            selectedIngredients = new ArrayList<>();
            controllAddIngredients = true;
        } else if (dishesStatus.getText().equals("Side dish")) {
            saveIngredientsForThisDish(sideTF.getText(), selectedIngredients);
            deselect();
            selectedIngredients = new ArrayList<>();
            controllAddIngredients = true;
        } else if (dishesStatus.getText().equals("Dessert")) {
            saveIngredientsForThisDish(dessertTF.getText(), selectedIngredients);
            deselect();
            selectedIngredients = new ArrayList<>();
            controllAddIngredients = true;
        } else if (dishesStatus.getText().equals("Drink")) {
            saveIngredientsForThisDish(drinkTF.getText(), selectedIngredients);
            deselect();
            selectedIngredients = new ArrayList<>();
            controllAddIngredients = true;
        } else {
            dishesStatus.setText("This plate already exists");
            deselect();
            selectedIngredients = new ArrayList<>();
            controllAddIngredients = true;
        }
    }

    @FXML
    public void deselect() {
        tabIngr.getSelectionModel().clearSelection();
        selectedIngredients = new ArrayList<>();
    }

    @FXML
    public void save() {
        String entree = entreeTF.getText();
        String main = mainTF.getText();
        String drink = drinkTF.getText();
        String dessert = dessertTF.getText();
        String side = sideTF.getText();
        if (entree.trim().isEmpty() && main.trim().isEmpty())
            status.setText("Insert an entree or a main course");
        else if (drink.trim().isEmpty()) status.setText("Insert a drink");
        else if (dessert.trim().isEmpty()) status.setText("Insert a dessert");
        else if (!controllAddIngredients) status.setText("Make sure you have added all the ingredients");
        else {
            UserRemote u;
            if(MainControllerLogin.selected.equals("RMI"))
                u = Singleton.getInstance().methodRmi();
            else
                u = Singleton.getInstance().methodSocket();
            try {
                if (u.updateMenu(selectedMenu[0], entreeTF.getText(), mainTF.getText(), dessertTF.getText(), sideTF.getText(), drinkTF.getText(), LocalDate.parse(selectedMenu[6]), LocalDate.parse(selectedMenu[6])))
                    status.setText("Success");
                handleLoad();
                if (dishes.isEmpty())
                    back.setDisable(false);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void backHome(ActionEvent event) {
        UserRemote u;
        if(MainControllerLogin.selected.equals("RMI"))
            u = Singleton.getInstance().methodRmi();
        else
            u = Singleton.getInstance().methodSocket();
        try {
            ArrayList<IngredientsDbDetails> ingr = new ArrayList<>();
            for(IngredientsGuiDetails x : ingredientsNo)
                ingr.add(new IngredientsDbDetails(x.getIngr()));
            boolean deleted = u.deleteSupplier(selectedSupplier, ingr);
            if (deleted) {
                selectedSupplier = null;
                status.setText("Deleted!!");
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } else {
                status.setText("Error");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteMenu() {
        if (selectedMenu != null) {
            UserRemote u;
            if (MainControllerLogin.selected.equals("RMI"))
                u = Singleton.getInstance().methodRmi();
            else
                u = Singleton.getInstance().methodSocket();
            try {
                if (u.deleteMenu(LocalDate.parse(selectedMenu[6]))) {
                    status.setText("Deleted");
                    handleLoad();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            selectedMenu = null;
        } else
            status.setText("Select a menu");
    }
}
