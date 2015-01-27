
import fggame.Field;
import fggame.defaultKi.FieldInfo;
import fggame.GameIntelligence;
import fggame.defaultKi.DefaultKiIntelligence;
import fggame.defaultKi.KiParams;
import java.io.IOException;

/**
 * Manuel Test. Press enter for next draw.
 * @author Jotschi
 */
public class DefaultKiTestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        KiParams params = new KiParams();

        params.setSelf_0(1);
        params.setSelf_1(2);
        params.setSelf_2(3);
        params.setSelf_3(15);
        params.setSelf_4up(1000);
        params.setMul_self_open_0(1);
        params.setMul_self_open_1(10);
        params.setMul_self_open_2(20);
        params.setOp_0(10);
        params.setOp_1(20);
        params.setOp_2(30);
        params.setOp_3(100);
        params.setOp_4up(500);
        params.setMul_op_open_0(0);
        params.setMul_op_open_1(1);
        params.setMul_op_open_2(2);
        params.setMul_op_open_3(3);

        DefaultKiIntelligence ki1 = new DefaultKiIntelligence(params, true);
        DefaultKiIntelligence ki2 = new DefaultKiIntelligence(params, true);
        GameIntelligence g = new GameIntelligence(10, ki1, ki2);
       
        boolean p1 = true;
        while (true) {
            System.in.read();
            g.nextDraw();
            System.out.println(g);
            Field f = g.getDraw().getField();
            FieldInfo fi = null;
            if (p1) {
                fi = ki1.getInfo()[f.getX()][f.getY()];
                p1 = false;
            } else {
                fi = ki2.getInfo()[f.getX()][f.getY()];
                p1 = true;
            }
            System.out.println(fi);
        }
    }
}
