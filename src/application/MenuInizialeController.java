package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuInizialeController {


    @FXML
    void MenuBase(ActionEvent event)throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuBase.fxml"));
        Parent p = (Parent) fxmlLoader.load();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Stage stage = new Stage();
        stage.setScene(new Scene(p,screenSize.getWidth(),screenSize.getHeight()));
        stage.show();
    }

    @FXML
    void MenusSpeciale(ActionEvent event)throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuSpeciale.fxml"));
        Parent p = (Parent) fxmlLoader.load();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Stage stage = new Stage();
        stage.setScene(new Scene(p,screenSize.getWidth(),screenSize.getHeight()));
        stage.show();
    }

    @FXML
    void GiteDisponibili(ActionEvent event)throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GiteDisponibili.fxml"));
        Parent p = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.show();
    }

    @FXML
    void NuovaGita(ActionEvent event)throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NuovaGita.fxml"));
        Parent p = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.show();

    }


    @FXML
    void fornitore(ActionEvent event)throws Exception{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Fornitore.fxml"));

            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene (root1));
            stage.show();

    }
@FXML
    void bambini(ActionEvent event) throws Exception{

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Bambini.fxml"));

            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene (root1));
            stage.show();
            //stage.close(); // in generale per chiudere una pagina
            stage.setFullScreen(true);


    }
    @FXML

    void PersonaleInterno(ActionEvent event)throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PersonaleInterno.fxml"));

        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene (root1));
        stage.show();
           stage.isFullScreen();




    }


    @FXML
    public void ReturnToHomePage(ActionEvent event)throws Exception{
        //.setOnAction(e -> Platform.exit());

        //System.exit(0);  //in questo modo chiudo tutto
       // Platform.exit(); altro modo per chiudere tutto
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;




    @FXML
    void initialize() {


    }



}


