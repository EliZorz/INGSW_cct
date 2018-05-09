package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 27/04/2018.
 */
public class NumGitaDbDetails implements Serializable {
    private String numGita;

    public NumGitaDbDetails (NumGitaGuiDetails codguiSp) {
        this.numGita = codguiSp.getNumGita();
    }

    public NumGitaDbDetails (String number) {
        this.numGita = number;
    }

    public String getNumGita() { return numGita; }

    public void setNumGita(String value) { this.numGita = value; }
}
