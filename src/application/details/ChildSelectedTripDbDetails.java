package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 03/05/2018.
 */
public class ChildSelectedTripDbDetails implements Serializable {


    private String name;
    private String surname;
    private String cf;


    //default constructor StringProperty -> String per salvare dati da GUI a DB
    public ChildSelectedTripDbDetails(ChildSelectedTripGuiDetails childguiSp){


        this.name = childguiSp.getName();
        this.surname = childguiSp.getSurname();
        this.cf = childguiSp.getCf();

    }

    //default constructor String -> StringProperty per inserire dati in GUI
    public ChildSelectedTripDbDetails(String name, String surname, String cf){

        this.name = name;
        this.surname = surname;
        this.cf = cf;

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


    //setters
    public void setName(String value){ this.name = value; }

    public void setSurname(String value){ this.surname = value; }

    public void setCf(String value){
        this.cf = value;
    }


}

