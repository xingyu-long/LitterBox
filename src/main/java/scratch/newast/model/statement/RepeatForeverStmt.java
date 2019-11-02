package scratch.newast.model.statement;

import java.util.List;

public class RepeatForeverStmt implements ControlStmt {
    private List<Stmt> stmtList;

    public RepeatForeverStmt(List<Stmt> stmtList) {
        this.stmtList = stmtList;
    }

    public List<Stmt> getStmtList() {
        return stmtList;
    }

    public void setStmtList(List<Stmt> stmtList) {
        this.stmtList = stmtList;
    }
}