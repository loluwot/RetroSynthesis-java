import org.openscience.cdk.Mapping;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.Mappings;
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
            String smarts = input.next();
            SmartsPattern pattern = SmartsPattern.create(smarts);
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
                Mappings arr = entry1.getValue().matchAll(m);
                ArrayList<Integer> arrList = new ArrayList<Integer>();
                for (int [] i : arr.uniqueAtoms()){
                    for (int j : i){
                        arrList.add(j);
                    }
                }
                functionals.put(entry1.getKey(), arrList);
            }
        }
        return functionals;
    }
}
