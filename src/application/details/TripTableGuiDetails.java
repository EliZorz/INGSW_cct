package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 23/04/2018.
 */
public class TripTableGuiDetails {

    private StringProperty layover;
    private StringProperty datetimeDep;
    private StringProperty datetimeArr;
    private StringProperty datetimeCom;
    //private StringProperty numTrip;
    private StringProperty accomodation;
    //private StringProperty numChildren;
    //private StringProperty numStaff;

    public TripTableGuiDetails(TripTableDbDetails tripDb) {
        this.layover = new SimpleStringProperty(tripDb.getLayover());
        this.datetimeDep = new SimpleStringProperty(tripDb.getDep());
        this.datetimeArr = new SimpleStringProperty(tripDb.getArr());
        this.datetimeCom = new SimpleStringProperty(tripDb.getCom());
        //this.numTrip = new SimpleStringProperty(tripDb.getNumTrip());
        this.accomodation = new SimpleStringProperty(tripDb.getAccomodation());
        //this.numChildren = new SimpleStringProperty(tripDb.getNumChildren());
        //this.numStaff = new SimpleStringProperty(tripDb.getNumStaff());
    }

    public String getLayover() { return layover.get();}
    public String getDep(){ return datetimeDep.get();}
    public String getArr(){ return datetimeArr.get();}
    public String getCom(){ return datetimeCom.get();}
    //public String getNumTrip(){ return numTrip.get(); }
    public String getAccomodation(){ return accomodation.get();}
    //public String getNumChildren(){ return numChildren.get();}
    //public String getNumStaff() {return numStaff.get();}

    public void setLayover(String value){layover.set(value);}
    public void setDep(String value){datetimeDep.set(value);}
    public void setArr(String value){datetimeArr.set(value);}
    public void setCom(String value){datetimeCom.set(value);}
    //public void setNumTrip(String value){numTtrip.set(value);}
    public void setAccomodation(String value){accomodation.set(value);}
    //public void setNumChildren(String value){numChildren.set(value);}
    //public void setNumStaff(String value){numStaff.set(value);}

    public StringProperty layoverProperty(){ return layover; }
    public StringProperty depProperty(){ return datetimeDep;}
    public StringProperty arrProperty() { return datetimeArr;}
    public StringProperty comProperty() { return datetimeCom;}
    //public StringProperty numTripProperty(){ return numTrip;}
    public StringProperty accomodationProperty(){return accomodation;}
    //public StringProperty numChildrenProperty(){ return numChildren;}
    //public StringProperty numStaffProperty() { return numStaff;}




}
