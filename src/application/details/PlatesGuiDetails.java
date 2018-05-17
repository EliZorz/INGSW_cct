package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlatesGuiDetails {
    private StringProperty nomePiatto;
    private StringProperty ingredient;
    public PlatesGuiDetails(PlatesDbDetails plates){
        this.nomePiatto = new SimpleStringProperty(plates.getNomePiatto());
        this.ingredient = new SimpleStringProperty(plates.getIngredient());
    }
    public String getNomePiatto(){return nomePiatto.get();}
    public String getIngredient(){return ingredient.get();}
    public void setNomePiatto(String nome){nomePiatto.set(nome);}
    public void setIngredient(String ingredient){this.ingredient.set(ingredient);}

    public StringProperty nomePiattoProperty() {
        return nomePiatto;
    }

    public StringProperty ingredientProperty() {
        return ingredient;
    }
}