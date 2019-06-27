import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.isomorphism.Mappings;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smarts.SmartsPattern;
import org.openscience.cdk.smiles.SmiFlavor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKValencyChecker;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.xmlcml.euclid.Int;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.oracle.jrockit.jfr.Transition.To;

public class Reactions {
    Map <String, SmartsPattern> reactionMatch = new HashMap<String, SmartsPattern>();
    ArrayList <Integer> cutPoints = new ArrayList<Integer>();
    ArrayList <IAtomContainer> fGroups = new ArrayList<IAtomContainer>();
    ArrayList <Integer> attachPoints = new ArrayList<Integer>();
    ArrayList <Integer> num = new ArrayList<Integer>();
    File file = new File("reactions.txt");
    public Reactions() throws FileNotFoundException {
        Scanner input = new Scanner(file);
        while (input.hasNextLine()){
            String line = input.nextLine();
            System.out.println(line);
            String [] arr = line.split(" ");
            String name = arr [0];
            String smarts = arr [1];
            int n = Integer.parseInt(arr[2]);
            reactionMatch.put(name,SmartsPattern.create(smarts));
            num.add(n);
            for (int i = 3; i < n+3; i++){
                cutPoints.add(Integer.parseInt(arr[i]));
            }
            for (int i = n+3; i < 3*n+3; i+=2){
                SmilesParser sp  = new SmilesParser(SilentChemObjectBuilder.getInstance());
                try {
                    IAtomContainer molecule = AtomContainerManipulator.removeHydrogens(sp.parseSmiles(arr[i]));
                    fGroups.add(molecule);
                } catch (InvalidSmilesException e) {
                    e.printStackTrace();
                }
            }
            for (int i = n+4; i < 3*n+3; i+=2){
                attachPoints.add(Integer.parseInt(arr[i]));
            }

        }
    }
    public SmartsPattern getPattern(String name){
        return (SmartsPattern)reactionMatch.get(name);
    }

    public SmartsPattern getPatternID(int id){
        System.out.println(reactionMatch.values().size() + "size");
        Map.Entry<String, SmartsPattern>[] arr = new Map.Entry[reactionMatch.values().size()];
        reactionMatch.entrySet().toArray(arr);
        return arr[reactionMatch.values().size()-id-1].getValue();
    }
    public boolean contains(int [] arr, int key){
        for (int i : arr){
            if (i == key){
                return true;
            }
        }
        return false;
    }
    public ArrayList<IAtomContainer> reactionAnalysis(IAtomContainer m) throws CDKException, IOException {
        ArrayList<IAtomContainer> retro = new ArrayList<IAtomContainer>();
        DepictionGenerator dg = new DepictionGenerator().withSize(512, 512)
                .withAtomColors();
       for (int i = 0; i < num.size(); i++){
           System.out.println(num.size() + "size");
           SmartsPattern pattern = getPatternID(i);
           System.out.println(reactionMatch.keySet().toArray()[i] + "smarts");
           int n = num.get(i);
           if (pattern.matches(m)){
               System.out.println(i + "index matched");
               int [] matches = pattern.match(m);
               System.out.println(Arrays.toString(matches));
               for (int j = 0; j < n; j++){
                   System.out.println("index" + (i*n+j));
                   IAtom atom = m.getAtom(matches[cutPoints.get(i*n+j)]);
                   CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(fGroups.get(i*n+j).getBuilder());
                   for (IAtom a : fGroups.get(i*n+j).atoms()) {
                       IAtomType type = matcher.findMatchingAtomType(fGroups.get(i*n+j), a);
                       AtomTypeManipulator.configure(a, type);
                   }
                   IAtom atom2 = fGroups.get(i*n+j).getAtom(attachPoints.get(i*n+j));
                   Iterator iter2 = fGroups.get(i*n+j).getConnectedBondsList(atom2).iterator();
                   while (iter2.hasNext()){
                       IBond bond = (IBond) iter2.next();
                       System.out.println(bond.getConnectedAtom(atom2).getSymbol() + "SSS");
                       if (bond.getConnectedAtom(atom2).getAtomicNumber() == 1){
                           m.removeBond(bond);
                           break;
                       }
                   }
                   System.out.println(atom.getSymbol());
                   System.out.println(atom2.getSymbol());
                   Iterator iter = m.bonds().iterator();
                   while (iter.hasNext()) {
                       IBond bond = (IBond) iter.next();
                       if (bond.getConnectedAtom(atom) != null && contains(matches,bond.getConnectedAtom(atom).getIndex())){
                           m.removeBond(bond);
                           break;
                       }
                   }
                    dg.depict(m).writeTo("pre" +j + ".png");
                   m.add(fGroups.get(i*n+j));
                   dg.depict(m).writeTo("post" +j + ".png");
                   IBond bon = new Bond(atom, atom2);
                   m.addBond(bon);


                   dg.depict(m).writeTo("past" +j + ".png");
                    IAtomContainer reactant = new AtomContainer();
                   IAtomContainerSet set = ConnectivityChecker.partitionIntoMolecules(m);
                   int fragmentCount = set.getAtomContainerCount();

                   System.out.println(fragmentCount);
                   atom2.setImplicitHydrogenCount(atom2.getImplicitHydrogenCount()-1);
                   dg.depict(m).writeTo("mol.png");
                   SmilesGenerator sg = new SmilesGenerator(SmiFlavor.Generic);
                   System.out.println(atom2.getBondCount()+atom2.getImplicitHydrogenCount() + "count" + " " + atom2.getCharge());

                    for (IAtomContainer atomContainer : set.atomContainers()){
                        if (atomContainer.contains(atom)){
                            retro.add(atomContainer);
                        }
                    }

               }
               break;
           }
       }
       for (int i = 0; i < retro.size(); i++){
           dg.depict(retro.get(i)).writeTo("retro" + i+".png");
       }
       return retro;
    }
    public ArrayList<Integer> idFinder(String smarts, IAtomContainer m){
        SmartsPattern s = SmartsPattern.create(smarts);
        ArrayList<Integer> default1 = new ArrayList<Integer>();
        if (s.matches(m)){
            System.out.println("AAAAAA");
            int [] matches = s.match(m);
            for (int i = 0; i < matches.length; i++){
                IAtom atom = m.getAtom(matches[i]);
                if (atom.isAromatic()){
                    default1.add(i);
                }
            }
        }
        return default1;
    }
}
