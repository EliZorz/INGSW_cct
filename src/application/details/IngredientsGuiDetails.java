package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 16/04/2018.
 */
public class IngredientsGuiDetails {

    private StringProperty ingredient;

    public IngredientsGuiDetails (IngredientsDbDetails ingrDb){
        this.ingredient = new SimpleStringProperty(ingrDb.getIngr());
    }

    public String getIngr(){
        return ingredient.get();
    }

    public void setIngr(String value) { this.ingredient.set(value); }

    public StringProperty ingredientProperty(){
        return ingredient;
    }

}

