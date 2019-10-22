import generator.CnfGenerator;
import parser.CnfParser;
import solver.MaxSatSolver;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        CnfGenerator cnfGenerator = CnfGenerator.getInstance();
        CnfParser cnfParser = CnfParser.getInstance();

        System.out.println("Enter number of clauses, minimum clause length, maximum clause length, and maximum number of propositions:");

        int numClauses = in.nextInt();
        int clauseMinLen = in.nextInt();
        int clauseMaxLen = in.nextInt();
        int maxProp = in.nextInt();

        String CNF = cnfGenerator.generateCNF(numClauses, clauseMinLen, clauseMaxLen, maxProp);
        System.out.println("CNF: " + CNF);

        MaxSatSolver maxSatSolver = MaxSatSolver.getInstance();
        maxSatSolver.bruteForceSolve(CNF);

        System.out.println("Max clauses: " + maxSatSolver.getMaxClausesSatisfied());
        maxSatSolver.printClausesSat();
        System.out.println("Truth assignment: " + maxSatSolver.getTruthAssignment().toString());
    }
}
