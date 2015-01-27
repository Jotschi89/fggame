/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fggame;

import fggame.GameIntelligence.State;

/**
 *
 * @author Jotschi
 */
public interface KiIntelligence {
    /**
     * Berechnet den nächsten Zug und gibt ihn als Field Objekt zurück.
     * @return Nächster Zug der KI.
     */
    public Field getNextDraw();
    /**
     * Initialisiert die KI auf ein GameIntelligence Objekt.
     * Notwendig vor dem ersten Aufruf von getNextDraw().
     * @param game Zugehöriges GameIntelligence Objekt
     * @param pl Weißt der KI diesen Spieler zu (darf nicht NONE sein)
     */
    public void init(GameIntelligence game, State pl);

    /**
     * Liefert eine nicht initialisierte Kopie des KiIntelligence Objekts.
     * @return Kopie
     */
    public KiIntelligence clone();
}
