package de.uni_passau.fim.se2.litterbox.analytics.pattern;

import java.util.List;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.analytics.IssueType;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.BlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.NonDataBlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.ChangeVariableBy;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.WaitSeconds;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.IfThenStmt;

public class ChangeAndWait extends AbstractIssueFinder {
    private String NAME = "change_and_wait";
    
    @Override
    public void visit(IfThenStmt node) {
        List<Stmt> stmts = node.getThenStmts().getStmts();
        for (int i = 1; i < stmts.size(); i++) {
            if (checkChangeAndWait(stmts, i - 1, i)) {
                addToBlock(stmts, i - 1, i);
            }
        }
        visitChildren(node);
    }

    private boolean checkChangeAndWait(List<Stmt> stmts, int i, int j) {
        return (stmts.get(i) instanceof ChangeVariableBy) && (stmts.get(j) instanceof WaitSeconds);
    }

    private void addToBlock(List<Stmt> stmts, int i, int j) {
        BlockMetadata ChangeBlock = stmts.get(i).getMetadata();
        NonDataBlockMetadata ChangeBlockData = (NonDataBlockMetadata) ChangeBlock;
        addBlock(ChangeBlockData.getBlockId());

        BlockMetadata waitBlock =  stmts.get(j).getMetadata();
        NonDataBlockMetadata waitBlockData = (NonDataBlockMetadata) waitBlock;
        addBlock(waitBlockData.getBlockId());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public IssueType getIssueType() {
        return IssueType.SMELL;
    }
    
}

