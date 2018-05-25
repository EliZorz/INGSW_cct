package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 03/05/2018.
 */
public class ChildSelectedTripGuiDetails {
    private StringProperty name;
    private StringProperty surname;
    private StringProperty cf;


    //default constructor String -> StringProperty per inserire dati in GUI
    public ChildSelectedTripGuiDetails(ChildSelectedTripDbDetails childDb){

        this.name = new SimpleStringProperty(childDb.getName());
        this.surname = new SimpleStringProperty(childDb.getSurname());
        this.cf = new SimpleStringProperty(childDb.getCf());

    }

    //getters
    public String getName(){
        return name.get();
    }

    public String getSurname(){
        return surname.get();
    }

    public String getCf(){
        return cf.get();
    }



    //setters
    public void setName(String value){ name.set(value); }

    public void setSurname(String value){
        surname.set(value);
    }

    public void setCf(String value){
        cf.set(value);
    }


    //stringproperty
    public StringProperty nameProperty(){ return name;}

    public StringProperty surnameProperty(){ return surname; }

    public StringProperty cfProperty(){return cf;}
}
