class Literal {

    public boolean isNeg;
    public int proposition; // For scalability

    public Literal(boolean isNeg, int proposition) {
        this.isNeg = isNeg;
        this.proposition = proposition;
    }
}
