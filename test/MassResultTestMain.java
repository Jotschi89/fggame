
import fggame.defaultKi.DefaultKiIntelligence;
import fggame.defaultKi.KiParams;
import fggame.sumV1Ki.SumV1KiIntelligence;
import fgplay.MassResults;
import java.io.IOException;


/**
 *
 * @author Jotschi
 */
public class MassResultTestMain {

    public static void main(String[] args) throws IOException {

        DefaultKiIntelligence defki1 = new DefaultKiIntelligence(StandardKiParams.getDefaultKiParams());
        DefaultKiIntelligence defki2 = new DefaultKiIntelligence(StandardKiParams.getDefaultKiParams());
        SumV1KiIntelligence sumki1 = new SumV1KiIntelligence(StandardKiParams.getSumV1KiParams());
        SumV1KiIntelligence sumki2 = new SumV1KiIntelligence(StandardKiParams.getSumV1KiParams());
        
        MassResults mr = new MassResults(defki1, sumki2, 15, 4);

        System.out.println("'Unentschieden' 'ki1Siege' 'ki2Siege' 'Rechenzeit'");
        int erg[] = mr.getErg(100);
        System.out.println(erg[0] + "  " + erg[1] + "  " + erg[2] + "  " + erg[3]);
    }
}

