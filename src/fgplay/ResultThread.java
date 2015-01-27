/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fgplay;

import fggame.GameIntelligence;
import fggame.KiIntelligence;

/**
 *
 * @author Jotschi
 */
class ResultThread extends Thread {

    private MassResults parent;
    private Thread parentThr;
    private int dim, anz;
    private KiIntelligence ki1, ki2;
    private boolean startorder; // Notwendig da bei vielen Threads und kleiner (ungerader) Spielzahl sonst ein Vorteil für Player 1 entstehen könnte 

    public ResultThread(int dim, int anz, KiIntelligence ki1, KiIntelligence ki2, MassResults parent, Thread parentThr, boolean startorder) {
        this.dim = dim;
        this.anz = anz;
        this.parentThr = parentThr;
        if (startorder) {
            this.ki1 = ki1;
            this.ki2 = ki2;
        } else {
            this.ki1 = ki2;
            this.ki2 = ki1;
        }
        this.startorder = startorder;
        this.parent = parent;
    }

    @Override
    public void run() {
        int i;
        int pl1 = 0;
        int pl2 = 0;
        int none = 0;
        
        // Erste Hälfte nicht vertauscht
        for (i = 0; i < anz / 2; i++) {
            GameIntelligence g = new GameIntelligence(dim, ki1, ki2);
            GameIntelligence.State s = g.getResult();
            if (s == fggame.GameIntelligence.State.NONE) {
                none++;
            } else if (s == fggame.GameIntelligence.State.PLAYER_1) {
                pl1++;
            } else if (s == fggame.GameIntelligence.State.PLAYER_2) {
                pl2++;
            }
        }
        // Zweite Hälfte vertauschter Aufruf 
        for (; i < anz; i++) {
            GameIntelligence g = new GameIntelligence(dim, ki2, ki1);
            GameIntelligence.State s = g.getResult();
            if (s == fggame.GameIntelligence.State.NONE) {
                none++;
            } else if (s == fggame.GameIntelligence.State.PLAYER_1) {
                pl2++;
            } else if (s == fggame.GameIntelligence.State.PLAYER_2) {
                pl1++;
            }
        }
        
        if(startorder) {
            parent.incPlayer1(pl1);
            parent.incPlayer2(pl2);
            parent.incNone(none);   
        }else {
            parent.incPlayer1(pl2);
            parent.incPlayer2(pl1);
            parent.incNone(none);  
        }
        parentThr.interrupt();
    }
}
