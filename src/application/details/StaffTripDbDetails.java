package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 25/04/2018.
 */
public class StaffTripDbDetails implements Serializable {
    private String name;
    private String surname;
    private String cf;

    public StaffTripDbDetails (StaffTripGuiDetails stafftrip) {
        this.name = stafftrip.getName();
        this.surname = stafftrip.getSurname();
        this.cf = stafftrip.getCf();
    }

    public StaffTripDbDetails(String name, String surname, String cf) {
        this.name = name;
        this.surname = surname;
        this.cf = cf;
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }

    public String getCf(){
        return cf;
    }

    public void setName(String value){ this.name = value; }

    public void setSurname(String value){ this.surname = value; }

    public void setCf(String value){
        this.cf = value;
    }
}
