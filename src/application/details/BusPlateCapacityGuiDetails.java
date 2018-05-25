package application.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by ELISA on 07/05/2018.
 */
public class BusPlateCapacityGuiDetails {
    private StringProperty plate;
    private StringProperty capacity;

    public BusPlateCapacityGuiDetails (BusPlateCapacityDbDetails bus){
        this.plate = new SimpleStringProperty(bus.getPlate());
        this.capacity = new SimpleStringProperty(bus.getCapacity());
    }

    public String getPlate(){ return plate.getValue(); }
    public String getCapacity() { return capacity.getValue(); }

    public void setPlate(String value) { plate.setValue(value); }
    public void setCapacity(String value) { capacity.setValue(value); }

    public StringProperty plateProperty(){ return plate; }
    public StringProperty capacityProperty() { return capacity; }
}
