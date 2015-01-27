/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jotschi
 */
public class StandardKiParams {

    public static fggame.defaultKi.KiParams getDefaultKiParams() {
        fggame.defaultKi.KiParams defparams = new fggame.defaultKi.KiParams();
        defparams.setSelf_0(1);
        defparams.setSelf_1(2);
        defparams.setSelf_2(3);
        defparams.setSelf_3(15);
        defparams.setSelf_4up(1000);
        defparams.setMul_self_open_0(1);
        defparams.setMul_self_open_1(10);
        defparams.setMul_self_open_2(20);
        defparams.setOp_0(10);
        defparams.setOp_1(20);
        defparams.setOp_2(30);
        defparams.setOp_3(100);
        defparams.setOp_4up(500);
        defparams.setMul_op_open_0(0);
        defparams.setMul_op_open_1(1);
        defparams.setMul_op_open_2(2);
        defparams.setMul_op_open_3(3);
        return defparams;
    }

    public static fggame.sumV1Ki.KiParams getSumV1KiParams() {
        fggame.sumV1Ki.KiParams sumparams = new fggame.sumV1Ki.KiParams();
   /*     sumparams.setSelf_0(1);
        sumparams.setSelf_1(10);
        sumparams.setSelf_2(20);
        sumparams.setSelf_3(200);
        sumparams.setSelf_4up(400);
        sumparams.setOp_0(0);
        sumparams.setOp_1(-10);
        sumparams.setOp_2(-20);
        sumparams.setOp_3(-600);
        sumparams.setOp_4up(-100000); */
        
        sumparams.setSelf_0(100001);
        sumparams.setSelf_1(100010);
        sumparams.setSelf_2(100020);
        sumparams.setSelf_3(100200);
        sumparams.setSelf_4up(100400);
        sumparams.setOp_0(100000);
        sumparams.setOp_1(99990);
        sumparams.setOp_2(99980);
        sumparams.setOp_3(99400);
        sumparams.setOp_4up(0);        
        return sumparams;
    }
}
