package common;

public class Literal {
    public boolean isNeg;
    public int proposition; // For scalability, starts from 1

    public Literal(boolean isNeg, int proposition) {
        this.isNeg = isNeg;
        this.proposition = proposition;
    }

    @Override
    public String toString() {
        return isNeg? Integer.toString(-proposition) : Integer.toString(proposition);
    }
}
