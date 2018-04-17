package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 16/04/2018.
 */
public class SupplierGuiDetails {
    private StringProperty piva;
    private StringProperty nameaz;
    private StringProperty tel;
    private StringProperty mail;
    private StringProperty address;
    private StringProperty cap;
    private StringProperty province;

    public SupplierGuiDetails (SupplierDbDetails supplierDb){

        this.piva = new SimpleStringProperty(supplierDb.getPiva());
        this.nameaz = new SimpleStringProperty(supplierDb.getNameaz());
        this.tel = new SimpleStringProperty(supplierDb.getTel());
        this.mail = new SimpleStringProperty(supplierDb.getMail());
        this.address = new SimpleStringProperty(supplierDb.getAddress());
        this.cap = new SimpleStringProperty(supplierDb.getCap());
        this.province = new SimpleStringProperty(supplierDb.getProvince());

    }

    //getters
    public String getPiva(){
        return piva.get();
    }

    public String getNameaz(){
        return nameaz.get();
    }

    public String getTel(){ return tel.get(); }

    public String getMail(){ return mail.get(); }

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
    public void setPiva(String value) {
        piva.set(value);
    }

    public void setNameaz(String value) {
        nameaz.set(value);
    }

    public void setTel(String value) {
        tel.set(value);
    }

    public void setMail(String value) {
        mail.set(value);
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
    public StringProperty pivaProperty(){ return piva; }

    public StringProperty nameazProperty(){
        return nameaz;
    }

    public StringProperty telProperty(){
        return tel;
    }

    public StringProperty mailProperty(){
        return mail;
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
