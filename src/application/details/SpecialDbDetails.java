package application.details;


import java.io.Serializable;

public class SpecialDbDetails implements Serializable {

   private String CF;
   private String allergie;

   public SpecialDbDetails(String CF, String allergie){
       this.CF = CF;
       this.allergie = allergie;
   }

   public SpecialDbDetails(SpecialGuiDetails special){
       this.CF = special.getCF();
       this.allergie = special.getAllergie();
   }

   public String getCF(){return CF; }

   public String getAllergie(){return allergie;}

   public void setCF(String CF){this.CF = CF;}

   public void setAllergie(String allergie){this.allergie = allergie;}
}
