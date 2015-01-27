/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fggame.sumV1Ki;

import fggame.Field;
import fggame.GameException;
import fggame.GameIntelligence;
import fggame.GameIntelligence.State;
import fggame.KiIntelligence;
import java.io.Serializable;

/**
 *
 * @author Jotschi
 */
public class SumV1KiIntelligence implements Serializable, KiIntelligence {

    private GameIntelligence game;
    private State pl;   // player
    private State op;   // opponent
    private int dim;
    private State map[][];   // Aktueller Spielzustand
    private KiParams params;

    public SumV1KiIntelligence(KiParams params) {
        this.params = params;
    }

    public void init(GameIntelligence game, State pl) {
        this.game = game;
        this.dim = game.getDimension();
        this.map = game.getMap();
        this.pl = pl;
        if (pl == GameIntelligence.State.PLAYER_1) {
            this.op = GameIntelligence.State.PLAYER_2;
        } else {
            this.op = GameIntelligence.State.PLAYER_1;
        }
    }

    @Override
    public KiIntelligence clone() {
        return new SumV1KiIntelligence(params);
    }

    public Field getNextDraw() {
        long pf[][] = new long[dim][dim]; //Pointsfield
        long max_points = Long.MIN_VALUE;
        int sel_point = 0, akt_point;
        int akt_x, akt_y;

        // Für jedes freie Feld Overall Rating berechnen
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (map[i][j].equals(GameIntelligence.State.NONE)) {
                    pf[i][j] = getOverallRating(i, j);
                } else {
                    pf[i][j] = Long.MIN_VALUE;
                }
            }
        }

        // max_points Wert feststellen
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (pf[i][j] > max_points) {
                    max_points = pf[i][j];
                }
            }
        }

        // Höchstes Feld auswählen
        akt_x = -1;
        akt_y = -1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (pf[i][j] == max_points) {
                    akt_point = (int) (Math.random() * 100) + 1;
                    if (sel_point < akt_point) {
                        sel_point = akt_point;
                        akt_x = i;
                        akt_y = j;
                    }
                }
            }
        }
        if (akt_x == -1 || akt_y == -1) {
            throw new GameException("Ki could not find a valid Field!");
        }
        return new Field(akt_x, akt_y);
    }

    // Alle Feld Ratings zusammengenommen für die Annahme das Feld x, y gesetzt wird.
    private long getOverallRating(int x, int y) {
        State oldstate = map[x][y]; // Alten Status merken
        // Annahme treffen (ändert map)
        map[x][y] = pl;
        // Annahme führt direkt zum Sieg
        if (fiveInRow(x, y)) {
            map[x][y] = oldstate;
            return Long.MAX_VALUE;
        }
        // Wert berechnen
        int sum = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (map[i][j].equals(GameIntelligence.State.NONE)) {
                    sum += getRating(i, j);
                }
            }
        }
        // Alten Status wiederherstellen
        map[x][y] = oldstate;
        return sum;
    }

    // Rating für ein einzelnes Feld
    private long getRating(int x, int y) {
        long p = 0;
        int a;

        // West-Ost
        a = numberOfBordering(x, y, 1, 0, pl) + numberOfBordering(x, y, -1, 0, pl);
        p = p + params.kiValueSelf(a);

        a = numberOfBordering(x, y, 1, 0, op) + numberOfBordering(x, y, -1, 0, op);
        p = p + params.kiValueOp(a);

        // Süd-Nord
        a = numberOfBordering(x, y, 0, 1, pl) + numberOfBordering(x, y, 0, -1, pl);
        p = p + params.kiValueSelf(a);

        a = numberOfBordering(x, y, 0, 1, op) + numberOfBordering(x, y, 0, -1, op);
        p = p + params.kiValueOp(a);

        // Nordwest-Südost
        a = numberOfBordering(x, y, 1, 1, pl) + numberOfBordering(x, y, -1, -1, pl);
        p = p + params.kiValueSelf(a);

        a = numberOfBordering(x, y, 1, 1, op) + numberOfBordering(x, y, -1, -1, op);
        p = p + params.kiValueOp(a);

        // Südwest-Nordost
        a = numberOfBordering(x, y, -1, +1, pl) + numberOfBordering(x, y, +1, -1, pl);
        p = p + params.kiValueSelf(a);

        a = numberOfBordering(x, y, -1, +1, op) + numberOfBordering(x, y, +1, -1, op);
        p = p + params.kiValueOp(a);

        return p;
    }

    /* Gibt die Anzahl der gleichfarbigen in einer Reihe direkt hintereinander angrenzenden Punkte an
     * x, y => Ausgangspunkt           
     * dir_x, dir_y => Richtung (+1, -1, 0)    
     * player => betreffenderSpieler
     * Richtungen: 1, 0 => Rechts
     *             0, 1 => Unten  usw.
     */
    private int numberOfBordering(int x, int y, int dir_x, int dir_y, State player) {
        int anz = 0;
        
        if(!fiveInRowPossible(x, y, dir_x, dir_y, player)) {
            return anz;
        }        
        
        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j] == player) {
                anz++;
            } else {
                return anz;
            }
        }
        return anz;
    }

    // Prüft ob von x,y aus 5 in einer Reihe sind (in beliebige Richtung)
    private boolean fiveInRow(int x, int y) {
        int n, o, s, w;

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
            return true;
        }
        return false;
    }

    // Prüft ob auf der Linie 5 in einer Reihe noch möglich sind
    private boolean fiveInRowPossible(int x, int y, int dir_x, int dir_y, State player) {
        int anz = 1;
        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j].equals(player) || map[i][j].equals(GameIntelligence.State.NONE)) {
                anz++;
            } else {
                break;
            }
        }
        for (int i = x - dir_x, j = y - dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i - dir_x, j = j - dir_y) {
            if (map[i][j].equals(player) || map[i][j].equals(GameIntelligence.State.NONE)) {
                anz++;
            } else {
                break;
            }
        }
        if (anz < 5) {
            return false;
        }
        return true;
    }
}
