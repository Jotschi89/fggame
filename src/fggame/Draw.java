/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fggame;

import fggame.GameIntelligence.State;
import java.io.Serializable;

/**
 * Diese Klasse kapselt einen Zug. Enthält daher ein Feld, dass die Position angibt, 
 * und den Spieler der den Zug macht (gespeichert als State-Objekt)
 * @author Jotschi
 */
public class Draw implements Serializable {
    private Field f;
    private State player;

    /**
     * Erzeugt ein neues Draw Objekt
     * @param f - Feld des entsprechenden Zuges
     * @param player - Spieler der den Zug gemacht hat
     */
    public Draw(Field f, State player) {
        this.f = f;
        this.player = player;
    }

    public State getPlayer() {
        return player;
    }

    public Field getField() {
        return f;
    }

    @Override
    public String toString() {
        return "Draw: Player = " + player + " Cords: " + f.toString();
    }
}
