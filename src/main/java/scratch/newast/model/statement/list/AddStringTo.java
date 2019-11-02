package scratch.newast.model.statement.list;

import scratch.newast.model.expression.string.StringExpr;
import scratch.newast.model.variable.Variable;

public class AddStringTo implements ListStmt {
    private StringExpr string;
    private Variable variable;

    public AddStringTo(StringExpr string, Variable variable) {
        this.string = string;
        this.variable = variable;
    }

    public StringExpr getString() {
        return string;
    }

    public void setString(StringExpr string) {
        this.string = string;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
}