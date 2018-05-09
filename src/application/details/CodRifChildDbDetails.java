package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 17/04/2018.
 */
public class CodRifChildDbDetails implements Serializable{
    private String codRif;

    public CodRifChildDbDetails (CodRifChildGuiDetails codguiSp) {this.codRif = codguiSp.getCodRif();}

    public CodRifChildDbDetails (String code) {
        this.codRif = code;
    }

    public String getCodRif() { return codRif; }

    public void setCodRif(String value) { this.codRif = value; }
}
