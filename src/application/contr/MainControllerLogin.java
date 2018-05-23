package application.contr;

import application.LookupCall;
import application.gui.GuiNew;
import application.Interfaces.UserRemote;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


/**
 * Created by ELISA on 23/03/2018.
 */
public class MainControllerLogin implements Initializable {

    public static String selected = null;  //per la scelta tra rmi e socket


    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ChoiceBox select;

    @FXML
    private Label lblStatus;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtPassword.clear();
        txtUsername.clear();

    }


    public void handleLogin() throws SQLException {
        String usr = txtUsername.getText();
        String pwd = txtPassword.getText();

        selected = (String) select.getSelectionModel().getSelectedItem();

        try {
            if (selected == null){
                System.out.println("User did not choose.\nRetry...");
                lblStatus.setText("RMI or SOCKET?");
            } else
            if(usr.trim().isEmpty() || pwd.trim().isEmpty()){
                this.renameLabel("Insert username, password");

            } else if(selected.equals("RMI")){
                System.out.println("User chose RMI.\nProceed...");

                UserRemote u = LookupCall.getInstance().methodRmi();

                boolean result = u.funzLog(usr, pwd);

                if (result){

                    this.renameLabel("Logged in.");
                    new GuiNew("MenuIniziale");

                } else{
                    this.renameLabel("Insert correct data.");
                }

            } else if (selected.equals("SOCKET")){
                System.out.println("User chose SOCKET.\nProceed...");

                UserRemote u = LookupCall.getInstance().methodSocket();

                boolean result = u.funzLog(usr, pwd);

                if (result){
                    this.renameLabel("Logged in.");
                    new GuiNew("MenuIniziale");

                }else{
                    this.renameLabel("Wrong data.");
                }
                // this.isLogged(ch.getUserService().funzLog(usr,pwd));  //chiama isLogged se il resultset è true

            } else {
                lblStatus.setText("Something wrong.");
            }

        } catch (Exception se) {
            se.printStackTrace();
        }

    }

    private void renameLabel(String st){
        lblStatus.setText(st);
    }

}