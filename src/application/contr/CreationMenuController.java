package application.contr;

import application.Interfaces.UserRemote;
import application.Singleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.time.LocalDate;


public class CreationMenuController {

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
        if(MainControllerLogin.selected.equals("RMI")) {

            try {
                UserRemote u = Singleton.getInstance().methodRmi();
                boolean addSuccess = u.addMenu(num, entree, main, dessert, side, drink, day);

                if(addSuccess){
                    label1.setText("Success!! You hava a new menu!");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
