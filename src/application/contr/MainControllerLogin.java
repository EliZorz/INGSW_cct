package application.contr;

import application.Interfaces.ServicesManager;
import application.rmi.client.RmiManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ELISA on 23/03/2018.
 */
public class MainControllerLogin {


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



        try {

            //extract data from dataSet
            String selected = (String) select.getSelectionModel().getSelectedItem();

            if(selected.equals("RMI")){
                System.out.println("User chose RMI.\nProceed...");

                //LA CONNESSIONE AL DB DEVE FARLA LA FUNZIONE funzLog
                ServicesManager ch = new RmiManager();

                //chiamare funzione da scrivere in questa classe che riceve da ServerImpl il ResultSet result e lo analizza
                this.isLogged(ch.getUserService().funzLog(usr, pwd));


                /* da mettere nel logOut !!!!!!!!!!!!!!!!!!!!!!!!!!
                if (connectionOK != null) {
                    try {
                        connectionOK.close();
                    } catch (SQLException sqe) {
                        sqe.printStackTrace();
                    }
                }
                if(result != null){
                    try{
                        result.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                */


            } else if (selected.equals("SOCKET")){
                System.out.println("User chose SOCKET.\nProceed...");









            } else {
                lblStatus.setText("RMI or SOCKET?");
            }


        } catch (Exception se) {
            se.printStackTrace();
        }

    }


    public void isLogged(ResultSet result){

        try{
            if( !result.next() ) {
                lblStatus.setText("Login failed");
                System.out.println("No user like that in your database");
            } else {
                result.beforeFirst();
                while (result.next()) {
                    String usrFound = result.getString("Username");
                    System.out.println("USER: " + usrFound);
                    String pwdFound = result.getString("Password");
                    System.out.println("PASSWORD: " + pwdFound);
                }

                try {
                    new GuiNew().openFxml("../../gui/MenuIniziale.fxml");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
