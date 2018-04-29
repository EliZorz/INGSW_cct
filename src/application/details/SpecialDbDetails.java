package application.details;

public class SpecialDbDetails {

    private String refCode;
    private String allergies;

    public SpecialDbDetails(SpecialGuiDetails special){
        this.allergies = special.getAllergies();
        this.refCode = special.getRefCode();
    }

    public SpecialDbDetails(String refCode,String allergies){
        this.refCode = refCode;
        this.allergies = allergies;
    }

    public String getRefCode(){
        return getRefCode();
    }

    public String getAllergies() {
        return allergies;
    }

    public void setRefCode(String refCode){this.refCode = refCode;}

    public void setAllergies(String allergies){this.allergies = allergies;}

}
