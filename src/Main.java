import parser.CnfParser;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        CnfParser cnfParser = CnfParser.getInstance();

        cnfParser.parseCnf("");
    }
}
