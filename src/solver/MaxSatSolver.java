package solver;

import common.Clause;
import common.Literal;
import generator.CnfGenerator;
import java.util.ArrayList;
import parser.CnfParser;

public class MaxSatSolver {

    private static MaxSatSolver instance = null; // Singleton instance

    private static int maxClausesSatisfied;
    private static ArrayList<Clause> clausesSatisfied;

    private MaxSatSolver() {
        maxClausesSatisfied = 0;
        clausesSatisfied = new ArrayList<Clause>();
    }

    public static MaxSatSolver getInstance() {

        if (instance == null)
            instance = new MaxSatSolver();

        return instance;
    }

    public static void solve(String cnf) {
        CnfParser parser = CnfParser.getInstance();
        parser.parseCnf(cnf);

        ArrayList<ArrayList<Literal>> parsedInformation = parser.getParsedInformation();
        ArrayList<Literal> literals = parser.getAllLiterals();

    }

}