package application.contr;

import application.rmi.client.Client;
import com.mysql.jdbc.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ELISA on 23/03/2018.
 */
public class MainController {


    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private MenuButton btnLogin;

    @FXML
    private Label lblStatus;

    public void handleLogin() throws SQLException {

        PreparedStatement st = null;
        ResultSet result;

        String usr = txtUsername.getText().toString();
        String pwd = txtPassword.getText().toString();
        String queryLog = "SELECT * FROM sys.login WHERE Username = ? AND Password = ? ";//"SELECT * FROM UserIn WHERE Username = ? AND Password = ? " ;

        result = st.executeQuery();

        boolean res = false;

        try {
            Database receivedCon = new Database();
            Connection connectionOK = receivedCon.databaseCon();
            if(connectionOK != null)
                System.out.println("Connection successful");
            else
                System.out.println("Connection failed");


            st = connectionOK.prepareStatement(queryLog);
            st.setString(1, usr);
            st.setString(2, pwd);

            //extract data from dataSet
            if( btnLogin.getItems().equals("RMI")){
                System.out.println("User chose RMI.\nProceed...");
                if( !result.next() ) {
                    //ritorna false al client che mostra messaggio di errore tramite label
                    res = false;
                    //lblStatus.setText("Login failed");
                    //System.out.println("No user like that in your database");
                } else {
                    result.beforeFirst();
                    while (result.next()) {
                        String usrFound = result.getString("Username");
                        System.out.println("USER: " + usrFound);
                        String pwdFound = result.getString("Password");
                        System.out.println("PASSWORD: " + pwdFound);
                    }

                    res = true;
                }

                //chiamo Client funzLog passando res MA PRIMA DEVO FARE LA CONNESSIONE!!!!!!!!!!!!!!!!!!!!!!

                new Client().funzLog(res);




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
                    //lblStatus.setText("Login SUCCESSFUL.");
                    //System.out.println("I found your user!");
                /* questa parte la fa il client quando riceve true!!!!
                try{
                    //apre finestra MenuIniziale
                    new GuiNew().openFxml("/application/MenuIniziale.fxml");

                } catch (Exception ex){
                    ex.printStackTrace();
                }
                */
                    //mi occupo poi di vedere se ha scelto RMI/SOCKET -> DOVE?????????????????????

            } else if (btnLogin.getItems().equals("SOCKET")){
                //CONNECT WITH SOCKET... dovrò passargli il risultato di una if else (sul database come per RMI)


            } else {
                lblStatus.setText("Choose: RMI or SOCKET?");
            }


        } catch (Exception se) {
            se.printStackTrace();
        }

    }

}
