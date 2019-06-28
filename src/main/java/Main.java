import org.openscience.cdk.*;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.qsar.descriptors.atomic.PiElectronegativityDescriptor;
import org.openscience.cdk.qsar.descriptors.atomic.SigmaElectronegativityDescriptor;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smarts.SmartsPattern;
import org.openscience.cdk.smiles.SmiFlavor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove();
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.println("Insert smiles of compound to be analyzed here: ");
        String smiles = input.nextLine();
        Functional fun = new Functional();
        Reactions react = new Reactions();
        Transformations transformations = new Transformations();
        try {
            SmilesParser sp  = new SmilesParser(SilentChemObjectBuilder.getInstance());
            IAtomContainer m = sp.parseSmiles(smiles);
            CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(m.getBuilder());
            for (IAtom atom : m.atoms()) {
                IAtomType type = matcher.findMatchingAtomType(m, atom);
                AtomTypeManipulator.configure(atom, type);
                //System.out.println(atom.getSymbol());
            }
            SigmaElectronegativityDescriptor electroNeg1 = new SigmaElectronegativityDescriptor();
            PiElectronegativityDescriptor electroNeg2 = new PiElectronegativityDescriptor();
            CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(m.getBuilder());
            adder.addImplicitHydrogens(m);
            SmilesGenerator sg = new SmilesGenerator(SmiFlavor.Generic);
            System.out.println(sg.create(m) + "new smiles");
//            for (int i : fun.functionalGroupAnalysis(m).get("claisencondensation")){
//                IAtom a = m.getAtom(i);
//                System.out.print(a.getSymbol() + " ");
//                if (a.isAromatic()){
//                    System.out.print("bruh");
//                }
//            }
//            for (int i : fun.functionalGroupAnalysis(m).get("enone")){
//                System.out.println(electroNeg1.calculate(m.getAtom(i), m).getValue().toString());
//            }
            //printMap(react.reactionAnalysis(m));
//            transformations.reactionAnalysis(m);
//            IAtomContainer clone1 = m.clone();
//            ArrayList<IAtomContainer> products = react.reactionAnalysis(clone1);
//            if (products.size() == 0){
//                DepictionGenerator dg = new DepictionGenerator().withSize(512, 512)
//                        .withAtomColors();
//                for (int i = 0; i < 2; i++){
//                    System.out.println(i + "_____________________ INDEx");
//                    IAtomContainer copy = m.clone();
//                    IAtomContainer test = transformations.reactionAnalysis(copy, i);
//
//                    if (test == null){
//                        continue;
//                    }
//                    else{
//
//                        AtomContainerManipulator.convertImplicitToExplicitHydrogens(test);
//                        dg.depict(test).writeTo("test.png");
//                        ArrayList<IAtomContainer> products1 = react.reactionAnalysis(test);
//                        System.out.println(products1.size() + "SIZE IMPORTANT");
//                        if (products1.size() > 0){
//                            int j = 0;
//                            for (IAtomContainer container : products1){
//                                dg.depict(container).writeTo("verycool" + j + ".png");
//                                j++;
//                            }
//                        }
//                    }
//                }
//            }
            DepictionGenerator dg = new DepictionGenerator().withSize(512, 512).withAtomColors();
            IAtomContainerSet set2 = new AtomContainerSet();
            set2.addAtomContainer(m);
            int ii = 0;
            for (IAtomContainer con : Retrosynthesis.step(set2, react, transformations).atomContainers()){
                dg.depict(con).writeTo("final" + ii+".png");
                ii++;
            }
            System.out.println(react.idFinder("[#6,#1]C([#6,#1])[O;D1]", m));
        } catch (InvalidSmilesException e) {
            System.err.println(e.getMessage());
        } catch (CDKException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }
}
