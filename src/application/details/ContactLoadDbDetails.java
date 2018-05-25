package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 21/04/2018.
 */
public class ContactLoadDbDetails implements Serializable {

    private String surname;
    private String cf;
    private String isDoc;
    private String isGuardian;
    private String isContact;



    //default constructor StringProperty -> String per salvare dati da GUI a DB
    public ContactLoadDbDetails(ContactsGuiDetails childguiSp){

        this.surname = childguiSp.getSurname();
        this.cf = childguiSp.getCf();
        this.isDoc = childguiSp.getIsDoc();
        this.isGuardian = childguiSp.getIsGuardian();
        this.isContact = childguiSp.getIsContact();

    }

    //default constructor String -> StringProperty per inserire dati in GUI
    public ContactLoadDbDetails(String surname, String cf, String isDoc, String isGuardian, String isContact){

        this.surname = surname;
        this.cf = cf;
        this.isDoc = isDoc;
        this.isGuardian = isGuardian;
        this.isContact = isContact;

    }

    //getters
    public String getSurname(){
        return surname;
    }

    public String getCf(){ return cf; }

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
    public void setSurname(String value){
        this.surname = value;
    }

    public void setCf(String value) { this.cf = value; }

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

