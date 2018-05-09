package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SpecialMenuGuiDetails {

    private StringProperty date;
    private StringProperty entree;
    private StringProperty mainCourse;
    private StringProperty sideDish;
    private StringProperty dessert;
    private StringProperty drink;
    private StringProperty FC;
    private StringProperty allergies;

    public SpecialMenuGuiDetails(SpecialMenuDbDetails special){
        this.date = new SimpleStringProperty(special.getDate());
        this.allergies = new SimpleStringProperty(special.getAllergies());
        this.entree = new SimpleStringProperty(special.getEntree());
        this.mainCourse = new SimpleStringProperty(special.getMain());
        this.sideDish = new SimpleStringProperty(special.getSide());
        this.dessert = new SimpleStringProperty(special.getDessert());
        this.drink = new SimpleStringProperty(special.getDrink());
        this.FC = new SimpleStringProperty(special.getFC());

    }

    public String getDate(){return date.get();}

    public String getEntree(){return entree.get();}

    public String getMain(){return mainCourse.get();}

    public String getSide(){return sideDish.get();}

    public String getDessert(){return dessert.get();}

    public String getDrink(){return drink.get();}

    public String getFC(){return FC.get();}

    public String getAllergies(){return allergies.get();}

    public void setDate(String date){this.date.set(date);}

    public void setEntree(String entree) {
        this.entree.set(entree);
    }

    public void setMainCourse(String mainCourse) {
        this.mainCourse.set(mainCourse);
    }

    public void setDessert(String dessert) {
        this.dessert.set(dessert);
    }

    public void setDrink(String drink) {
        this.drink.set(drink);
    }

    public void setAllergies(String allergies) {
        this.allergies.set(allergies);
    }

    public void setFC(String FC) {
        this.FC.set(FC);
    }

    public void setSideDish(String sideDish) {
        this.sideDish.set(sideDish);
    }

    public StringProperty sideDishProperty() {
        return sideDish;
    }

    public StringProperty mainCourseProperty() {
        return mainCourse;
    }

    public StringProperty entreeProperty() {
        return entree;
    }

    public StringProperty drinkProperty() {
        return drink;
    }

    public StringProperty dessertProperty() {
        return dessert;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty FCProperty() {
        return FC;
    }

    public StringProperty allergiesProperty() {
        return allergies;
    }
}