package common;

import java.util.ArrayList;

public class Clause {
    public ArrayList<Literal> literals;

    public Clause() {
        literals = new ArrayList<Literal>();
    }

    public void addLiteral(Literal l) {
        literals.add(l);
    }
}
