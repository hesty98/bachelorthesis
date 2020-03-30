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
}
