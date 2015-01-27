/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fggame.defaultKi;

import fggame.Field;
import fggame.defaultKi.KiParams.Param;
import java.io.Serializable;
import java.util.List;

/**
 * Kapselt Informationen zu einer Teilberechnung. Zum Beispiel für eine bestimmte Richtung.
 * Enthält eine Liste der von dieser Berechnung betroffenen Felder.
 * @author Jotschi
 */
public class ParaInfo implements Serializable {

    public static enum Typ {

        SELF, OP, SELF_MUL, OP_MUL
    }
    private KiParams ki;
    private List<Field> affected;    // Liste der Betroffenen Felder
    private KiParams.Param param;

    /**
     * Erzeugt ein ParaInfo Objekt.
     * @param ki - Ki der zughörigen Berechnung.
     */
    public ParaInfo(KiParams ki) {
        this.ki = ki;
    }

    /**
     * @return Liste aller dieser Teilberchnung betroffenen Felder. 
     * (Zum Beispiel die offenen Enden oder die betroffene Reihe)
     */
    public List<Field> getAffected() {
        return affected;
    }

    public void setAffected(List<Field> affected) {
        this.affected = affected;
    }

    /**
     * @return Für welchen Parameter wird berechnet.
     */
    public Param getParam() {
        return param;
    }

    /**
     * @return Wert der entsprechenden Teilberechnung.
     */
    public int getValue() {
        return ki.getKiValue(param);
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public void setParam(int nr, Typ t) {
        if (t == Typ.SELF) {
            switch (nr) {
                case 0:
                    param = Param.SELF_0;
                    return;
                case 1:
                    param = Param.SELF_1;
                    return;
                case 2:
                    param = Param.SELF_2;
                    return;
                case 3:
                    param = Param.SELF_3;
                    return;
                default:
                    param = Param.SELF_4UP;
                    return;
            }
        } else if (t == Typ.SELF_MUL) {
            switch (nr) {
                case 0:
                    param = Param.MUL_SELF_OPEN_0;
                    return;
                case 1:
                    param = Param.MUL_SELF_OPEN_1;
                    return;
                case 2:
                    param = Param.MUL_SELF_OPEN_2;
                    return;
            }
        } else if (t == Typ.OP) {
            switch (nr) {
                case 0:
                    param = Param.OP_0;
                    return;
                case 1:
                    param = Param.OP_1;
                    return;
                case 2:
                    param = Param.OP_2;
                    return;
                case 3:
                    param = Param.OP_3;
                    return;
                default:
                    param = Param.OP_4UP;
                    return;
            }
        } else if (t == Typ.OP_MUL) {
            switch (nr) {
                case 0:
                    param = Param.MUL_OP_OPEN_0;
                    return;
                case 1:
                    param = Param.MUL_OP_OPEN_1;
                    return;
                case 2:
                    param = Param.MUL_OP_OPEN_2;
                    return;
                case 3:
                    param = Param.MUL_OP_OPEN_3;
                    return;
            }
        }
    }

    @Override
    public String toString() {
        return param.toString() + "(Value:" + this.getValue() + ", Affected Fields:" + affected + ")";
    }
}
