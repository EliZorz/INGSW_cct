package application.details;

import java.io.Serializable;

/**
 * Created by ELISA on 07/05/2018.
 */
public class BusPlateCapacityDbDetails implements Serializable {
    private String plate;
    public String capacity;

    public BusPlateCapacityDbDetails (BusPlateCapacityGuiDetails bus) {
        this.plate = bus.getPlate();
        this.capacity = bus.getCapacity();
    }

    public BusPlateCapacityDbDetails (String plate, String capacity) {
        this.plate = plate;
        this.capacity  =capacity;
    }

    public String getPlate() {
        return plate;
    }
    public String getCapacity() {
        return capacity;
    }

    public void setPlate(String value) { this.plate = value; }
    public void setCapacity(String value) { this.capacity = value; }
}

