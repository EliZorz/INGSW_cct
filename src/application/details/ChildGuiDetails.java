package application.details;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class ChildGuiDetails {
    private StringProperty name;
    private StringProperty surname;
    private StringProperty cf;
    private StringProperty bornOn;
    private StringProperty bornWhere;
    private StringProperty residence;
    private StringProperty address;
    private StringProperty cap;
    private StringProperty province;

    //default constructor String -> StringProperty per inserire dati in GUI
    public ChildGuiDetails(ChildDbDetails childDb){

        this.name = new SimpleStringProperty(childDb.getName());
        this.surname = new SimpleStringProperty(childDb.getSurname());
        this.cf = new SimpleStringProperty(childDb.getCf());
        this.bornOn = new SimpleStringProperty(childDb.getBornOn());
        this.bornWhere = new SimpleStringProperty(childDb.getBornWhere());
        this.residence = new SimpleStringProperty(childDb.getResidence());
        this.address = new SimpleStringProperty(childDb.getAddress());
        this.cap = new SimpleStringProperty(childDb.getCap());
        this.province = new SimpleStringProperty(childDb.getProvince());

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

    public String getBornOn(){
        return bornOn.get();
    }

    public String getBornWhere(){
        return bornWhere.get();
    }

    public String getResidence(){
        return residence.get();
    }

    public String getAddress(){
        return address.get();
    }

    public String getCap(){
        return cap.get();
    }

    public String getProvince(){
        return province.get();
    }


    //setters
    public void setName(String value){
        name.set(value);
    }

    public void setSurame(String value){
        surname.set(value);
    }

    public void setCf(String value){
        cf.set(value);
    }

    public void setBornOn(String value){
        bornOn.set(value);
    }

    public void setBornWhere(String value){
        bornWhere.set(value);
    }

    public void setResidence(String value){
        residence.set(value);
    }

    public void setAddress(String value){
        address.set(value);
    }

    public void setCap(String value){
        cap.set(value);
    }

    public void setProvince(String value){
        province.set(value);
    }

    //stringproperty
    public StringProperty nameProperty(){
        return name;
    }

    public StringProperty surnameProperty(){
        return surname;
    }

    public StringProperty cfProperty(){
        return cf;
    }

    public StringProperty bornOnProperty(){
        return bornOn;
    }

    public StringProperty bornWhereProperty(){
        return bornWhere;
    }

    public StringProperty residenceProperty(){
        return residence;
    }

    public StringProperty addressProperty(){
        return address;
    }

    public StringProperty capProperty(){
        return cap;
    }

    public StringProperty provinceProperty(){
        return province;
    }


}
