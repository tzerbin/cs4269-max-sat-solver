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
    private static ArrayList<Integer> allProps;

    private MaxSatSolver() {
        maxClausesSatisfied = 0;
        clausesSatisfied = new ArrayList<Clause>();
        truthAssignment = new HashMap<Integer, Boolean>();
        allProps = new ArrayList<Integer>();
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

    public static void maxSat(String cnf, int method) {
        maxClausesSatisfied = 0;
        truthAssignment = new HashMap<Integer, Boolean>();

        CnfParser parser = CnfParser.getInstance();
        parser.parseCnf(cnf);

        ArrayList<Clause> parsedInformation = parser.getParsedInformation();
        allProps = parser.getAllPropositions();

        if (method == 0) {
            bruteForceSolve(parsedInformation);
        } else if (method == 1) {
            branchAndBoundSolve(parsedInformation, parsedInformation.size());
        }
    }

    private static int branchAndBoundSolve(ArrayList<Clause> clauses, int ub) {

        int numEmpty = emptyClauses(clauses);
        // Formula is empty or all clauses are empty
        if (clauses.size() == 0 || clauses.size() == numEmpty) {
            return numEmpty;
        }

        int lb = lowerBound(clauses);

        if (lb >= ub) {
            return clauses.size() + 1;
        }
        if (lb == ub-1) {
            clauses = unitProp(clauses);
        }

        int var = selectVariable();
        ub = Math.min(ub, branchAndBoundSolve(setVariable(clauses, var, true), ub));
        return Math.min(ub, branchAndBoundSolve(setVariable(clauses, var, false), ub));
    }

    private static ArrayList<Clause> unitProp(ArrayList<Clause> clauses) {

        ArrayList<Clause> unitClauses = new ArrayList<>();

        for (Clause clause : clauses) {
            if (clause.getLength() == 1) {
                unitClauses.add(clause);
            }
        }

        while (unitClauses.size() > 0 && clauses.size() > 0) {
            Literal lit = unitClauses.get(0).getLiteralAt(0);
            clauses = setVariable(clauses, lit.proposition, lit.isNeg);
            unitClauses.remove(0);
        }

        return clauses;
    }

    private static int emptyClauses(ArrayList<Clause> clauses) {
        int count = 0;
        for (Clause clause : clauses) {
            if (clause.getLength() == 0) {
                count++;
            }
        }
        return count;
    }

    private static int lowerBound(ArrayList<Clause> clauses) {
        //TODO
        return 0;
    }

    private static int selectVariable() {
        //TODO: choose a heuristic to select a variable from allProp
        return 0;
    }

    private static ArrayList<Clause> setVariable(ArrayList<Clause> clauses, int var, boolean isNeg) {
        ArrayList<Clause> newClauses = new ArrayList<>();

        for (Clause clause : clauses){
            Clause currClause = new Clause();
            boolean isSat = false;
            for (int i = 0; i < clause.getLength(); i++) {
                Literal lit = clause.getLiteralAt(i);

                if (lit.proposition == var) {
                    if (!lit.isNeg && !isNeg || lit.isNeg && isNeg) {
                        // literal evaluates to true and satisfies the clause
                        isSat = true;
                        break;
                    }
                } else {
                    currClause.addLiteral(lit);
                }
            }
            if (!isSat) {
                newClauses.add(currClause);
            }
        }
        return newClauses;
    }

    private static void bruteForceSolve(ArrayList<Clause> parsedInformation) {

        HashMap<Integer, Boolean> assignment = initTruthAssignment();

        int iterations = (int)Math.pow(2, allProps.size());

        for (int i = 0; i < iterations; i++) {
            int maxCount = 0;
            HashMap<Integer, Boolean> currAssignment = getTruthAssignment(i, assignment);
            ArrayList<Clause> potentialSatisfiedClauses = new ArrayList<Clause>();

            //check satisfiability of each clause
            for (int j = 0; j < parsedInformation.size(); j++) {
                if (isClauseSat(parsedInformation.get(j), currAssignment)) {
                    maxCount++;
                    potentialSatisfiedClauses.add(parsedInformation.get(j));
                }
            }
            if (maxCount > maxClausesSatisfied) {
                maxClausesSatisfied = maxCount;
                truthAssignment.putAll(currAssignment);
                clausesSatisfied = potentialSatisfiedClauses;
            }
        }
    }

    private static HashMap<Integer, Boolean> initTruthAssignment() {
        HashMap<Integer, Boolean> assignment = new HashMap<Integer, Boolean>();

        for (int i = 0; i < allProps.size(); i++) {
            assignment.put(allProps.get(i), false);
        }
        return assignment;
    }

    private static HashMap<Integer, Boolean> getTruthAssignment(int iteration, HashMap<Integer, Boolean> assignment) {

        for (int i = 0; i < allProps.size(); i++) {
            int n = allProps.get(i);
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

    private static boolean isClauseSat(Clause clause, HashMap<Integer, Boolean> assignment) {
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

    public static void printClausesSat() {
        System.out.print("Satisfied clauses: ");
        for (int i = 0; i < clausesSatisfied.size(); i ++) {
            System.out.print(clausesSatisfied.get(i));
            if (i < clausesSatisfied.size() - 1)
                System.out.print(", ");
        }


        System.out.println();
    }

}