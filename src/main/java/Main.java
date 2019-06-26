import org.openscience.cdk.*;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smarts.SmartsPattern;
import org.openscience.cdk.smiles.SmilesParser;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
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
        try {
            SmilesParser sp  = new SmilesParser(SilentChemObjectBuilder.getInstance());
            IAtomContainer m = sp.parseSmiles(smiles);
            printMap(fun.functionalGroupAnalysis(m));

        } catch (InvalidSmilesException e) {
            System.err.println(e.getMessage());
        }

    }
}
