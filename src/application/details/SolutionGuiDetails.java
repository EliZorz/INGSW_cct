package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 10/05/2018.
 */
public class SolutionGuiDetails {
    private StringProperty name;
    private StringProperty surname;
    private StringProperty cf;
    private StringProperty codRif;


    //default constructor String -> StringProperty per inserire dati in GUI
    public SolutionGuiDetails(SolutionDbDetails childDb){

        this.name = new SimpleStringProperty(childDb.getName());
        this.surname = new SimpleStringProperty(childDb.getSurname());
        this.cf = new SimpleStringProperty(childDb.getCf());
        this.codRif = new SimpleStringProperty(childDb.getCodRif());

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

    public String getCodRif() { return codRif.get();}



    //setters
    public void setName(String value){ name.set(value); }

    public void setSurname(String value){
        surname.set(value);
    }

    public void setCf(String value){
        cf.set(value);
    }

    public void setCodRif(String value){ codRif.set(value);}


    //stringproperty
    public StringProperty nameProperty(){ return name;}

    public StringProperty surnameProperty(){ return surname; }

    public StringProperty cfProperty(){return cf;}

    public StringProperty codRifProperty(){ return codRif;}
}