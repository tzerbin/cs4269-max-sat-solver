package parser;
import common.Clause;
import common.Literal;
import common.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class CnfParser {

    private static CnfParser instance = null; // Singleton instance

    private static ArrayList<Clause> parsedInformation;
    private static ArrayList<Literal> literalsInformation;
    private static ArrayList<Integer> propositionsInformation;

    private CnfParser() {
        clearData();
    }

    // Retrieves the parser instance
    public static CnfParser getInstance() {

        if (instance == null)
            instance = new CnfParser();

        return instance;
    }

    // Parses a string representing a formula in CNF, and
    // stores the information.
    public static void parseCnf(String cnf) {

        clearData();

        String[] clauses = cnf.split(Token.AND_TOKEN);

        int numberOfClauses = 0;
        int numberOfLiterals = 0;

        for (int i = 0; i < clauses.length; i ++) {

            // Add new array to represent clause
            parsedInformation.add(new Clause());

            // Substring the clause to remove left parenthesis and right parenthesis
            String clause = clauses[i].substring(1, clauses[i].length() - 1);

            String[] literals = clause.split(Token.OR_TOKEN);

            for (int j = 0; j < literals.length; j ++) {

                int value = Integer.parseInt(literals[j]);

                if (value < 0)
                    parsedInformation.get(i).addLiteral(new Literal(true, value * -1));
                else
                    parsedInformation.get(i).addLiteral(new Literal(false, value));
            }
        }

        generateInformationFromRawData();

        //printParsedInformation();
        //printLiteralsInformation();
    }

    public static void printParsedInformation() {

        for (int i = 0; i < parsedInformation.size(); i ++) {
            for (int j = 0; j < parsedInformation.get(i).getLength(); j ++) {
                System.out.print(parsedInformation.get(i).getLiteralAt(j) + " ");
            }
            System.out.println();
        }
    }

    public static void printLiteralsInformation() {
        for (int i = 0; i < literalsInformation.size(); i ++)
            System.out.print(literalsInformation.get(i) + " ");

        System.out.println();
    }

    public static ArrayList<Clause> getParsedInformation() {
        return parsedInformation;
    }

    public static ArrayList<Literal> getAllLiterals() {
        return literalsInformation;
    }

    public static ArrayList<Integer> getAllPropositions() {
        return propositionsInformation;
    }

    private static void generateInformationFromRawData() {
        generateListOfPropositions();
        generateListOfLiterals();
    }

    private static void generateListOfLiterals() {
        if (parsedInformation == null)
            return;

        for (int i = 0; i < parsedInformation.size(); i ++)
            for (int j = 0; j < parsedInformation.get(i).getLength(); j ++)
                literalsInformation.add(parsedInformation.get(i).getLiteralAt(j));
    }

    private static void generateListOfPropositions() {
        if (parsedInformation == null)
            return;

        HashSet<Integer> hm = new HashSet<Integer>();

        for (int i = 0; i < parsedInformation.size(); i ++) {
            for (int j = 0; j < parsedInformation.get(i).getLength(); j++) {
                Integer proposition = parsedInformation.get(i).getLiteralAt(j).proposition;
                if (!hm.contains(proposition)) {
                    propositionsInformation.add(proposition);
                    hm.add(proposition);
                }
            }
        }

        Collections.sort(propositionsInformation);
    }

    private static void clearData() {
        parsedInformation = new ArrayList<Clause>();
        literalsInformation = new ArrayList<Literal>();
        propositionsInformation = new ArrayList<Integer>();
    }
}
