import Parser.CnfParser;

public class main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        CnfParser cnfParser = CnfParser.getInstance();
        
        cnfParser.parseCnf("");
    }
}
