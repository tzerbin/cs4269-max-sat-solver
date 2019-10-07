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

    private String generateCNF(int numClauses, int clauseMinLen, int clauseMaxLen, int maxProp) {
        String CNF = "";

        for (int i = 0; i < numClauses; i++) {

        }

        return CNF;
    }

    private String generateClause (int clauseMinLen, int clauseMaxLen, int maxProp) {
        String clause = "(";
        Random randomGenerator = new Random();
        int clauseLen = randomGenerator.nextInt(clauseMaxLen) + clauseMinLen;

        for (int i = 0; i < clauseLen; i++) {
            boolean neg = randomGenerator.nextBoolean();

        }

        return clause;
    }
}
