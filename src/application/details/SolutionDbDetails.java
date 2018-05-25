package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 10/05/2018.
 */
public class SolutionDbDetails implements Serializable {


    private String name;
    private String surname;
    private String cf;
    public String codRif;


    //default constructor StringProperty -> String per salvare dati da GUI a DB
    public SolutionDbDetails(SolutionGuiDetails childguiSp){


        this.name = childguiSp.getName();
        this.surname = childguiSp.getSurname();
        this.cf = childguiSp.getCf();
        this.codRif = childguiSp.getCodRif();

    }

    //default constructor String -> StringProperty per inserire dati in GUI
    public SolutionDbDetails(String name, String surname, String cf, String codRif){

        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.codRif = codRif;

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

    public String getCodRif() { return codRif; }


    //setters
    public void setName(String value){ this.name = value; }

    public void setSurname(String value){ this.surname = value; }

    public void setCf(String value){
        this.cf = value;
    }

    public void setCodRif(String value) { this.codRif = value; }


}
