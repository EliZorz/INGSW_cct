package application.details;

import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 * Created by ELISA on 16/04/2018.
 */
public class SupplierDbDetails implements Serializable {
    private String piva;
    private String nameaz;
    private String tel;
    private String mail;
    private String address;
    private String cap;
    private String province;

    public SupplierDbDetails(SupplierGuiDetails supplierguiSp){


        this.piva = supplierguiSp.getPiva();
        this.nameaz = supplierguiSp.getNameaz();
        this.tel = supplierguiSp.getTel();
        this.mail = supplierguiSp.getMail();
        this.address = supplierguiSp.getAddress();
        this.cap = supplierguiSp.getCap();
        this.province = supplierguiSp.getProvince();

    }

    //default constructor String -> StringProperty per inserire dati in GUI
    public SupplierDbDetails(String piva, String nameaz, String tel, String mail, String address, String cap, String province){

        this.piva = piva;
        this.nameaz = nameaz;
        this.tel = tel;
        this.mail = mail;
        this.address = address;
        this.cap = cap;
        this.province = province;

    }

    //getters
    public String getPiva(){ return piva; }
    public String getNameaz(){ return nameaz; }
    public String getTel(){ return tel; }
    public String getMail(){ return mail; }
    public String getAddress(){ return address; }
    public String getCap(){ return cap; }
    public String getProvince(){ return province; }

    //setters
    public void setPiva(String value) { this.piva = value; }
    public void setNameaz(String value) { this.nameaz = value; }
    public void setTel(String value) { this.tel = value; }
    public void setMail(String value) { this.mail = value; }
    public void setAddress(String value) { this.address = value; }
    public void setCap(String value) { this.cap = value; }
    public void setProvince(String value) { this.province = value; }

}
