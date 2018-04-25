package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 23/04/2018.
 */
public class TripTableDbDetails implements Serializable {

    private String datetimeDep;
    private String datetimeArr;
    private String datetimeCom;
    //private String numTrip;
    private String accomodation;
    private String depFrom;
    private String arrivalTo;
    //private String codRif;

    public TripTableDbDetails(TripTableGuiDetails trip){

        this.datetimeDep = trip.getDep();
        this.datetimeArr = trip.getArr();
        this.datetimeCom = trip.getCom();
        //this.numTrip = trip.getNumTrip();
        this.accomodation = trip.getAccomodation();
        this.depFrom = trip.getDepFrom();
        this.arrivalTo = trip.getArrTo();
        //this.codRif = trip.getCodRif();

    }

    public TripTableDbDetails(String datetimeDep, String datetimeArr, String datetimeCom, /*String numTrip,*/ String accomodation, String depFrom, String arrivalTo/*, String codRif*/){

        this.datetimeDep = datetimeDep;
        this.datetimeArr = datetimeArr;
        this.datetimeCom = datetimeCom;
        //this.numTrip = numTrip;
        this.accomodation = accomodation;
        this.depFrom = depFrom;
        this.arrivalTo = arrivalTo;
        //this.codRif = codRif;

    }

    public String getDep(){ return datetimeDep;}
    public String getArr(){ return datetimeArr;}
    public String getCom(){ return datetimeCom;}
    //public String getNumTrip(){ return numTrip; }
    public String getAccomodation(){ return accomodation;}
    public String getDepFrom(){ return depFrom;}
    public String getArrTo() {return arrivalTo;}
    //public String getCodRif() { return codRif;}


    public void setDep(String value){this.datetimeDep = value;}
    public void setArr(String value){this.datetimeArr = value;}
    public void setCom(String value){this.datetimeCom = value;}
    //public void setNumTrip(String value){this.numTrip = value;}
    public void setAccomodation(String value){this.accomodation = value;}
    public void setDepFrom(String value){this.depFrom = value;}
    public void setArrTo(String value){this.arrivalTo = value;}
    //public void setCodRif(String value) {this.codRif = value;}

}
