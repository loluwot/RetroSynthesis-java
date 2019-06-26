import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smarts.SmartsPattern;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Functional {
    HashMap functional = new HashMap<String, SmartsPattern>();
    File file = new File("functionalgroups.txt");


    public Functional() throws FileNotFoundException {
        Scanner input = new Scanner(file);
        while (input.hasNext()){
            String name = input.next();
            SmartsPattern pattern = SmartsPattern.create(input.next());
            functional.put(name.toLowerCase(), pattern);
        }
    }
    public SmartsPattern getPattern(String name){
        return (SmartsPattern)functional.get(name);
    }
    public HashMap<String, ArrayList<Integer>> functionalGroupAnalysis(IAtomContainer m){
        HashMap functionals = new HashMap<String,ArrayList<Integer>>();
        for (Object entry : functional.entrySet()){
            Map.Entry<String, SmartsPattern> entry1 = (Map.Entry<String, SmartsPattern>)entry;
            if (entry1.getValue().matches(m)){
                int [] arr = entry1.getValue().match(m);
                ArrayList<Integer> arrList = new ArrayList<Integer>();
                for (int i : arr){
                    arrList.add(i);
                }
                functionals.put(entry1.getKey(), arrList);
            }
        }
        return functionals;
    }
//    public static SmartsPattern acylHalide = SmartsPattern.create("[CX3](=[OX1])[F,Cl,Br,I]");
//    public static SmartsPattern aldehyde = SmartsPattern.create("[CX3H1](=O)[#6]");
//    public static SmartsPattern anhydride = SmartsPattern.create("[CX3](=[OX1])[OX2][CX3](=[OX1])");
//    public static SmartsPattern amide = SmartsPattern.create("[NX3][CX3](=[OX1])[#6]");
//    public static SmartsPattern carboxylic = SmartsPattern.create("[CX3](=O)[OX2H1]");
//    public static SmartsPattern ester = SmartsPattern.create("[#6][CX3](=O)[OX2H0][#6]");
//    public static SmartsPattern ketone = SmartsPattern.create("[#6][CX3](=O)[#6]");
//    public static SmartsPattern ether = SmartsPattern.create("[OD2]([#6])[#6]");
//    public static SmartsPattern enamine = SmartsPattern.create("[NX3][CX3]=[CX3]");
//    public static SmartsPattern primAmine = SmartsPattern.create("[NX3;H2;!$(NC=[!#6]);!$(NC#[!#6])][#6]");
//    public static SmartsPattern secAmine = SmartsPattern.create("[NX3;H2,H1;!$(NC=O)].[NX3;H2,H1;!$(NC=O)]");
//    public static SmartsPattern azide = SmartsPattern.create("[$(*-[NX2-]-[NX2+]#[NX1]),$(*-[NX2]=[NX2+]=[NX1-])]");

}
