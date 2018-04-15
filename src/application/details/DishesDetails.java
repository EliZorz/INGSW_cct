package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DishesDetails {

    private StringProperty number;
    private StringProperty entree;
    private StringProperty mainCourse;
    private StringProperty dessert;
    private StringProperty drink;

    public DishesDetails(DishesDbDetails ddd){
        this.dessert = new SimpleStringProperty(ddd.getDessert());
        this.drink = new SimpleStringProperty(ddd.getDrink());
        this.entree = new SimpleStringProperty(ddd.getEntree());
        this.number = new SimpleStringProperty(ddd.getNumber());
        this.mainCourse = new SimpleStringProperty(ddd.getMainCourse());

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


}
