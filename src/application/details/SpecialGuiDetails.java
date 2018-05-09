package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class SpecialGuiDetails {
    private StringProperty CF;
    private StringProperty allergie;

    public SpecialGuiDetails(SpecialDbDetails special){
        this.CF = new SimpleStringProperty(special.getCF());
        this.allergie = new SimpleStringProperty(special.getAllergie());

    }

    public String getCF(){return CF.get();}

    public String getAllergie() {
        return allergie.get();
    }

    public void setCF(String CF){
        this.CF.set(CF);
    }

    public void setAllergie(String allergie){
        this.allergie.set(allergie);
    }

    public StringProperty CFProperty() {
        return CF;
    }

    public StringProperty allergieProperty(){
        return allergie;
    }
}