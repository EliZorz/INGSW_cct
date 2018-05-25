package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 18/04/2018.
 */
public class ContactsGuiDetails {

    private StringProperty name;
    private StringProperty surname;
    private StringProperty cf;
    private StringProperty tel;
    private StringProperty mail;
    private StringProperty bornOn;
    private StringProperty bornWhere;
    private StringProperty address;
    private StringProperty cap;
    private StringProperty province;
    private StringProperty isDoc;
    private StringProperty isGuardian;
    private StringProperty isContact;

    //default constructor String -> StringProperty per inserire dati in GUI
    public ContactsGuiDetails(ContactsDbDetails childDb){

        this.name = new SimpleStringProperty(childDb.getName());
        this.surname = new SimpleStringProperty(childDb.getSurname());
        this.cf = new SimpleStringProperty(childDb.getCf());
        this.tel = new SimpleStringProperty(childDb.getTel());
        this.mail = new SimpleStringProperty(childDb.getMail());
        this.bornOn = new SimpleStringProperty(childDb.getBornOn());
        this.bornWhere = new SimpleStringProperty(childDb.getBornWhere());
        this.address = new SimpleStringProperty(childDb.getAddress());
        this.cap = new SimpleStringProperty(childDb.getCap());
        this.province = new SimpleStringProperty(childDb.getProvince());
        this.isDoc = new SimpleStringProperty(childDb.getIsDoc());
        this.isGuardian = new SimpleStringProperty(childDb.getIsGuardian());
        this.isContact = new SimpleStringProperty(childDb.getIsContact());

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

    public String getTel(){ return tel.get(); }

    public String getMail(){ return mail.get(); }

    public String getBornOn(){
        return bornOn.get();
    }

    public String getBornWhere(){
        return bornWhere.get();
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

    public String getIsDoc(){
        return isDoc.get();
    }

    public String getIsGuardian(){ return isGuardian.get(); }

    public String getIsContact(){ return isContact.get(); }


    //setters
    public void setName(String value){ name.set(value); }

    public void setSurname(String value){
        surname.set(value);
    }

    public void setCf(String value){
        cf.set(value);
    }

    public void setTel(String value){
        tel.set(value);
    }

    public void setMail(String value){
        mail.set(value);
    }

    public void setBornOn(String value){
        bornOn.set(value);
    }

    public void setBornWhere(String value){
        bornWhere.set(value);
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

    public void setIsDoc(String value){
        isDoc.set(value);
    }

    public void setIsGuardian(String value){ isGuardian.set(value); }

    public void setIsContact(String value) { isContact.set(value); }

    //stringproperty
    public StringProperty nameProperty(){
        return name;
    }

    public StringProperty surnameProperty(){ return surname; }

    public StringProperty cfProperty(){
        return cf;
    }

    public StringProperty telProperty(){
        return tel;
    }

    public StringProperty mailProperty(){
        return mail;
    }

    public StringProperty bornOnProperty(){
        return bornOn;
    }

    public StringProperty bornWhereProperty(){
        return bornWhere;
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
