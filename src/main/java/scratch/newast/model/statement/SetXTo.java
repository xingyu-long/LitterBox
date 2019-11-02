package scratch.newast.model.statement;

import scratch.newast.model.expression.NumExpr;

public class SetXTo implements SpriteMotionStmt {
    private NumExpr num;

    public SetXTo(NumExpr num) {
        this.num = num;
    }

    public NumExpr getNum() {
        return num;
    }

    public void setNum(NumExpr num) {
        this.num = num;
    }
}