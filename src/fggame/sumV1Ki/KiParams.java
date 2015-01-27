/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fggame.sumV1Ki;

import java.io.Serializable;

/**
 *
 * @author Jotschi
 */
public class KiParams implements Serializable {
   /**
     * Enumeration aller Ki Parameter.
     */
    public static enum Param {
        SELF_0, SELF_1, SELF_2, SELF_3, SELF_4UP,
        OP_0, OP_1, OP_2, OP_3, OP_4UP
    };
    private int self_0;
    private int self_1;
    private int self_2;
    private int self_3;
    private int self_4up;
    private int op_0;
    private int op_1;
    private int op_2;
    private int op_3;
    private int op_4up;

    /**
     * Erzeugt ein leeres KI-Params Objekt.
     */
    public KiParams() {
    }

    public int getOp_0() {
        return op_0;
    }

    public void setOp_0(int op_0) {
        this.op_0 = op_0;
    }

    public int getOp_1() {
        return op_1;
    }

    public void setOp_1(int op_1) {
        this.op_1 = op_1;
    }

    public int getOp_2() {
        return op_2;
    }

    public void setOp_2(int op_2) {
        this.op_2 = op_2;
    }

    public int getOp_3() {
        return op_3;
    }

    public void setOp_3(int op_3) {
        this.op_3 = op_3;
    }

    public int getOp_4up() {
        return op_4up;
    }

    public void setOp_4up(int op_4up) {
        this.op_4up = op_4up;
    }

    public int getSelf_0() {
        return self_0;
    }

    public void setSelf_0(int self_0) {
        this.self_0 = self_0;
    }

    public int getSelf_1() {
        return self_1;
    }

    public void setSelf_1(int self_1) {
        this.self_1 = self_1;
    }

    public int getSelf_2() {
        return self_2;
    }

    public void setSelf_2(int self_2) {
        this.self_2 = self_2;
    }

    public int getSelf_3() {
        return self_3;
    }

    public void setSelf_3(int self_3) {
        this.self_3 = self_3;
    }

    public int getSelf_4up() {
        return self_4up;
    }

    public void setSelf_4up(int self_4up) {
        this.self_4up = self_4up;
    }

    /**
     * Liefert den Wert des übergebenen Parameters
     * @param p
     * @return Wert des entsprechenden Parameters.
     */
    public int getKiValue(Param p) {
        if (p == Param.SELF_0) {
            return getSelf_0();
        }
        if (p == Param.SELF_1) {
            return getSelf_1();
        }
        if (p == Param.SELF_2) {
            return getSelf_2();
        }
        if (p == Param.SELF_3) {
            return getSelf_3();
        }
        if (p == Param.SELF_4UP) {
            return getSelf_4up();
        }
        if (p == Param.OP_0) {
            return getOp_0();
        }
        if (p == Param.OP_1) {
            return getOp_1();
        }
        if (p == Param.OP_2) {
            return getOp_2();
        }
        if (p == Param.OP_3) {
            return getOp_3();
        }
        if (p == Param.OP_4UP) {
            return getOp_4up();
        }
        return -1;
    }

    /**
     * Liefert den Wert der Parameter Self_0 bis Self_4up, abhängig von der übergebenen Zahl.
     * @param anz - Gibt den betroffnen Self Parameter an.
     * @return Wert des entsprechenden Parameters.
     */
    public int kiValueSelf(int anz) {
        if (anz == 0) {
            return getSelf_0();
        }
        if (anz == 1) {
            return getSelf_1();
        }
        if (anz == 2) {
            return getSelf_2();
        }
        if (anz == 3) {
            return getSelf_3();
        }
        if (anz >= 4) {
            return getSelf_4up();
        }
        return -1;
    }

    /**
     * Liefert den Wert der Parameter Op_0 bis Op_4up, abhängig von der übergebenen Zahl.
     * @param anz - Gibt den betroffnen Op Parameter an.
     * @return Wert des entsprechenden Parameters.
     */
    public int kiValueOp(int anz) {
        if (anz == 0) {
            return getOp_0();
        }
        if (anz == 1) {
            return getOp_1();
        }
        if (anz == 2) {
            return getOp_2();
        }
        if (anz == 3) {
            return getOp_3();
        }
        if (anz >= 4) {
            return getOp_4up();
        }
        return -1;
    }

    @Override
    public String toString() {
        return "KiParams{" + "self_0=" + self_0 + "self_1=" + self_1 + "self_2=" + self_2 + "self_3=" + self_3 + "self_4up=" + self_4up + "op_0=" + op_0 + "op_1=" + op_1 + "op_2=" + op_2 + "op_3=" + op_3 + "op_4up=" + op_4up + '}';
    }

    public String getAsText() {
            StringBuilder sb = new StringBuilder();
            sb.append("SELF_0=").append(getSelf_0()).append("\r\n");
            sb.append("SELF_1=").append(getSelf_1()).append("\r\n");
            sb.append("SELF_2=").append(getSelf_2()).append("\r\n");
            sb.append("SELF_3=").append(getSelf_3()).append("\r\n");
            sb.append("SELF_4UP=").append(getSelf_4up()).append("\r\n");
            sb.append("OP_0=").append(getOp_0()).append("\r\n");
            sb.append("OP_1=").append(getOp_1()).append("\r\n");
            sb.append("OP_2=").append(getOp_2()).append("\r\n");
            sb.append("OP_3=").append(getOp_3()).append("\r\n");
            sb.append("OP_4UP=").append(getOp_4up()).append("\r\n");
            return sb.toString();
    }

    public void setAsText(String s) {
         String f[] = s.split("\n");
         for(String line : f) {
             parseLine(line);
         }
    }

   private void parseLine(String s) {
        String[] sf;
        sf = s.split("=");
        try {
            if (sf[0].equals("SELF_0")) {
                self_0 = Integer.parseInt(sf[1]);
            }
            if (sf[0].equals("SELF_1")) {
                self_1 = Integer.parseInt(sf[1]);
            }
            if (sf[0].equals("SELF_2")) {
                self_2 = Integer.parseInt(sf[1]);
            }
            if (sf[0].equals("SELF_3")) {
                self_3 = Integer.parseInt(sf[1]);
            }
            if (sf[0].equals("SELF_4UP")) {
                self_4up = Integer.parseInt(sf[1]);
            }
            if (sf[0].equals("OP_0")) {
                op_0 = Integer.parseInt(sf[1]);
            }
            if (sf[0].equals("OP_1")) {
                op_1 = Integer.parseInt(sf[1]);
            }
            if (sf[0].equals("OP_2")) {
                op_2 = Integer.parseInt(sf[1]);
            }
            if (sf[0].equals("OP_3")) {
                op_3 = Integer.parseInt(sf[1]);
            }
            if (sf[0].equals("OP_4UP")) {
                op_4up = Integer.parseInt(sf[1]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Syntaxfehler in bei den KI Parametern!");
        } catch (NumberFormatException e) {
            System.out.println("Fehler beim lesen zumindest eines Parameters!");
        }
    }    
}
