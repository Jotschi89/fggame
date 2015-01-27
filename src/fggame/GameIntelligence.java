/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fggame;

import fggame.defaultKi.DefaultKiIntelligence;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Game Objekt mit dessen Hilfe ein Spiel gespielt werden kann.
 * @author Jotschi
 */
public class GameIntelligence implements Serializable {

    /**
     * Enum der möglichen Stati eines Feldes am Spielfeld, bzw der möglichen Sieger.
     */
    public static enum State {

        NONE, PLAYER_1, PLAYER_2
    };
    private int dim;
    private KiIntelligence ki1, ki2;
    private State winner;
    private List<Field> winningFields;
    private State map[][];
    private List<Draw> draw;

    /**
     * Legt ein Spiel ohne Computergegner an (Player vs. Player).
     * @param dimension - Spielfeldgröße
     */
    public GameIntelligence(int dimension) {
        if (dimension < 5) {
            this.dim = 10;
        } else {
            this.dim = dimension;
        }
        this.map = new State[dimension][dimension];
        for (int j = 0; j < dimension; j++) {
            for (int i = 0; i < dimension; i++) {
                map[j][i] = State.NONE;
            }
        }
        this.winner = null;
        this.draw = new ArrayList<Draw>();
    }

    /**
     * Legt ein Spiel mit (bis zu) zwei Computergegnern an.
     * @param dimension - Spielfeldgröße
     * @param ki1 - KiIntelligence für alle ungeraden Züge.
     * @param ki2 - KiIntelligence für alle geraden Züge.
     */
    public GameIntelligence(int dimension, KiIntelligence ki1, KiIntelligence ki2) {
        this(dimension);
        ki1.init(this, State.PLAYER_1);
        ki2.init(this, State.PLAYER_2);
        this.ki1 = ki1;
        this.ki2 = ki2;
    }

    /**
     * Berechnet den nächsten Zug mittels der Eingestellten KI:
     *      - Ungerader Zug: ki1
     *      - Gerader Zug: ki2
     * Es beginnt daher immer ki1.
     * Wirft eine GameException fals das Spiel bereits beendet ist, oder die benötigte ki nicht vorhanden ist.
     * @return Zug mit Koordinaten und Spieler.
     */
    public Draw nextDraw() {
        if (isFinished()) {
            throw new GameException("Game is finished! No draw possible.");
        }
        Draw d;
        // Welche Ki?
        if (getAtDraw().equals(State.PLAYER_1)) {
            if (ki1 == null) {
                throw new GameException("Game has no ki1!");
            }
            d = new Draw(ki1.getNextDraw(), State.PLAYER_1);
        } else {
            if (ki2 == null) {
                throw new GameException("Game has no ki2!");
            }
            d = new Draw(ki2.getNextDraw(), State.PLAYER_2);
        }

        addDraw(d);
        return getDraw();
    }

    /**
     * Führt einen eigenen Zug auf Feld f aus. Es ist jederzeit möglich einen 
     * eigenen Zug zu machen, selbst wenn eigentlich eine Ki einstellt wäre. Die Player1 
     * immer beginnt gehören alle geraden Züge Player1 und alle ungeraden Player2.
     * Wirft eine GameException falls das Spiel bereits beendet ist.
     * @param f - Feld mit den Koordinaten des durchzuführenden Zuges.
     * @return Zug mit Koordinaten und Spieler.
     */
    public Draw nextDraw(Field f) {
        int x = f.getX();
        int y = f.getY();
        if (isFinished()) {
            throw new GameException("Game is finished! No draw possible.");
        }
        if (!map[x][y].equals(State.NONE)) {
            throw new GameException("Field is not empty! (x=" + x + " y=" + y + ")");
        }
        Draw d = new Draw(f, getAtDraw());

        addDraw(d);
        return getDraw();
    }

    private void addDraw(Draw d) {
        draw.add(d);
        map[d.getField().getX()][d.getField().getY()] = d.getPlayer();
        if (isFull()) {
            winner = State.NONE;
        }
        if (isFinished(d.getField())) {
            winner = d.getPlayer();
        }
    }

    /**
     *
     * @return Player der als nächstes dran ist.
     */
    private State getAtDraw() {
        if (getDraw() == null) {
            return State.PLAYER_1;
        }
        if (getDraw().getPlayer().equals(State.PLAYER_1)) {
            return State.PLAYER_2;
        } else {
            return State.PLAYER_1;
        }
    }

    private boolean isFull() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (map[i][j].equals(State.NONE)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Bekommt letze Änderung der map übergeben und übrpüft ob das Spiel vorbei
     * ist (5 in einer Reihe sind) und setzt die winningFields */
    private boolean isFinished(Field f) {
        int x = f.getX();
        int y = f.getY();
        int n, o, s, w;
        State pl = map[x][y];

        //Ost-West Richtung
        w = x;
        for (int i = x; i >= 0; i--) {  //w
            if (map[i][y].equals(pl)) {
                w = i;
            } else {
                break;
            }
        }
        o = x;
        for (int i = x; i < dim; i++) {  //o
            if (map[i][y].equals(pl)) {
                o = i;
            } else {
                break;
            }
        }
        if (o - w >= 4) {
            winningFields = new ArrayList<Field>();
            winningFields.add(new Field(w, y));
            winningFields.add(new Field(w + 1, y));
            winningFields.add(new Field(w + 2, y));
            winningFields.add(new Field(w + 3, y));
            winningFields.add(new Field(w + 4, y));
            return true;
        }


        //Nord Süd Richtung
        n = y;
        for (int i = y; i >= 0; i--) {  //s
            if (map[x][i].equals(pl)) {
                n = i;
            } else {
                break;
            }
        }
        s = y;
        for (int i = y; i < dim; i++) {  //n
            if (map[x][i].equals(pl)) {
                s = i;
            } else {
                break;
            }
        }
        if (s - n >= 4) {
            winningFields = new ArrayList<Field>();
            winningFields.add(new Field(x, n));
            winningFields.add(new Field(x, n + 1));
            winningFields.add(new Field(x, n + 2));
            winningFields.add(new Field(x, n + 3));
            winningFields.add(new Field(x, n + 4));
            return true;
        }

        //Süd-West Nord-Ost Richtung
        s = y;
        w = x;
        int j = y;
        for (int i = x; i >= 0; i--) {  //sw
            if (j >= dim) {
                break;
            }
            if (map[i][j].equals(pl)) {
                s = j;
                w = i;
            } else {
                break;
            }
            j++;
        }

        n = y;
        o = x;
        j = y;
        for (int i = x; i < dim; i++) {  //no
            if (j < 0) {
                break;
            }
            if (map[i][j].equals(pl)) {
                n = j;
                o = i;
            } else {
                break;
            }
            j--;
        }
        if (s - n >= 4 && o - w >= 4) {
            winningFields = new ArrayList<Field>();
            winningFields.add(new Field(w, s));
            winningFields.add(new Field(w + 1, s - 1));
            winningFields.add(new Field(w + 2, s - 2));
            winningFields.add(new Field(w + 3, s - 3));
            winningFields.add(new Field(w + 4, s - 4));
            return true;
        }

        //Nord-West Süd-Ost Richtung
        n = y;
        w = x;
        j = y;
        for (int i = x; i >= 0; i--) {  //nw
            if (j < 0) {
                break;
            }
            if (map[i][j] == pl) {
                n = j;
                w = i;
            } else {
                break;
            }
            j--;
        }

        s = y;
        o = x;
        j = y;
        for (int i = x; i < dim; i++) {  //so
            if (j >= dim) {
                break;
            }
            if (map[i][j] == pl) {
                s = j;
                o = i;
            } else {
                break;
            }
            j++;
        }
        if (s - n >= 4 && o - w >= 4) {
            winningFields = new ArrayList<Field>();
            winningFields.add(new Field(w, n));
            winningFields.add(new Field(w + 1, n + 1));
            winningFields.add(new Field(w + 2, n + 2));
            winningFields.add(new Field(w + 3, n + 3));
            winningFields.add(new Field(w + 4, n + 4));
            return true;
        }
        return false;
    }

    public int getDimension() {
        return dim;
    }

    /**
     *
     * @return True wenn das Spiel beendet ist (Keine weiteren Züge mehr möglich!)
     */
    public boolean isFinished() {
        if (winner == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return Gibt den Gewinner zurück. Bei Unentschieden 'none'. null solange das Spiel noch läuft.
     */
    public State getWinner() {
        return winner;
    }

    /* *
     * Liste der Felder die den Sieg ausgelöste haben.
     * Null bei unenschieden oder nicht beendetem Spiel.
     */
    public List<Field> getWinningFields() {
        return winningFields;
    }

    /**
     * Returnt letzen Draw. Null wenn kein einziger Draw vorhanden.
     * @return Draw
     */
    public Draw getDraw() {
        int s = draw.size();
        if (s == 0) {
            return null;
        }
        return draw.get(s - 1);
    }

    /**
     * Returnt Draw mit der übergebenen Nummer.
     * @param nr - Nummer des Zuges.
     * @return Draw
     */
    public Draw getDraw(int nr) {
        return draw.get(nr);
    }

    /**
     * Returnt chronologisch geordnete Liste aller Züge.
     * @return List<Draw>
     */
    public List<Draw> getDraws() {
        return draw;
    }

    /**
     * Gibt die Anzahl aller bisher gespielter Züge zurück.
     * @return Anzahl der Züge
     */
    public int getNumberDraws() {
        return draw.size();
    }

    public KiIntelligence getKi1() {
        return ki1;
    }

    public KiIntelligence getKi2() {
        return ki2;
    }

    /**
     * Spielt das Spiel bis zum Ende und liefert dann den Sieger.
     * @return Sieger des Spieles
     */
    public State getResult() {
        while (!this.isFinished()) {
            this.nextDraw();
        }
        return this.getWinner();
    }

    /**
     *
     * @return Map vom Spielfeld mit der Beledung (PLAYER_1, PLAYER_2, NONE)
     */
    public State[][] getMap() {
        return map;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Game: Dimension=" + dim + "  Draw NR:" + getNumberDraws());
        if (this.getDraw() != null) {
            s.append("\n").append(this.getDraw().toString());
        }
        for (int i = 0; i < dim; i++) {
            s.append("\n");
            for (int j = 0; j < dim; j++) {
                if (map[j][i].equals(State.NONE)) {
                    s.append("_ ");
                }
                if (map[j][i].equals(State.PLAYER_1)) {
                    s.append("X ");
                }
                if (map[j][i].equals(State.PLAYER_2)) {
                    s.append("O ");
                }
            }
        }
        return s.toString();
    }
}
