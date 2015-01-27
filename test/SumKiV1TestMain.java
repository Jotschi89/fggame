
import fggame.GameIntelligence;
import fggame.defaultKi.DefaultKiIntelligence;
import fggame.sumV1Ki.SumV1KiIntelligence;
import java.io.IOException;

/**
 * Manuel Test. Press enter for next draw
 * @author Jotschi
 */
public class SumKiV1TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        fggame.sumV1Ki.KiParams sumparams = new fggame.sumV1Ki.KiParams();
        fggame.sumV1Ki.KiParams sumparams2 = new fggame.sumV1Ki.KiParams();
        sumparams.setSelf_0(9);
        sumparams.setSelf_1(10);
        sumparams.setSelf_2(18);
        sumparams.setSelf_3(180);
        sumparams.setSelf_4up(360);
        sumparams.setOp_0(0);
        sumparams.setOp_1(12);
        sumparams.setOp_2(18);
        sumparams.setOp_3(600);
        sumparams.setOp_4up(50000);
           
        
        SumV1KiIntelligence ki1 = new SumV1KiIntelligence(sumparams);
        SumV1KiIntelligence ki2 = new SumV1KiIntelligence(sumparams);
        GameIntelligence g = new GameIntelligence(10, ki1, ki2);

        while (true) {
            System.in.read();
            g.nextDraw();
            System.out.println(g);
        }
    }
}
