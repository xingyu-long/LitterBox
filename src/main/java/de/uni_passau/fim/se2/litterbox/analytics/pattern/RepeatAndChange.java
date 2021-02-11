package de.uni_passau.fim.se2.litterbox.analytics.pattern;

import java.util.List;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.analytics.IssueType;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.BlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.NonDataBlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.actorlook.ChangeGraphicEffectBy;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.RepeatTimesStmt;

public class RepeatAndChange extends AbstractIssueFinder {
    private String NAME = "repeat_and_change";

    @Override
    public void visit(RepeatTimesStmt node) {
        BlockMetadata block = node.getMetadata();
        NonDataBlockMetadata blockData = (NonDataBlockMetadata) block;
        List<Stmt> stmts = node.getStmtList().getStmts();
        for (int i = 0; i < stmts.size(); i++) {
            if (stmts.get(i) instanceof ChangeGraphicEffectBy) {
                addBlock(blockData.getBlockId());
            }
        }
        visitChildren(node);
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

