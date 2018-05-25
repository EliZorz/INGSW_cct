package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class DishesDetails {

    private StringProperty number;
    private StringProperty entree;
    private StringProperty mainCourse;
    private StringProperty dessert;
    private StringProperty sideDish;
    private StringProperty drink;
    private StringProperty day;


    public DishesDetails(DishesDbDetails dishes){
        this.dessert = new SimpleStringProperty(dishes.getDessert());
        this.drink = new SimpleStringProperty(dishes.getDrink());
        this.entree = new SimpleStringProperty(dishes.getEntree());
        this.number = new SimpleStringProperty(dishes.getNumber());
        this.mainCourse = new SimpleStringProperty(dishes.getMainCourse());
        this.day = new SimpleStringProperty(dishes.getDay());
        this.sideDish = new SimpleStringProperty(dishes.getSideDish());

    }

    public String getDessert() {
        return dessert.get();
    }

    public String getDrink() {
        return drink.get();
    }

    public String getEntree() {
        return entree.get();
    }

    public String getMainCourse() {
        return mainCourse.get();
    }

    public String getNumber() {
        return number.get();
    }

    public String getSideDish() {
        return sideDish.get();
    }

    public String getDay() { return day.get(); }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public void setMainCourse(String mainCourse) {
        this.mainCourse.set(mainCourse);
    }

    public void setEntree(String entree) {
        this.entree.set(entree);
    }

    public void setDrink(String drink) {
        this.drink.set(drink);
    }

    public void setDessert(String dessert) {
        this.dessert.set(dessert);
    }

    public void setSideDish(String sideDish){this.sideDish.set(sideDish);}

    public StringProperty dessertProperty() {
        return dessert;
    }

    public StringProperty drinkProperty() {
        return drink;
    }

    public StringProperty entreeProperty() {
        return entree;
    }

    public StringProperty mainCourseProperty() {
        return mainCourse;
    }

    public StringProperty numberProperty() {
        return number;
    }

    public StringProperty dayProperty() { return day;}

    public StringProperty sideDishProperty(){return sideDish;}
}
