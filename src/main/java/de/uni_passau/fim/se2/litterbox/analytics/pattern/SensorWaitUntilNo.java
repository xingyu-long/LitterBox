package de.uni_passau.fim.se2.litterbox.analytics.pattern;

import java.util.List;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.analytics.IssueType;
import de.uni_passau.fim.se2.litterbox.ast.model.ASTNode;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.bool.Not;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.bool.Touching;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.BlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.NonDataBlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.WaitUntil;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.IfThenStmt;

public class SensorWaitUntilNo extends AbstractIssueFinder {
    private String NAME = "long_script";

    @Override
    public void visit(IfThenStmt node) {
        List<ASTNode> children = (List<ASTNode>) node.getChildren();
        if (children.get(0) instanceof Touching) {
            List<Stmt> stmts =  node.getThenStmts().getStmts();
            for (Stmt stmt : stmts) {
                if (stmt instanceof WaitUntil) {
                    List<ASTNode> childOfStmt =  (List<ASTNode>) stmt.getChildren();
                    if (checkNotTouch(childOfStmt.get(0))) {
                        BlockMetadata block = node.getMetadata();
                        NonDataBlockMetadata blockData = (NonDataBlockMetadata) block;
                        addBlock(blockData.getBlockId());
                        break;
                    }
                }
            }
        }
        visitChildren(node);
    }

    private boolean checkNotTouch(ASTNode node) {
        return node instanceof Not && (ASTNode) node.getChildren().get(0) instanceof Touching;
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

