package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 17/04/2018.
 */
public class CodRifChildDbDetails implements Serializable{
    private String codRif;

    public CodRifChildDbDetails (IngredientsGuiDetails codguiSp) {
        this.codRif = codguiSp.getIngr();
    }

    public CodRifChildDbDetails (String ingredient) {
        this.codRif = ingredient;
    }

    public String getCodRif() { return codRif; }

    public void setCodRif(String value) { this.codRif = value; }
}
