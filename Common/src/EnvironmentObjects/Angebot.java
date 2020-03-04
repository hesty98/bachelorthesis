package EnvironmentObjects;

import java.io.Serializable;

/**
 *
 */
public class Angebot implements Serializable {
    private double price;

    public Angebot(double price) {
        this.price = price;
    }
}
