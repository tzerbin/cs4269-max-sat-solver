package generator;
import java.util.Random;

public class CnfGenerator {
    private static CnfGenerator generator_instance = null;

    private CnfGenerator() {}

    public static CnfGenerator getInstance() {
        if (generator_instance == null) {
            generator_instance = new CnfGenerator();
        }
        return generator_instance;
    }

    public static String generateCNF(int numClauses, int clauseMinLen, int clauseMaxLen, int maxProp) {
        String CNF = "";

        for (int i = 0; i < numClauses; i++) {
            String clause = generateClause(clauseMinLen, clauseMaxLen, maxProp);
            CNF = CNF.concat(clause);

            if (i+1 < numClauses) {
                CNF = CNF.concat("^");
            }
        }
        return CNF;
    }

    private static String generateClause (int clauseMinLen, int clauseMaxLen, int maxProp) {
        String clause = "(";
        Random randomGenerator = new Random();
        int clauseLen = randomGenerator.nextInt(clauseMaxLen) + clauseMinLen;

        for (int i = 0; i < clauseLen; i++) {
            boolean neg = randomGenerator.nextBoolean();
            int prop = randomGenerator.nextInt(maxProp) + 1; //propositions named from 1 to maxProp

            String literal = "";
            if (neg) {
                literal = literal.concat("-");
            }
            literal = literal.concat(String.valueOf(prop));
            clause = clause.concat(literal);
            if (i+1 < clauseLen) {
                clause = clause.concat("v");
            }
        }

        clause = clause.concat(")");

        return clause;
    }
}
