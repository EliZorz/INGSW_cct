package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SpecialGuiDetails {
    private StringProperty refCode;
    private StringProperty allergies;

    public SpecialGuiDetails(SpecialDbDetails special){
        this.refCode = new SimpleStringProperty(special.getRefCode());
        this.allergies = new SimpleStringProperty(special.getAllergies());
    }

    public String getRefCode(){return refCode.get();}

    public String getAllergies(){return allergies.get();}

    public void setRefCode(String refCode){this.refCode.set(refCode);}

    public void setAllergies(String allergies){this.allergies.set(allergies);}

    public StringProperty allergiesProperty() { return allergies; }

    public StringProperty refCodeProperty() { return refCode; }
}
