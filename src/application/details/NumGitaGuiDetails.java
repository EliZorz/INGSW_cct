package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 27/04/2018.
 */
public class NumGitaGuiDetails {
    private StringProperty numGita;

    public NumGitaGuiDetails (NumGitaDbDetails number){
        this.numGita = new SimpleStringProperty(number.getNumGita());
    }

    public String getNumGita(){ return numGita.getValue(); }

    public void setNumGita(String value) { numGita.setValue(value); }

    public StringProperty numGitaProperty(){
        return numGita;
    }
}
