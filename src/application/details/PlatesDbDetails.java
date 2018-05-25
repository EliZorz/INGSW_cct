package application.details;

import java.io.Serializable;

public class PlatesDbDetails implements Serializable {
    private String nomePiatto;
    private String ingredient;

    public PlatesDbDetails(PlatesGuiDetails plates){
        this.nomePiatto = plates.getNomePiatto();
        this.ingredient = plates.getIngredient();
    }
    public PlatesDbDetails(String nome, String ingredient){
        this.nomePiatto = nome;
        this.ingredient = ingredient;
    }
    public String getNomePiatto(){return  nomePiatto; }
    public String getIngredient(){return ingredient; }
    public void setNomePiatto(String nomePiatto){this.nomePiatto = nomePiatto;}
    public void setIngredient(String ingredient){this.ingredient = ingredient; }
}
