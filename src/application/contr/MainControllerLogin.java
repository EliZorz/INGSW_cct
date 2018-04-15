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
        String selected = (String) select.getSelectionModel().getSelectedItem();

        try {
            if (selected.equals("")){
                System.out.println("User did not choose.\nRetry...");
                lblStatus.setText("RMI or SOCKET?");
            } else if(usr.trim().isEmpty() || usr == null || pwd.trim().isEmpty() || pwd == null){
                this.renameLabel("Insert username, password");

            } else if(selected.equals("RMI")){
                System.out.println("User chose RMI.\nProceed...");

                //... vd singleton
                UserRemote u = Singleton.getInstance().methodRmi();

                boolean result = u.funzLog(usr, pwd);

                if (result){

                    this.renameLabel("Loggato");

                    new GuiNew("MenuIniziale");

                } else{
                    this.renameLabel("Insert correct data");
                }

                //chiamare funzione da scrivere in questa classe che riceve da ServerImpl il ResultSet result e lo analizza
                //this.isLogged(ch.getUserService().funzLog(usr, pwd));


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

                UserRemote u = Singleton.getInstance().methodSocket();

                boolean result = u.funzLog(usr, pwd);

                if (result){

                    this.renameLabel("Loggato");
                    new GuiNew("MenuIniziale");


                }else{
                    this.renameLabel("Credenziali sbagliate");
                }

               // this.isLogged(ch.getUserService().funzLog(usr,pwd));  //chiama isLogged se il resultset è true

            } else {
                lblStatus.setText("RMI or SOCKET?");
            }











        } catch (Exception se) {
            se.printStackTrace();
        }

    }

    public void renameLabel(String st){

        lblStatus.setText(st);
    }

}
