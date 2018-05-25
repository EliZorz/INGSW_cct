package application.details;

import java.io.Serializable;

public class IngredientsDbDetails implements Serializable {

    private String ingredient;

    public IngredientsDbDetails (IngredientsGuiDetails ingrguiSp) {
        this.ingredient = ingrguiSp.getIngr();
    }

    public IngredientsDbDetails (String ingredient) {
        this.ingredient = ingredient;
    }

    public String getIngr() { return ingredient; }

    public void setIngr(String value) { this.ingredient = value; }
}
