import org.openscience.cdk.smarts.SmartsPattern;

public class Functional {
    public static SmartsPattern acylHalide = SmartsPattern.create("[CX3](=[OX1])[F,Cl,Br,I]");
    public static SmartsPattern aldehyde = SmartsPattern.create("[CX3H1](=O)[#6]");
    public static SmartsPattern anhydride = SmartsPattern.create("[CX3](=[OX1])[OX2][CX3](=[OX1])");
    public static SmartsPattern amide = SmartsPattern.create("[NX3][CX3](=[OX1])[#6]");
    public static SmartsPattern carboxylic = SmartsPattern.create("[CX3](=O)[OX2H1]");
    public static SmartsPattern ester = SmartsPattern.create("[#6][CX3](=O)[OX2H0][#6]");
    public static SmartsPattern ketone = SmartsPattern.create("[#6][CX3](=O)[#6]");
    public static SmartsPattern ether = SmartsPattern.create("[OD2]([#6])[#6]");
    public static SmartsPattern enamine = SmartsPattern.create("[NX3][CX3]=[CX3]");
    public static SmartsPattern primAmine = SmartsPattern.create("[NX3;H2;!$(NC=[!#6]);!$(NC#[!#6])][#6]");
    public static SmartsPattern secAmine = SmartsPattern.create("[NX3;H2,H1;!$(NC=O)].[NX3;H2,H1;!$(NC=O)]");

}
