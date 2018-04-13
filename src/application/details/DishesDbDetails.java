package application.details;

import java.io.Serializable;
import java.util.ArrayList;

public class DishesDbDetails extends ArrayList<DishesDbDetails> implements Serializable {

    private String entree;
    private String drink;
    private String dessert;
    private String mainCourse;
    private String number;

    public DishesDbDetails(DishesDetails dd){
        this.entree = dd.getEntree();
        this.dessert = dd.getDessert();
        this.drink = dd.getDrink();
        this.mainCourse = dd.getMainCourse();
        this.number = dd.getNumber();

    }

    public DishesDbDetails(String entree, String drink, String dessert, String mainCourse, String number){
        this.number = number;
        this.mainCourse = mainCourse;
        this.drink = drink;
        this.dessert = dessert;
        this.entree = entree;
    }



    public String getDessert() {
        return dessert;
    }

    public String getEntree() {
        return entree;
    }

    public String getDrink() {
        return drink;
    }

    public String getMainCourse() {
        return mainCourse;
    }

    public String getNumber() {
        return number;
    }

    public void setDessert(String dessert) {
        this.dessert = dessert;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public void setEntree(String entree) {
        this.entree = entree;
    }

    public void setMainCourse(String mainCourse) {
        this.mainCourse = mainCourse;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
