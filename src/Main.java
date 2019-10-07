import generator.CnfGenerator;
import parser.CnfParser;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        CnfGenerator cnfGenerator = CnfGenerator.getInstance();
        CnfParser cnfParser = CnfParser.getInstance();

        String CNF = cnfGenerator.generateCNF(5, 1, 4, 8);
        System.out.println("CNF: " + CNF);

        cnfParser.parseCnf(CNF);
    }
}
