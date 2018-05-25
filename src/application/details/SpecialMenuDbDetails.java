package application.details;

import java.io.Serializable;

public class SpecialMenuDbDetails implements Serializable {

    private String date;
    private String entree;
    private String side;
    private String dessert;
    private String drink;
    private String main;
    private String FC;
    private String allergies;

    public SpecialMenuDbDetails(SpecialMenuGuiDetails special){
        this.allergies = special.getAllergies();
        this.date = special.getDate();
        this.entree = special.getEntree();
        this.side = special.getSide();
        this.dessert = special.getDessert();
        this.drink = special.getDrink();
        this.main = special.getMain();
        this.FC = special.getFC();
    }

    public SpecialMenuDbDetails(String date, String entree, String side, String dessert, String drink, String main, String FC, String allergies){
        this.date = date;
        this.entree = entree;
        this.allergies = allergies;
        this.drink = drink;
        this.dessert = dessert;
        this.FC = FC;
        this.main = main;
        this.side = side;
    }

    public String getDate(){return date; }

    public String getSide(){return side; }

    public String getDrink() {
        return drink;
    }

    public String getEntree() {
        return entree;
    }

    public String getDessert() {
        return dessert;
    }

    public String getMain(){return main;}

    public String getFC(){return FC; }

    public String getAllergies(){return allergies;}

    public void setDate(String date){this.date = date;}

    public void setFC(String FC) {
        this.FC = FC;
    }

    public void setDessert(String dessert) {
        this.dessert = dessert;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public void setEntree(String entree) {
        this.entree = entree;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setSide(String side) {
        this.side = side;
    }
}

