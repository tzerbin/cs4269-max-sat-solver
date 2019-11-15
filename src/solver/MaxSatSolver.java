package solver;

import common.Clause;
import common.Literal;
import generator.CnfGenerator;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.HashSet;
import java.util.LinkedList;
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

    public static void maxSat(String cnf, int method, int maxProp) {
        maxClausesSatisfied = 0;
        truthAssignment = new HashMap<Integer, Boolean>();

        CnfParser parser = CnfParser.getInstance();
        parser.parseCnf(cnf);

        ArrayList<Clause> parsedInformation = parser.getParsedInformation();
        allProps = parser.getAllPropositions();
        int numClauses = parsedInformation.size();

        if (method == 0) {
            bruteForceSolve(parsedInformation);
        } else if (method == 1) {
            maxClausesSatisfied = numClauses - branchAndBoundSolve(parsedInformation, numClauses);
        } else if (method == 2) {
            maxClausesSatisfied = satBasedSolveBinary(parsedInformation, maxProp);
        }
    }

    // https://www.researchgate.net/publication/2950789_Improved_Branch_and_Bound_Algorithms_for_MAX-SAT
    private static int branchAndBoundSolve(ArrayList<Clause> clauses, int ub) {

        int lb = lowerBound(clauses);

        if (lb >= ub) {
            return lb;
        }
        if (lb == ub-1) {
            clauses = unitProp(clauses);
        }

        int numEmpty = emptyClauses(clauses);
        // Formula is empty or all clauses are empty
        if (clauses.size() == 0 || clauses.size() == numEmpty) {
            return numEmpty;
        }

        int var = selectVariable(clauses);

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

        //TODO: can be further improved by implementing underestimation
        return emptyClauses(clauses);
    }

    private static int selectVariable(ArrayList<Clause> clauses) {
        // Weighted Clause Length variable selection heuristic
        int w1 = 1, w2 = 3;
        int max = 0;
        int selectedVar = -1;
        int minClauseLen = -1;
        int minClauseIdx = 0;

        for (int prop : allProps) {
            int unit = 0;
            int binary = 0;
            for (int i = 0; i < clauses.size(); i++) {
                Clause clause = clauses.get(i);
                int len = clause.getLength();
                if (len == 1) {
                    if (clause.getLiteralAt(0).proposition == prop) {
                        unit++;
                    }
                } else if (len == 2) {
                    if (clause.getLiteralAt(0).proposition == prop || clause.getLiteralAt(1).proposition == prop) {
                        binary++;
                    }
                } else if (len > 0) {
                    if (minClauseLen == -1 || len < minClauseLen) {
                        minClauseLen = len;
                        minClauseIdx = i;
                    }
                }
            }
            int score = (w1 * unit) + (w2 * binary);
            if (score > max) {
                max = score;
                selectedVar = prop;
            }
        }

        if (selectedVar == -1) {
            //select arbitrary prop from shortest clause
            return clauses.get(minClauseIdx).getLiteralAt(0).proposition;
        }
        return selectedVar;
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

    private static int satBasedSolveBinary(ArrayList<Clause> clauses, int maxProp) {

        int upperBound = clauses.size();
        int lowerBound = 0;
        boolean selectedClausesSatisfiable = false;

        while (upperBound != lowerBound + 1) {

            int middle = (upperBound + lowerBound) / 2;

            /*
            System.out.println("ALL CLAUSES");
            for (int j = 0; j < clauses.size(); j ++) {
                System.out.println(clauses.get(j).toString());
            }*/

            ArrayList<int[]> combinations = getAllCombinations(clauses.size(), middle);

            // For each combination, select all other clauses, and check if it is satisfiable.
            for (int i = 0; i < combinations.size(); i ++) {

                ArrayList<Clause> selectedClauses = new ArrayList<Clause>();
                HashSet<Integer> clausesIndexToRemove = new HashSet<Integer>();
                selectedClausesSatisfiable = false;

                for (int j = 0; j < middle; j ++) {
                    clausesIndexToRemove.add(combinations.get(i)[j]);
                }

                for (int j = 0; j < clauses.size(); j ++) {
                    if (!clausesIndexToRemove.contains(j))
                        selectedClauses.add(clauses.get(j));
                }

                bruteForceSolve(selectedClauses); // Faulty
                if (maxClausesSatisfied == maxProp - middle) {
                    selectedClausesSatisfiable = true;
                    break;
                }
            }

            if (selectedClausesSatisfiable)
                upperBound = middle;
            else {
                System.out.println("HIT LOWER HERE");
                lowerBound = middle;
            }

        }

        System.out.println("Max clauses: " + upperBound);
        return upperBound;
    }

    private static ArrayList<int[]> getAllCombinations(int n, int r) {
        ArrayList<int[]> combinations = new ArrayList<int[]>();
        int[] combination = new int[r];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r - 1] < n) {
            combinations.add(combination.clone());

            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return combinations;
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