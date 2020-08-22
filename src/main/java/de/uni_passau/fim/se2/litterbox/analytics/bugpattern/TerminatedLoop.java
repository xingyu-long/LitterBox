package de.uni_passau.fim.se2.litterbox.analytics.bugpattern;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.ast.model.StmtList;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.RepeatForeverStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.RepeatTimesStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.UntilStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.termination.StopAll;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.termination.StopThisScript;

import java.util.List;

/**
 * TerminatedLoop is a bug pattern which occurs if a loop contains a StopAll or StopThisScript block which
 * is not guarded by some condition. This can be identified simply looking at the last child in the statement list of a
 * loop and checking if it is a stop block. If that is the case the loop will only execute once and all blocks
 * succeeding the loop (in the case of a "repeat times" or "repeat until") will not be executed either.
 */
public class TerminatedLoop extends AbstractIssueFinder {

    private static final String NAME = "terminated_loop";

    @Override
    public IssueType getIssueType() {
        return IssueType.BUG;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void visit(RepeatForeverStmt node) {
        StmtList children = node.getStmtList();
        checkLoopChildren(children);
        visitChildren(node);
    }

    private void checkLoopChildren(StmtList children) {
        List<Stmt> stmts = children.getStmts();
        Stmt last = stmts.get(stmts.size() - 1);
        if (last instanceof StopAll) {
            addIssue(last, ((StopAll) last).getMetadata());
        } else if (last instanceof StopThisScript) {
            addIssue(last, ((StopThisScript) last).getMetadata());
        }
    }

    @Override
    public void visit(RepeatTimesStmt node) {
        StmtList children = node.getStmtList();
        checkLoopChildren(children);
        visitChildren(node);
    }

    @Override
    public void visit(UntilStmt node) {
        StmtList children = node.getStmtList();
        checkLoopChildren(children);
        visitChildren(node);
    }
}