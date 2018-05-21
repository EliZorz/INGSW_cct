package application.contr;

import application.Singleton;
import application.gui.GuiNew;
import application.Interfaces.UserRemote;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;




/**
 * Created by ELISA on 23/03/2018.
 */
public class MainControllerLogin {

    public static String selected = null;  //per la scelta tra rmi e socket


    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ChoiceBox select;

    @FXML
    private Label lblStatus;




    public void handleLogin() throws SQLException {
        String usr = txtUsername.getText().toString();
        String pwd = txtPassword.getText().toString();

        selected = (String) select.getSelectionModel().getSelectedItem();

        try {
             if (selected == null){
                System.out.println("User did not choose.\nRetry...");
                lblStatus.setText("RMI or SOCKET?");
            } else
                if(usr.trim().isEmpty() || usr == null || pwd.trim().isEmpty() || pwd == null){
                this.renameLabel("Insert username, password");

            } else if(selected.equals("RMI")){
                System.out.println("User chose RMI.\nProceed...");

                UserRemote u = Singleton.getInstance().methodRmi();

                boolean result = u.funzLog(usr, pwd);

                if (result){

                    this.renameLabel("Logged in.");
                    new GuiNew("MenuIniziale");

                } else{
                    this.renameLabel("Insert correct data.");
                }

            } else if (selected.equals("SOCKET")){
                System.out.println("User chose SOCKET.\nProceed...");

                UserRemote u = Singleton.getInstance().methodSocket();

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

    public void renameLabel(String st){
        lblStatus.setText(st);
    }

}
