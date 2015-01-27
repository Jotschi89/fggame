/*
Stellt statische Methoden zum durchführen einer großen Anzahl von Spielen von einer Ki gegen eine andere zu verfügung.
 */
package fgplay;

import fggame.GameIntelligence;
import fggame.GameIntelligence.State;
import fggame.KiIntelligence;
import fggame.defaultKi.KiParams;
import java.util.Date;

/**
 * Liefert statistische Ergebnisse über viele Spiele zwischen zwei KIs.
 * @author Jotschi
 */
public class MassResults {

    private KiIntelligence ki1, ki2;
    private int dim, thr;
    private int erg[];

    /**
     * Erzeugt ein MassResult Objekt (singlethreaded)
     * @param ki1 - Ki1
     * @param ki2 - Ki2
     * @param dim - Spielfeldgröße
     */
    public MassResults(KiIntelligence ki1, KiIntelligence ki2, int dim) {
        this.ki1 = ki1;
        this.ki2 = ki2;
        this.dim = dim;
        this.thr = 1;
    }
    
    /**
     * Erzeugt ein MassResult Objekt (multithreaded)
     * @param ki1 - Ki1
     * @param ki2 - Ki2
     * @param dim - Spielfeldgröße
     * @param thr - Anzahl der Threads
     */
    public MassResults(KiIntelligence ki1, KiIntelligence ki2, int dim, int thr) {
        this.ki1 = ki1;
        this.ki2 = ki2;
        this.dim = dim;

        if (thr > 0) {
            this.thr = thr;
        } else {
            this.thr = 1;
        }
    }

    /** Durchläuft die übergebene Zahl und Art der Spiele und liefert die Ergebnisse zurück.
     * @param int anz - Anzahl der zu spielenden Spiele. 
     * @return int[] Feld mit: 'Unentschieden', 'ki1Siege', 'ki2Siege', 'Rechenzeit'
     */
    public int[] getErg(int anz) {
        long z = new Date().getTime();
        erg = new int[4];

        // Multithreaded
        if (thr > 1) {
            int games_per_thr = anz / thr;
            int remain_games = anz;
            boolean toogle = true;
            // Threads starten
            for (int i = 0; i < thr; i++) {
                new ResultThread(dim, games_per_thr, ki1.clone(), ki2.clone(), this, Thread.currentThread(), toogle).start();

                // Verbleibende Spiele bestimmen
                remain_games -= games_per_thr;
                if (toogle) {
                    toogle = false;
                } else {
                    toogle = true;
                }
            }
            // Restliche in extra Thread
            if (remain_games > 0) {
                new ResultThread(dim, remain_games, ki1.clone(), ki2.clone(), this, Thread.currentThread(), toogle).start();
            }
            // Warten bis alle fertig sind
            while (erg[0] + erg[1] + erg[2] < anz) {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException ex) {
                    Thread.interrupted();
                }
            }

        } // Singlethreaded
        else {
            int i;
            for (i = 0; i < anz / 2; i++) {
                GameIntelligence g = new GameIntelligence(dim, ki1, ki2);
                State s = g.getResult();
                if (s == fggame.GameIntelligence.State.NONE) {
                    erg[0]++;
                } else if (s == fggame.GameIntelligence.State.PLAYER_1) {
                    erg[1]++;
                } else if (s == fggame.GameIntelligence.State.PLAYER_2) {
                    erg[2]++;
                }
            }

            for (; i < anz; i++) {
                GameIntelligence g = new GameIntelligence(dim, ki2, ki1);
                State s = g.getResult();
                if (s == fggame.GameIntelligence.State.NONE) {
                    erg[0]++;
                } else if (s == fggame.GameIntelligence.State.PLAYER_1) {
                    erg[2]++;
                } else if (s == fggame.GameIntelligence.State.PLAYER_2) {
                    erg[1]++;
                }
            }
        }

        z = new Date().getTime() - z;
        erg[3] = (int) z;

        return erg;
    }

    protected synchronized void incPlayer1(int inc) {
        erg[1] += inc;
    }

    protected synchronized void incPlayer2(int inc) {
        erg[2] += inc;
    }

    protected synchronized void incNone(int inc) {
        erg[0] += inc;
    }
}
