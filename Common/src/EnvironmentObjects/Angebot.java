package EnvironmentObjects;

import java.io.Serializable;

/**
 *
 */
public class Angebot implements Serializable {
    private double pricePerUnit;

    public Angebot(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
