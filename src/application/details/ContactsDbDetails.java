package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 18/04/2018.
 */
public class ContactsDbDetails implements Serializable {

    private String name;
    private String surname;
    private String cf;
    private String mail;
    private String tel;
    private String bornOn;
    private String bornWhere;
    private String address;
    private String cap;
    private String province;
    private String isDoc;
    private String isGuardian;
    private String isContact;



    //default constructor StringProperty -> String per salvare dati da GUI a DB
    public ContactsDbDetails(ContactsGuiDetails childguiSp){


        this.name = childguiSp.getName();
        this.surname = childguiSp.getSurname();
        this.cf = childguiSp.getCf();
        this.mail = childguiSp.getMail();
        this.tel = childguiSp.getTel();
        this.bornOn = childguiSp.getBornOn();
        this.bornWhere = childguiSp.getBornWhere();
        this.address = childguiSp.getAddress();
        this.cap = childguiSp.getCap();
        this.province = childguiSp.getProvince();
        this.isDoc = childguiSp.getIsDoc();
        this.isGuardian = childguiSp.getIsGuardian();
        this.isContact = childguiSp.getIsContact();

    }

    //default constructor String -> StringProperty per inserire dati in GUI
    public ContactsDbDetails(String name, String surname, String cf, String mail, String tel, String bornOn, String bornWhere, String address, String cap, String province, String isDoc, String isGuardian, String isContact){

        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.tel = tel;
        this.mail = mail;
        this.bornOn = bornOn;
        this.bornWhere = bornWhere;
        this.address = address;
        this.cap = cap;
        this.province = province;
        this.isDoc = isDoc;
        this.isGuardian = isGuardian;
        this.isContact = isContact;

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

    public String getTel(){
        return tel;
    }

    public String getMail(){ return mail; }

    public String getBornOn(){
        return bornOn;
    }

    public String getBornWhere(){
        return bornWhere;
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

    public String getIsDoc(){
        return isDoc;
    }

    public String getIsGuardian(){
        return isGuardian;
    }

    public String getIsContact(){
        return isContact;
    }


    //setters
    public void setName(String value){ this.name = value; }

    public void setSurname(String value){
        this.surname = value;
    }

    public void setCf(String value){
        this.cf = value;
    }

    public void setTel(String value){
        this.tel = value;
    }

    public void setMail(String value){
        this.mail = value;
    }

    public void setBornOn(String value){
        this.bornOn = value;
    }

    public void setBornWhere(String value){
        this.bornWhere = value;
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

    public void setIsDoc(String value){
        this.isDoc = value;
    }

    public void setIsGuardian(String value){
        this.isGuardian = value;
    }

    public void setIsContact(String value){
        this.isContact = value;
    }
}
