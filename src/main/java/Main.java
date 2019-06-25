import org.openscience.cdk.*;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smarts.SmartsPattern;
import org.openscience.cdk.smiles.SmilesParser;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            SmilesParser sp  = new SmilesParser(SilentChemObjectBuilder.getInstance());
            IAtomContainer m = sp.parseSmiles("CC(CCC(Cl)=O)C(O)=O");
            if (Functional.acylHalide.matches(m)){
                System.out.println(Arrays.toString(Functional.acylHalide.match(m)));
                for (int i : Functional.acylHalide.match(m)){

                    IAtom atom = m.getAtom(i);
                    System.out.println(atom.getSymbol());
                }
            }
            if (Functional.carboxylic.matches(m)){
                System.out.println(Arrays.toString(Functional.carboxylic.match(m)));
                for (int i : Functional.carboxylic.match(m)){
                    IAtom atom = m.getAtom(i);
                    System.out.println(atom.getSymbol());
                }
            }
        } catch (InvalidSmilesException e) {
            System.err.println(e.getMessage());
        }

    }
}
