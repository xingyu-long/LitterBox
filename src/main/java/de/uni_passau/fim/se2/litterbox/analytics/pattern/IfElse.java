package de.uni_passau.fim.se2.litterbox.analytics.pattern;

import java.util.ArrayList;
import java.util.List;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.analytics.IssueType;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.BlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.NonDataBlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.IfElseStmt;

public class IfElse extends AbstractIssueFinder {

    // TODO: use this NAME for now, we will define our type later.
    private String NAME="if_else";

    @Override
    public void visit(Script node) {
        visitChildren(node);
    }

    @Override
    public void visit(IfElseStmt node) {
        // If we visit the IfElseStmt, then we will add into issue for now.
        BlockMetadata block = node.getMetadata();
        NonDataBlockMetadata blockData = (NonDataBlockMetadata) block;
        addBlock(blockData.getBlockId());
        
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
