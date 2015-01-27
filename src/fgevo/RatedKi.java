/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fgevo;

import fggame.KiIntelligence;
import fggame.defaultKi.DefaultKiIntelligence;

/**
 *
 * @author Jotschi
 */
public class RatedKi implements Comparable<RatedKi> {
    private DefaultKiIntelligence ki;
    private int points;

    public RatedKi(DefaultKiIntelligence ki, int points) {
        this.ki = ki;
        this.points = points;
    }    
    
    public DefaultKiIntelligence getKi() {
        return ki;
    }

    public void setKi(DefaultKiIntelligence ki) {
        this.ki = ki;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int compareTo(RatedKi o) {
        return this.getPoints() - o.getPoints();
    }
    
    
}
