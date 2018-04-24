package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 23/04/2018.
 */
public class TripTableDbDetails implements Serializable {

    private String layover;
    private String datetimeDep;
    private String datetimeArr;
    private String datetimeCom;
    //private String numTrip;
    private String accomodation;
    //private String numChildren;
    //private String numStaff;

    public TripTableDbDetails(TripTableGuiDetails trip){
        this.layover = trip.getLayover();
        this.datetimeDep = trip.getDep();
        this.datetimeArr = trip.getArr();
        this.datetimeCom = trip.getCom();
        //this.numTrip = trip.getNumTrip();
        this.accomodation = trip.getAccomodation();
        //this.numChildren = trip.getNumChildren();
        //this.numStaff = trip.getNumStaff();

    }

    public TripTableDbDetails(String layover, String datetimeDep, String datetimeArr, String datetimeCom, /*String numTrip,*/ String accomodation/*, String numChildren, String numStaff*/){

        this.layover = layover;
        this.datetimeDep = datetimeDep;
        this.datetimeArr = datetimeArr;
        this.datetimeCom = datetimeCom;
        //this.numTrip = numTrip;
        this.accomodation = accomodation;
        //this.numChildren = numChildren;
        //this.numStaff = numStaff;

    }

    public String getLayover() { return layover;}
    public String getDep(){ return datetimeDep;}
    public String getArr(){ return datetimeArr;}
    public String getCom(){ return datetimeCom;}
    //public String getNumTrip(){ return numTrip; }
    public String getAccomodation(){ return accomodation;}
    //public String getNumChildren(){ return numChildren;}
    //public String getNumStaff() {return numStaff;}


    public void setLayover(String value){this.layover = value;}
    public void setDep(String value){this.datetimeDep = value;}
    public void setArr(String value){this.datetimeArr = value;}
    public void setCom(String value){this.datetimeCom = value;}
    //public void setNumTrip(String value){this.numTrip = value;}
    public void setAccomodation(String value){this.accomodation = value;}
    //public void setNumChildren(String value){this.layover = value;}
    //public void setNumStaff(String value){this.numStaff = value;}

}
