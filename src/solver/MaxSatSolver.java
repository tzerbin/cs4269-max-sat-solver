package solver;

import common.Clause;
import common.Literal;
import generator.CnfGenerator;
import java.util.ArrayList;
import java.util.HashMap;

import parser.CnfParser;

public class MaxSatSolver {

    private static MaxSatSolver instance = null; // Singleton instance

    private static int maxClausesSatisfied;
    private static ArrayList<Clause> clausesSatisfied;
    private static HashMap<Integer, Boolean> truthAssignment;

    private MaxSatSolver() {
        maxClausesSatisfied = 0;
        clausesSatisfied = new ArrayList<Clause>();
        truthAssignment = new HashMap<Integer, Boolean>();
    }

    public static MaxSatSolver getInstance() {

        if (instance == null)
            instance = new MaxSatSolver();

        return instance;
    }

    public static int getMaxClausesSatisfied(){
        return maxClausesSatisfied;
    }

    public static HashMap<Integer, Boolean> getTruthAssignment(){
        return truthAssignment;
    }

    public static ArrayList<Clause> getClausesSatisfied(){
        return clausesSatisfied;
    }

    public static void bruteForceSolve(String cnf) {
        CnfParser parser = CnfParser.getInstance();
        parser.parseCnf(cnf);

        ArrayList<Clause> parsedInformation = parser.getParsedInformation();
        ArrayList<Integer> props = parser.getAllPropositions();
        int numProps = props.size();

        HashMap<Integer, Boolean> assignment = initTruthAssignment(props);

        int iterations = (int)Math.pow(2, numProps);

        for (int i = 0; i < iterations; i++) {
            int maxCount = 0;
            HashMap<Integer, Boolean> currAssignment = getTruthAssignment(i, assignment, props);

            //check satisfiability of each clause
            for (int j = 0; j < parsedInformation.size(); j++) {
                if (isClauseSat(parsedInformation.get(j), currAssignment)) {
                    maxCount++;
                }
            }
            if (maxCount > maxClausesSatisfied) {
                maxClausesSatisfied = maxCount;
                truthAssignment.putAll(currAssignment);
            }
        }
    }

    public static HashMap<Integer, Boolean> initTruthAssignment(ArrayList<Integer> props) {
        HashMap<Integer, Boolean> assignment = new HashMap<Integer, Boolean>();

        for (int i = 0; i < props.size(); i++) {
            assignment.put(props.get(i), false);
        }
        return assignment;
    }

    public static HashMap<Integer, Boolean> getTruthAssignment(int iteration, HashMap<Integer, Boolean> assignment, ArrayList<Integer> props) {

        for (int i = 0; i < props.size(); i++) {
            int n = props.get(i);
            if (iteration%2 == 0) {
                assignment.put(n, false);
            } else {
                assignment.put(n, true);
            }
            iteration /= 2;
            if (iteration == 0) {
                break;
            }
        }
        return assignment;
    }

    public static boolean isClauseSat(Clause clause, HashMap<Integer, Boolean> assignment){
        for (int i = 0; i < clause.getLength(); i++) {
            int prop = clause.getLiteralAt(i).proposition;
            boolean isNeg = clause.getLiteralAt(i).isNeg;

            boolean assg = assignment.get(prop);

            if ((assg && !isNeg) || (!assg && isNeg)) {
                return true;
            }
        }
        return false;
    }

}