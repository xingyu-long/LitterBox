package de.uni_passau.fim.se2.litterbox.analytics.pattern;

import java.util.List;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.analytics.IssueType;
import de.uni_passau.fim.se2.litterbox.ast.model.ASTNode;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.bool.Not;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.BlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.NonDataBlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.UntilStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.list.DeleteOf;

public class DeleteElementFromList extends AbstractIssueFinder {
    private String NAME = "delete_element_from_list";

    @Override
    public void visit(UntilStmt node) {
        List<ASTNode> children = (List<ASTNode>) node.getChildren();
        List<Stmt> stmts = node.getStmtList().getStmts();

        if (children.get(0) instanceof Not && (stmts.get(0) instanceof DeleteOf)) {
            BlockMetadata untilBlock = node.getMetadata();
            NonDataBlockMetadata untilBlockData = (NonDataBlockMetadata) untilBlock;
            addBlock(untilBlockData.getBlockId());
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
