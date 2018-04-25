package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 23/04/2018.
 */
public class TripTableGuiDetails {

    private StringProperty datetimeDep;
    private StringProperty datetimeArr;
    private StringProperty datetimeCom;
    //private StringProperty numTrip;
    private StringProperty accomodation;
    private StringProperty depFrom;
    private StringProperty arrivalTo;
    //private StringProperty codRif;

    public TripTableGuiDetails(TripTableDbDetails tripDb) {
        this.datetimeDep = new SimpleStringProperty(tripDb.getDep());
        this.datetimeArr = new SimpleStringProperty(tripDb.getArr());
        this.datetimeCom = new SimpleStringProperty(tripDb.getCom());
        //this.numTrip = new SimpleStringProperty(tripDb.getNumTrip());
        this.accomodation = new SimpleStringProperty(tripDb.getAccomodation());
        this.depFrom = new SimpleStringProperty(tripDb.getDepFrom());
        this.arrivalTo = new SimpleStringProperty(tripDb.getArrTo());
        //this.codRif = new SimpleStringProperty(tripDb.getCodRif());
    }

    public String getDep(){ return datetimeDep.get();}
    public String getArr(){ return datetimeArr.get();}
    public String getCom(){ return datetimeCom.get();}
    //public String getNumTrip(){ return numTrip.get(); }
    public String getAccomodation(){ return accomodation.get();}
    public String getDepFrom(){ return depFrom.get();}
    public String getArrTo() {return arrivalTo.get();}
    //public String getCodRif() {return codRif.get();}

    public void setDep(String value){datetimeDep.set(value);}
    public void setArr(String value){datetimeArr.set(value);}
    public void setCom(String value){datetimeCom.set(value);}
    //public void setNumTrip(String value){numTtrip.set(value);}
    public void setAccomodation(String value){accomodation.set(value);}
    public void setDepFrom(String value){depFrom.set(value);}
    public void setArrTo(String value){arrivalTo.set(value);}
    //public void setCodRif(String value){codRif.set(value);}

    public StringProperty depProperty(){ return datetimeDep;}
    public StringProperty arrProperty() { return datetimeArr;}
    public StringProperty comProperty() { return datetimeCom;}
    //public StringProperty numTripProperty(){ return numTrip;}
    public StringProperty accomodationProperty(){return accomodation;}
    public StringProperty depFromProperty(){ return depFrom;}
    public StringProperty arrToProperty() { return arrivalTo;}
    //public StringProperty codRifProperty(){return codRif;}




}
