package application.details;

public class SpecialMenuDbDetails {

    private String date;
    private String entree;
    private String side;
    private String dessert;
    private String drink;
    private String main;
    private String FC;
    private String allergies;

    public SpecialMenuDbDetails(SpecialMenuGuiDetails special){
        this.allergies = special.getAllergies();
    }
}
