/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fggame;

import java.io.Serializable;

/**
 * Kapselt die x und y Koordination.
 * @author Jotschi
 */
public class Field implements Serializable{

    private int x;
    private int y;

    /**
     * Erzeugt ein neues Field-Objekt. Wirft eine GameException wenn eine Koordinate kleiner als 0 ist. 
     * @param x  X-Koordinate
     * @param y  Y-Koordinate
     */
    public Field(int x, int y) {
        if (x < 0 || y < 0) {
            throw new GameException("Field x:" + x + " y:" + y + " --> Wrong coordinates! (Must be at least 0)");
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Erzeugt ein Field Objekt mit x und y Koordinate aus einer (eindimensionalen) Feldnummer n.
     * Wirft eine GameException wenn n kleiner als 0.
     * @param n  Nummer des Feldes
     * @param dim  Größe des Spielfelds
     * @param b  Anstonsten nutzloser boolean zur Unterscheidung der Konstruktoren
     */
    public Field(int n, int dim, boolean b) {
        this.y = n / dim;
        this.x = n % dim;
        if (x < 0 || y < 0) {
            throw new GameException("Field x:" + x + " y:" + y + " --> Wrong coordinates! (Must be at least 0)");
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Rechnet die Koordinaten von Field f in den Index eines eindimensionalen
     * Arrays um.
     * @param dim Spielfeldgröße
     * @return Index der den Koordinaten von Field entspricht.
     */
    public int getNr(int dim) {
        int n = this.getY() * dim + this.getX();
        return n;
    }

    @Override
    public String toString() {
        return "(x=" + x + "  y=" + y + ")";
    }
}
