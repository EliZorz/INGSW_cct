package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 18/04/2018.
 */
public class StaffDbDetails implements Serializable {

    private String name;
    private String surname;
    private String cf;
    private String mail;
    private String bornOn;
    private String bornWhere;
    private String residence;
    private String address;
    private String cap;
    private String province;


    //default constructor StringProperty -> String per salvare dati da GUI a DB
    public StaffDbDetails(StaffGuiDetails childguiSp){


        this.name = childguiSp.getName();
        this.surname = childguiSp.getSurname();
        this.cf = childguiSp.getCf();
        this.mail = childguiSp.getMail();
        this.bornOn = childguiSp.getBornOn();
        this.bornWhere = childguiSp.getBornWhere();
        this.residence = childguiSp.getResidence();
        this.address = childguiSp.getAddress();
        this.cap = childguiSp.getCap();
        this.province = childguiSp.getProvince();

    }

    //default constructor String -> StringProperty per inserire dati in GUI
    public StaffDbDetails(String name, String surname, String cf, String mail, String bornOn, String bornWhere, String residence, String address, String cap, String province){

        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.mail = mail;
        this.bornOn = bornOn;
        this.bornWhere = bornWhere;
        this.residence = residence;
        this.address = address;
        this.cap = cap;
        this.province = province;

    }

    //getters
    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }

    public String getCf(){
        return cf;
    }

    public String getMail() { return mail; }

    public String getBornOn(){
        return bornOn;
    }

    public String getBornWhere(){
        return bornWhere;
    }

    public String getResidence(){
        return residence;
    }

    public String getAddress(){
        return address;
    }

    public String getCap(){
        return cap;
    }

    public String getProvince(){
        return province;
    }


    //setters
    public void setName(String value){ this.name = value; }

    public void setSurname(String value){ this.surname = value; }

    public void setCf(String value){
        this.cf = value;
    }

    public void setMail(String value) { this.mail = value; }

    public void setBornOn(String value){
        this.bornOn = value;
    }

    public void setBornWhere(String value){
        this.bornWhere = value;
    }

    public void setResidence(String value){
        this.residence = value;
    }

    public void setAddress(String value){
        this.address = value;
    }

    public void setCap(String value){
        this.cap = value;
    }

    public void setProvince(String value){
        this.province = value;
    }
}