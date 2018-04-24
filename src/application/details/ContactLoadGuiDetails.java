package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 * Created by ELISA on 21/04/2018.
 */
public class ContactLoadGuiDetails {

    private StringProperty surname;
    private StringProperty cf;
    private StringProperty isDoc;
    private StringProperty isGuardian;
    private StringProperty isContact;

    //default constructor String -> StringProperty per inserire dati in GUI
    public ContactLoadGuiDetails(ContactLoadDbDetails childDb){

        this.surname = new SimpleStringProperty(childDb.getSurname());
        this.cf = new SimpleStringProperty((childDb.getCf()));
        this.isDoc = new SimpleStringProperty(childDb.getIsDoc());
        this.isGuardian = new SimpleStringProperty(childDb.getIsGuardian());
        this.isContact = new SimpleStringProperty(childDb.getIsContact());

    }

    //getters
    public String getSurname(){
        return surname.get();
    }

    public String getCf(){ return cf.get(); }

    public String getIsDoc(){
        return isDoc.get();
    }

    public String getIsGuardian(){ return isGuardian.get(); }

    public String getIsContact(){ return isContact.get(); }


    //setters
    public void setSurname(String value){
        surname.set(value);
    }

    public void setCf(String value) {cf.set(value);}

    public void setIsDoc(String value){
        isDoc.set(value);
    }

    public void setIsGuardian(String value){ isGuardian.set(value); }

    public void setIsContact(String value) { isContact.set(value); }

    //stringproperty
    public StringProperty surnameProperty(){ return surname; }

    public StringProperty cfProperty(){ return cf; }

    public StringProperty isDocProperty(){
        return isDoc;
    }

    public StringProperty isGuardianProperty(){
        return isGuardian;
    }

    public StringProperty isContactProperty(){
        return isContact;
    }

}
