package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 17/04/2018.
 */
public class CodRifChildGuiDetails {
    private StringProperty codRif;

    public CodRifChildGuiDetails (CodRifChildDbDetails codDb){
        this.codRif = new SimpleStringProperty(codDb.getCodRif());
    }

    public String getCodRif(){ return codRif.getValue(); }

    public void setCodRif(String value) { codRif.setValue(value); }

    public StringProperty codRifProperty(){
        return codRif;
    }
}
