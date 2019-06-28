import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class Retrosynthesis {
    public static IAtomContainerSet step(IAtomContainerSet molecules, Reactions react, Transformations transformations) throws CDKException, IOException, CloneNotSupportedException {
        IAtomContainerSet set = new AtomContainerSet();

        for (IAtomContainer m : molecules.atomContainers()){
            IAtomContainer clone1 = m.clone();
            ArrayList<IAtomContainer> products = react.reactionAnalysis(clone1);
            if (products.size() == 0){
                DepictionGenerator dg = new DepictionGenerator().withSize(512, 512)
                        .withAtomColors();
                for (int i = 0; i < 2; i++){
//                    System.out.println(i + "_____________________ INDEx");
                    IAtomContainer copy = m.clone();
                    IAtomContainer test = transformations.reactionAnalysis(copy, i);

                    if (test == null){
                        continue;
                    }
                    else{
                        AtomContainerManipulator.convertImplicitToExplicitHydrogens(test);
                        dg.depict(test).writeTo("test.png");
                        ArrayList<IAtomContainer> products1 = react.reactionAnalysis(test);
                        System.out.println(products1.size() + "SIZE IMPORTANT");
                        if (products1.size() > 0){
                            for (IAtomContainer mols : products1){
                                set.addAtomContainer(mols);
                            }
                            break;
                        }
                    }
                }
            }
            else{
                for (IAtomContainer mols : products){
                    set.addAtomContainer(mols);
                }
            }
        }
        if (set.getAtomContainerCount() == 0){
            return molecules;
        }
        else{
            return step(set, react, transformations);
        }
    }

}
