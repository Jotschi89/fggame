/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fggame.defaultKi;

import fggame.Field;
import fggame.GameException;
import fggame.GameIntelligence;
import fggame.GameIntelligence.State;
import fggame.KiIntelligence;
import fggame.defaultKi.ParaInfo.Typ;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jotschi
 */
public class DefaultKiIntelligence implements Serializable, KiIntelligence {

    private long pf[][];     // Pointsfield
    private KiParams params;
    private FieldInfo info[][];     // Info was die Ki tut
    private List<Field> affected;  // Liste der bei der letzten Aktion betroffnen Felder
    private GameIntelligence game;
    private int dim;
    private boolean enableInfo;
    private int lastdraw;  //Nummer der Runde wann zuletzt dran
    private State pl;      // Welcher Spieler ist die Ki?
    private State op;      // Gegenspieler der Ki
    private State map[][];   // Aktueller Spielzustand

    /**
     *
     * @param params Zugehörige 19 Parameter dieser KI.
     * @param enableInfo Aktiviert Debug Informationen
     */
    public DefaultKiIntelligence(KiParams params, boolean enableInfo) {
        this.params = params;
        this.enableInfo = enableInfo;
    }

    /**
     *
     * @param params Zugehörige 19 Parameter dieser KI.
     */
    public DefaultKiIntelligence(KiParams params) {
        this.params = params;
        this.enableInfo = false;
    }

    public void init(GameIntelligence game, State pl) {
        this.game = game;
        this.dim = game.getDimension();
        this.pf = new long[dim][dim];
        this.map = game.getMap();
        this.pl = pl;
        this.lastdraw = 0;
        if (pl == GameIntelligence.State.PLAYER_1) {
            this.op = GameIntelligence.State.PLAYER_2;
        } else {
            this.op = GameIntelligence.State.PLAYER_1;
        }
        if (enableInfo) {
            this.info = new FieldInfo[dim][dim];
        }
    }

    @Override
    public DefaultKiIntelligence clone() {
        return new DefaultKiIntelligence(params, enableInfo);
    }

    public Field getNextDraw() {
        long max_points = 0;
        int sel_point = 0, akt_point;
        int akt_x, akt_y;
        // Wieviele Runden neu berechnen?
        // Erster Aufruf
        // Pointsfield aktualisieren
        if (lastdraw == 0) {
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (map[i][j].equals(GameIntelligence.State.NONE)) {
                        pf[i][j] = this.getRating(i, j);
                    } else {
                        setPointsNull(i, j);
                    }
                }
            }
        } else {
            // Wiederholter Aufruf
            for (int i = lastdraw; i < game.getNumberDraws(); i++) {
                int x = game.getDraw(i).getField().getX();
                int y = game.getDraw(i).getField().getY();
                // Pointsfield aktualisieren
                setPointsNull(x, y);
                //horinzontal
                updateLine(x, y, 1, 0);
                updateLine(x, y, -1, 0);
                //vertikal
                updateLine(x, y, 0, 1);
                updateLine(x, y, 0, -1);
                //diagonal 1
                updateLine(x, y, 1, 1);
                updateLine(x, y, -1, -1);
                //diagonal 2
                updateLine(x, y, 1, -1);
                updateLine(x, y, -1, 1);
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
                if (pf[i][j] * 100 >= max_points * params.getKi_accurcy_percent() - params.getKi_blur() * 100) {
               //     akt_point = max_points + (long) (Math.random() * 100 * (max_points + 1));  // ??? Wozu so kompleziert ???
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
        this.lastdraw = game.getNumberDraws();
        return new Field(akt_x, akt_y);
    }

    public KiParams getKi() {
        return params;
    }

    // Updatet den Punkte Wert aller Felder von x/y bis 5 Felder in Richtung dir_x/y+dir_y.
    private void updateLine(int x, int y, int dir_x, int dir_y) {
        for (int i = x + dir_x, j = y + dir_y, k = 0; i
                >= 0 && i < dim && j >= 0 && j < dim && k < 5; i = i + dir_x, j = j + dir_y, k++) {
            if (map[i][j].equals(GameIntelligence.State.NONE)) {
                pf[i][j] = this.getRating(i, j);
            } else {
                setPointsNull(i, j);
            }
        }
    }

    private long getRating(int x, int y) {
        long p = 0;
        int offen, a;

        if (!enableInfo) {
            // West-Ost    
            a = numberOfBordering(x, y, 1, 0, pl) + numberOfBordering(x, y, -1, 0, pl);
            offen = openEndsSelf(x, y, 1, 0);
            p = p + params.kiValueSelf(a) * params.kiOpenValueSelf(offen);

            a = numberOfBordering(x, y, 1, 0, op) + numberOfBordering(x, y, -1, 0, op);
            offen = openEndsOp(x, y, 1, 0);
            p = p + params.kiValueOp(a) * params.kiOpenValueOp(offen);

            // Süd-Nord
            a = numberOfBordering(x, y, 0, 1, pl) + numberOfBordering(x, y, 0, -1, pl);
            offen = openEndsSelf(x, y, 0, 1);
            p = p + params.kiValueSelf(a) * params.kiOpenValueSelf(offen);

            a = numberOfBordering(x, y, 0, 1, op) + numberOfBordering(x, y, 0, -1, op);
            offen = openEndsOp(x, y, 0, 1);
            p = p + params.kiValueOp(a) * params.kiOpenValueOp(offen);

            // Nordwest-Südost
            a = numberOfBordering(x, y, 1, 1, pl) + numberOfBordering(x, y, -1, -1, pl);
            offen = openEndsSelf(x, y, 1, 1);
            p = p + params.kiValueSelf(a) * params.kiOpenValueSelf(offen);

            a = numberOfBordering(x, y, 1, 1, op) + numberOfBordering(x, y, -1, -1, op);
            offen = openEndsOp(x, y, 1, 1);
            p = p + params.kiValueOp(a) * params.kiOpenValueOp(offen);


            // Südwest-Nordost
            a = numberOfBordering(x, y, -1, +1, pl) + numberOfBordering(x, y, +1, -1, pl);
            offen = openEndsSelf(x, y, -1, +1);
            p = p + params.kiValueSelf(a) * params.kiOpenValueSelf(offen);

            a = numberOfBordering(x, y, -1, +1, op) + numberOfBordering(x, y, +1, -1, op);
            offen = openEndsOp(x, y, -1, +1);
            p = p + params.kiValueOp(a) * params.kiOpenValueOp(offen);
        }

        //DEBUG
        if (enableInfo) {
            info[x][y] = new FieldInfo();

            // West-Ost
            info[x][y].setSelf_hor(new ParaInfo(params));
            info[x][y].setSelf_hor_mul(new ParaInfo(params));
            this.affected = new ArrayList<Field>();
            a = numberOfBorderingDebug(x, y, 1, 0, pl) + numberOfBorderingDebug(x, y, -1, 0, pl);
            info[x][y].getSelf_hor().setAffected(affected);
            this.affected = new ArrayList<Field>();
            offen = openEndsSelfDebug(x, y, 1, 0);
            info[x][y].getSelf_hor_mul().setAffected(affected);
            p = p + params.kiValueSelf(a) * params.kiOpenValueSelf(offen);
            info[x][y].getSelf_hor().setParam(a, Typ.SELF);
            info[x][y].getSelf_hor_mul().setParam(offen, Typ.SELF_MUL);

            info[x][y].setOp_hor(new ParaInfo(params));
            info[x][y].setOp_hor_mul(new ParaInfo(params));
            this.affected = new ArrayList<Field>();
            a = numberOfBorderingDebug(x, y, 1, 0, op) + numberOfBorderingDebug(x, y, -1, 0, op);
            info[x][y].getOp_hor().setAffected(affected);
            this.affected = new ArrayList<Field>();
            offen = openEndsOpDebug(x, y, 1, 0);
            info[x][y].getOp_hor_mul().setAffected(affected);
            p = p + params.kiValueOp(a) * params.kiOpenValueOp(offen);
            info[x][y].getOp_hor().setParam(a, Typ.OP);
            info[x][y].getOp_hor_mul().setParam(offen, Typ.OP_MUL);

            // Süd-Nord
            info[x][y].setSelf_ver(new ParaInfo(params));
            info[x][y].setSelf_ver_mul(new ParaInfo(params));
            this.affected = new ArrayList<Field>();
            a = numberOfBorderingDebug(x, y, 0, 1, pl) + numberOfBorderingDebug(x, y, 0, -1, pl);
            info[x][y].getSelf_ver().setAffected(affected);
            this.affected = new ArrayList<Field>();
            offen = openEndsSelfDebug(x, y, 0, 1);
            info[x][y].getSelf_ver_mul().setAffected(affected);
            p = p + params.kiValueSelf(a) * params.kiOpenValueSelf(offen);
            info[x][y].getSelf_ver().setParam(a, Typ.SELF);
            info[x][y].getSelf_ver_mul().setParam(offen, Typ.SELF_MUL);

            info[x][y].setOp_ver(new ParaInfo(params));
            info[x][y].setOp_ver_mul(new ParaInfo(params));
            this.affected = new ArrayList<Field>();
            a = numberOfBorderingDebug(x, y, 0, 1, op) + numberOfBorderingDebug(x, y, 0, -1, op);
            info[x][y].getOp_ver().setAffected(affected);
            this.affected = new ArrayList<Field>();
            offen = openEndsOpDebug(x, y, 0, 1);
            info[x][y].getOp_ver_mul().setAffected(affected);
            p = p + params.kiValueOp(a) * params.kiOpenValueOp(offen);
            info[x][y].getOp_ver().setParam(a, Typ.OP);
            info[x][y].getOp_ver_mul().setParam(offen, Typ.OP_MUL);

            // Nordwest-Südost
            info[x][y].setSelf_nwtoso(new ParaInfo(params));
            info[x][y].setSelf_nwtoso_mul(new ParaInfo(params));
            this.affected = new ArrayList<Field>();
            a = numberOfBorderingDebug(x, y, 1, 1, pl) + numberOfBorderingDebug(x, y, -1, -1, pl);
            info[x][y].getSelf_nwtoso().setAffected(affected);
            this.affected = new ArrayList<Field>();
            offen = openEndsSelfDebug(x, y, 1, 1);
            info[x][y].getSelf_nwtoso_mul().setAffected(affected);
            p = p + params.kiValueSelf(a) * params.kiOpenValueSelf(offen);
            info[x][y].getSelf_nwtoso().setParam(a, Typ.SELF);
            info[x][y].getSelf_nwtoso_mul().setParam(offen, Typ.SELF_MUL);

            info[x][y].setOp_nwtoso(new ParaInfo(params));
            info[x][y].setOp_nwtoso_mul(new ParaInfo(params));
            this.affected = new ArrayList<Field>();
            a = numberOfBorderingDebug(x, y, 1, 1, op) + numberOfBorderingDebug(x, y, -1, -1, op);
            info[x][y].getOp_nwtoso().setAffected(affected);
            this.affected = new ArrayList<Field>();
            offen = openEndsOpDebug(x, y, 1, 1);
            info[x][y].getOp_nwtoso_mul().setAffected(affected);
            p = p + params.kiValueOp(a) * params.kiOpenValueOp(offen);
            info[x][y].getOp_nwtoso().setParam(a, Typ.OP);
            info[x][y].getOp_nwtoso_mul().setParam(offen, Typ.OP_MUL);

            // Südwest-Nordost
            info[x][y].setSelf_swtono(new ParaInfo(params));
            info[x][y].setSelf_swtono_mul(new ParaInfo(params));
            this.affected = new ArrayList<Field>();
            a = numberOfBorderingDebug(x, y, -1, +1, pl) + numberOfBorderingDebug(x, y, +1, -1, pl);
            info[x][y].getSelf_swtono().setAffected(affected);
            this.affected = new ArrayList<Field>();
            offen = openEndsSelfDebug(x, y, -1, +1);
            info[x][y].getSelf_swtono_mul().setAffected(affected);
            p = p + params.kiValueSelf(a) * params.kiOpenValueSelf(offen);
            info[x][y].getSelf_swtono().setParam(a, Typ.SELF);
            info[x][y].getSelf_swtono_mul().setParam(offen, Typ.SELF_MUL);

            info[x][y].setOp_swtono(new ParaInfo(params));
            info[x][y].setOp_swtono_mul(new ParaInfo(params));
            this.affected = new ArrayList<Field>();
            a = numberOfBorderingDebug(x, y, -1, +1, op) + numberOfBorderingDebug(x, y, +1, -1, op);
            info[x][y].getOp_swtono().setAffected(affected);
            this.affected = new ArrayList<Field>();
            offen = openEndsOpDebug(x, y, -1, +1);
            info[x][y].getOp_swtono_mul().setAffected(affected);
            p = p + params.kiValueOp(a) * params.kiOpenValueOp(offen);
            info[x][y].getOp_swtono().setParam(a, Typ.OP);
            info[x][y].getOp_swtono_mul().setParam(offen, Typ.OP_MUL);


            info[x][y].setTotal(p);
        }
        return p;
    }

    /* Gibt die Anzahl der gleichfarbigen in einer Reihe direkt hintereinander angrezenden Punkte an
     * x, y => Ausgangspunkt           Bps:  numberOfBordering(2, 1, 1, 0, RED);
     * dir_x, dir_y => Richtung (+1, -1, 0)    01234567
     * player => betreffender Spieler        0 OOO000OO
     *                                       1 OR0RRROO
     *                                       2 OROOOOOO
     *                                 return: 3
     * Richtungen: 1, 0 => Rechts
     *             0, 1 => Unten  usw.
     */
    private int numberOfBordering(int x, int y, int dir_x, int dir_y, State player) {
        int anz = 0;

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

    // Method Version for Debug Mode
    private int numberOfBorderingDebug(int x, int y, int dir_x, int dir_y, State player) {
        int anz = 0;

        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j] == player) {
                affected.add(new Field(i, j));
                anz++;
            } else {
                return anz;
            }
        }

        return anz;
    }

    /* Liefert die Anzahl der Enden an denen noch eine eigene 5er Reihe entstehen
     * könnte nachdem x, y gesetzt wurde. Parameter siehe "numberOfBordering". */
    private int openEndsSelf(int x, int y, int dir_x, int dir_y) {
        int anz = 1, open = 0;

        //Sind auf der Linie überhaupt noch 5 in einer Reihe möglich
        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j].equals(pl) || map[i][j].equals(GameIntelligence.State.NONE)) {
                anz++;
            } else {
                break;
            }
        }
        for (int i = x - dir_x, j = y - dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i - dir_x, j = j - dir_y) {
            if (map[i][j].equals(pl) || map[i][j].equals(GameIntelligence.State.NONE)) {
                anz++;
            } else {
                break;
            }
        }
        if (anz < 5) {
            return 0;
        }

        // Offen in die eine Richtung?
        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j].equals(pl)) {
                //nothing
            } else if (map[i][j].equals(GameIntelligence.State.NONE)) {
                open++;
                break;
            } else {
                break;
            }
        }

        // Offen in die andere Richtung?
        for (int i = x - dir_x, j = y - dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i - dir_x, j = j - dir_y) {
            if (map[i][j].equals(pl)) {
                //nothing
            } else if (map[i][j].equals(GameIntelligence.State.NONE)) {
                open++;
                break;
            } else {
                break;
            }
        }
        return open;
    }

    /* Liefert die Anzahl der Enden an denen noch eine eigene 5er Reihe entstehen
     * könnte nachdem x, y gesetzt wurde. Parameter siehe "numberOfBordering". 
     * DEBUG Version of this Method */
    private int openEndsSelfDebug(int x, int y, int dir_x, int dir_y) {
        int anz = 1, open = 0;

        //Sind auf der Linie überhaupt noch 5 in einer Reihe möglich
        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j].equals(pl) || map[i][j].equals(GameIntelligence.State.NONE)) {
                anz++;
            } else {
                break;
            }
        }
        for (int i = x - dir_x, j = y - dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i - dir_x, j = j - dir_y) {
            if (map[i][j].equals(pl) || map[i][j].equals(GameIntelligence.State.NONE)) {
                anz++;
            } else {
                break;
            }
        }
        if (anz < 5) {
            return 0;
        }

        // Offen in die eine Richtung?
        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j].equals(pl)) {
                //nothing
            } else if (map[i][j].equals(GameIntelligence.State.NONE)) {
                affected.add(new Field(i, j));
                open++;
                break;
            } else {
                break;
            }
        }

        // Offen in die andere Richtung?
        for (int i = x - dir_x, j = y - dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i - dir_x, j = j - dir_y) {
            if (map[i][j].equals(pl)) {
                //nothing
            } else if (map[i][j].equals(GameIntelligence.State.NONE)) {
                affected.add(new Field(i, j));
                open++;
                break;
            } else {
                break;
            }
        }
        return open;
    }

    /* Liefert die Anzahl der Enden an denen noch eine gegnerische 5er Reihe
     * entstehen könnte bevor x, y gesetzt wurde. Parameter siehe "numberOfBordering". */
    private int openEndsOp(int x, int y, int dir_x, int dir_y) {
        int anz = 1, open = 1;
        boolean isColorBordering = false, isColorBorderingTemp = false;

        //Sind auf der Linie überhaupt noch 5 in einer Reihe möglich
        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j].equals(op) || map[i][j].equals(GameIntelligence.State.NONE)) {
                anz++;
            } else {
                break;
            }
        }
        for (int i = x - dir_x, j = y - dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i - dir_x, j = j - dir_y) {
            if (map[i][j].equals(op) || map[i][j].equals(GameIntelligence.State.NONE)) {
                anz++;
            } else {
                break;
            }
        }
        if (anz < 5) {
            return 0;
        }

        // Offen in die eine Richtung?
        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j].equals(op)) {
                isColorBorderingTemp = true;
            } else if (map[i][j].equals(GameIntelligence.State.NONE)) {
                if (isColorBorderingTemp) {
                    open++;
                }
                break;
            } else {
                break;
            }
        }

        isColorBordering = isColorBorderingTemp;
        isColorBorderingTemp = false;

        // Offen in die andere Richtung?
        for (int i = x - dir_x, j = y - dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i - dir_x, j = j - dir_y) {
            if (map[i][j].equals(op)) {
                isColorBorderingTemp = true;
            } else if (map[i][j].equals(GameIntelligence.State.NONE)) {
                if (isColorBorderingTemp) {
                    open++;
                }
                break;
            } else {
                break;
            }
        }

        //Kein Angrenzendes Feld mit der Farbe "color"
        if (!isColorBordering && !isColorBorderingTemp) {
            return 0;
        }
        return open;
    }

    /* Liefert die Anzahl der Enden an denen noch eine gegnerische 5er Reihe
     * entstehen könnte bevor x, y gesetzt wurde. Parameter siehe "numberOfBordering". 
     * DEBUG Version of this Method */
    private int openEndsOpDebug(int x, int y, int dir_x, int dir_y) {
        int anz = 1, open = 1;
        boolean isColorBordering = false, isColorBorderingTemp = false;

        //Sind auf der Linie überhaupt noch 5 in einer Reihe möglich
        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j].equals(op) || map[i][j].equals(GameIntelligence.State.NONE)) {
                anz++;
            } else {
                break;
            }
        }
        for (int i = x - dir_x, j = y - dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i - dir_x, j = j - dir_y) {
            if (map[i][j].equals(op) || map[i][j].equals(GameIntelligence.State.NONE)) {
                anz++;
            } else {
                break;
            }
        }
        if (anz < 5) {
            return 0;
        }

        // Offen in die eine Richtung?
        for (int i = x + dir_x, j = y + dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i + dir_x, j = j + dir_y) {
            if (map[i][j].equals(op)) {
                isColorBorderingTemp = true;
            } else if (map[i][j].equals(GameIntelligence.State.NONE)) {
                if (isColorBorderingTemp) {
                    affected.add(new Field(i, j));
                    open++;
                }
                break;
            } else {
                break;
            }
        }

        isColorBordering = isColorBorderingTemp;
        isColorBorderingTemp = false;

        // Offen in die andere Richtung?
        for (int i = x - dir_x, j = y - dir_y; i
                >= 0 && i < dim && j >= 0 && j < dim; i = i - dir_x, j = j - dir_y) {
            if (map[i][j].equals(op)) {
                isColorBorderingTemp = true;
            } else if (map[i][j].equals(GameIntelligence.State.NONE)) {
                if (isColorBorderingTemp) {
                    affected.add(new Field(i, j));
                    open++;
                }
                break;
            } else {
                break;
            }
        }

        //Kein Angrenzendes Feld mit der Farbe "color"
        if (!isColorBordering && !isColorBorderingTemp) {
            return 0;
        } else {
            affected.add(new Field(x, y));
        }
        return open;
    }

    public FieldInfo[][] getInfo() {
        if (enableInfo) {
            return info;
        } else {
            return null;
        }
    }

    public boolean isEnabledInfo() {
        return enableInfo;
    }

    private void setPointsNull(int x, int y) {
        pf[x][y] = -1 - params.getKi_blur() * 100; // Wegen Ki Blur damit sicher nieriger!
        if (enableInfo) {
            info[x][y] = null;
        }
    }
}
