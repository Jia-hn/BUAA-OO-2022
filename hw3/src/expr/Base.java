package expr;

public interface Base {
    Base deepCopy();

    Expr simplify();

    Expr optimize1();

    Base optimize2();

    Base optimize3();

    String toString();
}
