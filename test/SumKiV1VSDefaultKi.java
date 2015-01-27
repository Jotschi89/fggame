
import fggame.GameIntelligence;
import fggame.defaultKi.DefaultKiIntelligence;
import fggame.sumV1Ki.SumV1KiIntelligence;
import java.io.IOException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Manuel Test. Press enter for next draw.
 * @author Jotschi
 */
public class SumKiV1VSDefaultKi {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        SumV1KiIntelligence ki1 = new SumV1KiIntelligence(StandardKiParams.getSumV1KiParams());
        DefaultKiIntelligence ki2 = new DefaultKiIntelligence(StandardKiParams.getDefaultKiParams());
        GameIntelligence g = new GameIntelligence(10, ki1, ki2);
        
        while (true) {
            System.in.read();
            g.nextDraw();
            System.out.println(g);
        }
    }
}
