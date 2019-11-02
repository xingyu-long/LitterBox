package scratch.newast.model.expression;

public class And implements BoolExpr {
    private BoolExpr first;
    private BoolExpr second;

    public And(BoolExpr first, BoolExpr second) {
        this.first = first;
        this.second = second;
    }

    public BoolExpr getFirst() {
        return first;
    }

    public void setFirst(BoolExpr first) {
        this.first = first;
    }

    public BoolExpr getSecond() {
        return second;
    }

    public void setSecond(BoolExpr second) {
        this.second = second;
    }
}