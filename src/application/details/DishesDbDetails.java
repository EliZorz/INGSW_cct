package application.details;

import java.io.Serializable;
import java.util.ArrayList;

public class DishesDbDetails extends ArrayList<DishesDbDetails> implements Serializable {

    private String number;
    private String entree;
    private String mainCourse;
    private String dessert;
    private String sideDish;
    private String drink;
    private String day;

    public DishesDbDetails(DishesDetails dish){
        this.entree = dish.getEntree();
        this.dessert = dish.getDessert();
        this.drink = dish.getDrink();
        this.sideDish = dish.getSideDish();
        this.mainCourse = dish.getMainCourse();
        this.number = dish.getNumber();
        this.day = dish.getDay();

    }

    public DishesDbDetails(String number, String entree, String mainCourse, String dessert, String sideDish,String drink,String day){
        this.number = number;
        this.mainCourse = mainCourse;
        this.drink = drink;
        this.dessert = dessert;
        this.entree = entree;
        this.day = day;
        this.sideDish = sideDish;
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

    public String getDay() {
        return day;
    }

    public String getSideDish() {
        return sideDish;
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

    public void setDay(String day){this.day = day;}

    public void setSideDish(String sideDish){this.sideDish = sideDish;}



}