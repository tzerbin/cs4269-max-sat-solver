package parser;

public class CnfParser {

    private static CnfParser instance = null; // Singleton instance

    private CnfParser() {

    }

    // Retrieves the parser instance
    public static CnfParser getInstance() {

        if (instance == null)
            instance = new CnfParser();

        return instance;
    }

    // Parses a string representing a formula in CNF,
    // and store the information
    public static void parseCnf(String cnf) {
        System.out.println("YES");
    }
}
