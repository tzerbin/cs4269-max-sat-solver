package common;

import java.util.ArrayList;

public class Clause {
    private ArrayList<Literal> literals;

    public Clause() {
        literals = new ArrayList<Literal>();
    }

    public void addLiteral(Literal l) {
        literals.add(l);
    }

    public void removeLiteral() { literals.remove(literals.size() - 1); }

    public int getLength() { return literals.size(); }

    public Literal getLiteralAt(int i) { return literals.get(i); }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i < literals.size() - 1; i ++) {
            sb.append(literals.get(i));
            sb.append(Token.OR_TOKEN);
        }

        if (literals.size() > 0)
            sb.append(literals.get(literals.size() - 1));

        sb.append(')');
        return sb.toString();
    }

}
