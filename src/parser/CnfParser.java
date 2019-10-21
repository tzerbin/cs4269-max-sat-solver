package parser;
import common.Literal;
import common.Token;

import java.util.ArrayList;
import java.util.Scanner;

public class CnfParser {

    private static CnfParser instance = null; // Singleton instance

    private static ArrayList<ArrayList<Literal>> parsedInformation;
    private static ArrayList<Literal> literalsInformation;

    private CnfParser() {
        parsedInformation = new ArrayList<ArrayList<Literal>>();
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

        String[] clauses = cnf.split(Token.AND_TOKEN);

        int numberOfClauses = 0;
        int numberOfLiterals = 0;

        for (int i = 0; i < clauses.length; i ++) {

            // Add new array to represent clause
            parsedInformation.add(new ArrayList<Literal>());

            // Substring the clause to remove left parenthesis and right parenthesis
            String clause = clauses[i].substring(1, clauses[i].length() - 1);

            String[] literals = clause.split(Token.OR_TOKEN);

            for (int j = 0; j < literals.length; j ++) {

                int value = Integer.parseInt(literals[j]);

                if (value < 0)
                    parsedInformation.get(i).add(new Literal(true, value * -1));
                else
                    parsedInformation.get(i).add(new Literal(false, value));
            }
        }

        generateInformationFromRawData();

        //printParsedInformation();
    }

    public static void printParsedInformation() {

        for (int i = 0; i < parsedInformation.size(); i ++) {
            for (int j = 0; j < parsedInformation.get(i).size(); j ++) {
                System.out.print(parsedInformation.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    public static ArrayList<ArrayList<Literal>> getParsedInformation() {
        return parsedInformation;
    }

    public static ArrayList<Literal> getAllLiterals() {
        return literalsInformation;
    }

    private static void generateInformationFromRawData() {
        generateListOfLiterals();
    }

    private static void generateListOfLiterals() {
        if (parsedInformation == null)
            return;

        literalsInformation = new ArrayList<Literal>();
        for (int i = 0; i < parsedInformation.size(); i ++)
            for (int j = 0; j < parsedInformation.get(i).size(); j ++)
                literalsInformation.add(parsedInformation.get(i).get(j));
    }
}
