package de.uni_passau.fim.se2.litterbox.analytics.pattern;

import java.util.List;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.analytics.IssueType;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.BlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.CloneOfMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.NonDataBlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.ChangeVariableBy;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.CreateCloneOf;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.RepeatTimesStmt;

public class ChangeAndClone extends AbstractIssueFinder {
    private String NAME = "long_script";

    @Override
    public void visit(Script node) {
        List<Stmt> stmts = node.getStmtList().getStmts();
        for (int i = 1; i < stmts.size(); i++) {
            if (checkChangeAndClone(stmts, i - 1, i)) {
                addToBlock(stmts, i - 1, i);
            }
        }

        visitChildren(node);
    }

    @Override
    public void visit(RepeatTimesStmt node) {
        List<Stmt> stmts = node.getStmtList().getStmts();
        for (int i = 1; i < stmts.size(); i++) {
            if (checkChangeAndClone(stmts, i - 1, i)) {
                addToBlock(stmts, i - 1, i);
            }
        }

        visitChildren(node);
    }

    private void addToBlock(List<Stmt> stmts, int i, int j) {
        BlockMetadata ChangeBlock = stmts.get(i).getMetadata();
        NonDataBlockMetadata ChangeBlockData = (NonDataBlockMetadata) ChangeBlock;
        addBlock(ChangeBlockData.getBlockId());

        CloneOfMetadata clone = (CloneOfMetadata) stmts.get(j).getMetadata();
        BlockMetadata cloneBlock = clone.getCloneBlockMetadata();
        NonDataBlockMetadata cloneBlockData = (NonDataBlockMetadata) cloneBlock;
        addBlock(cloneBlockData.getBlockId());
    }

    private boolean checkChangeAndClone(List<Stmt> stmts, int i, int j) {
        return (stmts.get(i) instanceof ChangeVariableBy) && (stmts.get(j) instanceof CreateCloneOf);
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
