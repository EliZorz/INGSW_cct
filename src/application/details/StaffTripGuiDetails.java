package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 25/04/2018.
 */
public class StaffTripGuiDetails {
    private StringProperty name;
    private StringProperty surname;
    private StringProperty cf;

    public StaffTripGuiDetails(StaffTripDbDetails stafftrip){
        this.name = new SimpleStringProperty(stafftrip.getName());
        this.surname = new SimpleStringProperty(stafftrip.getSurname());
        this.cf = new SimpleStringProperty(stafftrip.getCf());
    }

    public String getName(){
        return name.get();
    }

    public String getSurname(){
        return surname.get();
    }

    public String getCf(){
        return cf.get();
    }

    public void setName(String value){ name.set(value); }

    public void setSurname(String value){
        surname.set(value);
    }

    public void setCf(String value){
        cf.set(value);
    }

    public StringProperty nameProperty(){ return name;}

    public StringProperty surnameProperty(){ return surname; }

    public StringProperty cfProperty(){return cf;}
}
