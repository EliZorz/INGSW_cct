package application.details;

import javafx.fxml.Initializable;

import java.io.Serializable;

/**
 * Created by ELISA on 25/04/2018.
 */
public class ChildTripDbDetails implements Serializable {
    private String name;
    private String surname;
    private String cf;

    public ChildTripDbDetails (ChildTripGuiDetails childtrip) {
        this.name = childtrip.getName();
        this.surname = childtrip.getSurname();
        this.cf = childtrip.getCf();
    }

    public ChildTripDbDetails(String name, String surname, String cf) {
        this.name = name;
        this.surname = surname;
        this.cf = cf;
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }

    public String getCf(){
        return cf;
    }

    public void setName(String value){ this.name = value; }

    public void setSurname(String value){ this.surname = value; }

    public void setCf(String value){
        this.cf = value;
    }

}
