/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fgevo;

import fggame.defaultKi.DefaultKiIntelligence;
import fggame.defaultKi.KiParams;
import fgplay.MassResults;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Jotschi
 */
public class FGEvo {
    public static final int POPULATION = 2000;
    public static final int PERCENT_SURVIVE = 5;   // teilbar durch POPULATION
    public static final int STEPS_PER_GENERATION = 10;     // teilbar durch POPULATION
    public static final int GAMES_PER_STEP = 25;   // Games per Step and Ki
    public static final int DIMENSION = 15;
    public static final int GENERATIONS = 9999;
    public static final float MUTATION_VARIANCE = 0.2f;
    public static final int SURVIVERS = POPULATION * PERCENT_SURVIVE / 100;
    public static final int CHILDS_PER_KI = POPULATION / SURVIVERS;
    public static final int KILLED_PER_STEP = (POPULATION - SURVIVERS) / STEPS_PER_GENERATION;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        List<RatedKi> pop = new ArrayList<RatedKi>();
        
        System.out.println("BASE POP=" + POPULATION);
        System.out.println("PERCENT SURVIVE=" + PERCENT_SURVIVE);
        System.out.println("STEPS PER GENERATION=" + STEPS_PER_GENERATION);
        System.out.println("SURVIVERS PER GENERATION=" + SURVIVERS);
        System.out.println("GAMES PER GENERATION=" + GAMES_PER_STEP*STEPS_PER_GENERATION*POPULATION/2);
        System.out.println("CHILDS PER KI=" + CHILDS_PER_KI);
        System.out.println("MUTATION VARIANCE=" + MUTATION_VARIANCE);
        System.out.println("GENERATIONS=" + GENERATIONS);
        System.out.println("------------------------------\n");
        
        
        DefaultKiIntelligence reference_ki = getReferenceKi();
        
        // Erzeuge Start-Population
        for(int i = 0; i < POPULATION; i++) {
            pop.add(new RatedKi(createRandomKi(0, 1000), 0));
        }
        
        for(int g = 0; g < GENERATIONS; g++) {
            System.out.println("GENERATION " + g);
            // Pro STEP KIs töten
            for(int i = 0; i < STEPS_PER_GENERATION; i++) {
                System.out.println("  STEP " + i);
                // Spiele bringen Punkte
                for(int j = 0; j < pop.size(); j++) {
                    RatedKi act = pop.get(j);
                    for(int k = 0; k < GAMES_PER_STEP; k++) {
                        // zufälliger Gegner
                        int l = getRandomNumber(0, pop.size() - 1);
                        RatedKi op = pop.get(l);
                        int erg[] = new MassResults(act.getKi(), op.getKi(), DIMENSION, 4).getErg(4); 
                        act.setPoints(act.getPoints() + erg[0] + erg[1] * 3);
                    }
                }
                // Kis sortieren und schlechtesten töten
                Collections.sort(pop);
                pop = pop.subList(KILLED_PER_STEP, pop.size());
            }  
            // Neue Generation erzeugen
            List<RatedKi> nextpop = new ArrayList<RatedKi>();
            int i = 0;
            for(RatedKi act : pop) {
                for(int j = 0; j < CHILDS_PER_KI; j++) {
                    // random partner
                    RatedKi op = pop.get(getRandomNumber(0, pop.size() - 1));
                    DefaultKiIntelligence child = getChildKi(act.getKi(), op.getKi());
                    nextpop.add(new RatedKi(child, 0));
                }
                // Gib KIs Referencspiele aus
                int erg[] = new MassResults(act.getKi(), reference_ki, DIMENSION, 10).getErg(1000);
                System.out.println("\n    SURVIVOR KI " + i + ": " + erg[0] + "(draw) " + erg[1] + "(win) " + erg[2] + "(lose)");
                // Schreibe Ki in File
                FileWriter fstream = new FileWriter("GEN" + g + "_KI" + i + ".ki");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(act.getKi().getKi().getAsText());
                out.close();  
                fstream.close();
                
                i++;
            }  
            
            pop = nextpop;
        }
    }
    
    
    public static DefaultKiIntelligence getChildKi(DefaultKiIntelligence mother, DefaultKiIntelligence father) {
        KiParams p = new KiParams();
        KiParams m = mother.getKi();
        KiParams f = father.getKi();
        p.setSelf_0(Math.round(validateMaxNumber((int)(m.getSelf_0() * getRandomMutationFactor() + f.getSelf_0() * getRandomMutationFactor()))));
        p.setSelf_1(Math.round(validateMaxNumber((int)(m.getSelf_1() * getRandomMutationFactor() + f.getSelf_1() * getRandomMutationFactor()))));
        p.setSelf_2(Math.round(validateMaxNumber((int)(m.getSelf_2() * getRandomMutationFactor() + f.getSelf_2() * getRandomMutationFactor()))));
        p.setSelf_3(Math.round(validateMaxNumber((int)(m.getSelf_3() * getRandomMutationFactor() + f.getSelf_3() * getRandomMutationFactor()))));
        p.setSelf_4up(Math.round(validateMaxNumber((int)(m.getSelf_4up() * getRandomMutationFactor() + f.getSelf_4up() * getRandomMutationFactor()))));
        p.setOp_0(Math.round(validateMaxNumber((int)(m.getOp_0() * getRandomMutationFactor() + f.getOp_0() * getRandomMutationFactor()))));
        p.setOp_1(Math.round(validateMaxNumber((int)(m.getOp_1() * getRandomMutationFactor() + f.getOp_1() * getRandomMutationFactor()))));
        p.setOp_2(Math.round(validateMaxNumber((int)(m.getOp_2() * getRandomMutationFactor() + f.getOp_2() * getRandomMutationFactor()))));
        p.setOp_3(Math.round(validateMaxNumber((int)(m.getOp_3() * getRandomMutationFactor() + f.getOp_3() * getRandomMutationFactor()))));
        p.setOp_4up(Math.round(validateMaxNumber((int)(m.getOp_4up() * getRandomMutationFactor() + f.getOp_4up() * getRandomMutationFactor()))));   
        p.setMul_self_open_0(Math.round(validateMaxNumber((int)(m.getMul_self_open_0() * getRandomMutationFactor() + f.getMul_self_open_0() * getRandomMutationFactor()))));
        p.setMul_self_open_1(Math.round(validateMaxNumber((int)(m.getMul_self_open_1() * getRandomMutationFactor() + f.getMul_self_open_1() * getRandomMutationFactor()))));
        p.setMul_self_open_2(Math.round(validateMaxNumber((int)(m.getMul_self_open_2() * getRandomMutationFactor() + f.getMul_self_open_2() * getRandomMutationFactor()))));
        p.setMul_op_open_0(Math.round(validateMaxNumber((int)(m.getMul_op_open_0() * getRandomMutationFactor() + f.getMul_op_open_0() * getRandomMutationFactor()))));
        p.setMul_op_open_1(Math.round(validateMaxNumber((int)(m.getMul_op_open_1() * getRandomMutationFactor() + f.getMul_op_open_1() * getRandomMutationFactor()))));
        p.setMul_op_open_2(Math.round(validateMaxNumber((int)(m.getMul_op_open_2() * getRandomMutationFactor() + f.getMul_op_open_2() * getRandomMutationFactor()))));       
        p.setMul_op_open_3(Math.round(validateMaxNumber((int)(m.getMul_op_open_3() * getRandomMutationFactor() + f.getMul_op_open_3() * getRandomMutationFactor()))));       
        p.setKi_accurcy_percent(100);
        p.setKi_blur(0);
        return new DefaultKiIntelligence(p);
    }
    
    public static float getRandomMutationFactor() {
        return 1 + ((float)(Math.random() * 2 * MUTATION_VARIANCE) - MUTATION_VARIANCE);       
    }
    
    public static int validateMaxNumber(int nb) {
        if(nb > 99999) {
            return 99999;
        }
        if(nb < 0) {
            return 0;
        }
        return nb;
    }
    
    
    public static DefaultKiIntelligence createRandomKi(int from, int to) {
        Math.round((Math.random() * to) - from);
        KiParams p = new KiParams();
        p.setSelf_0(getRandomNumber(from, to));
        p.setSelf_1(getRandomNumber(from, to));
        p.setSelf_2(getRandomNumber(from, to));
        p.setSelf_3(getRandomNumber(from, to));
        p.setSelf_4up(getRandomNumber(from, to));
        p.setOp_0(getRandomNumber(from, to));
        p.setOp_1(getRandomNumber(from, to));
        p.setOp_2(getRandomNumber(from, to));
        p.setOp_3(getRandomNumber(from, to));
        p.setOp_4up(getRandomNumber(from, to));   
        p.setMul_op_open_0(getRandomNumber(from, to));
        p.setMul_op_open_1(getRandomNumber(from, to));
        p.setMul_op_open_2(getRandomNumber(from, to));
        p.setMul_op_open_3(getRandomNumber(from, to));
        p.setMul_self_open_0(getRandomNumber(from, to));
        p.setMul_self_open_1(getRandomNumber(from, to));
        p.setMul_self_open_2(getRandomNumber(from, to)); 
        p.setKi_accurcy_percent(100);
        p.setKi_blur(0);
        DefaultKiIntelligence ki = new DefaultKiIntelligence(p);
        
        return ki;
    }
    
    public static int getRandomNumber(int from, int to) {
       return (int) Math.round((Math.random() * to) - from);        
    }
    
    public static DefaultKiIntelligence getReferenceKi() {
        KiParams p = new KiParams();
        p.setSelf_0(1);
        p.setSelf_1(2);
        p.setSelf_2(3);
        p.setSelf_3(15);
        p.setSelf_4up(1000);
        p.setOp_0(1);
        p.setOp_1(2);
        p.setOp_2(3);
        p.setOp_3(10);
        p.setOp_4up(500);   
        p.setMul_op_open_0(0);
        p.setMul_op_open_1(10);
        p.setMul_op_open_2(20);
        p.setMul_op_open_3(30);
        p.setMul_self_open_0(1);
        p.setMul_self_open_1(10);
        p.setMul_self_open_2(20); 
        p.setKi_accurcy_percent(100);
        p.setKi_blur(0);
        return new DefaultKiIntelligence(p);
    }

}
