/*
 * Copyright (C) 2019 LitterBox contributors
 *
 * This file is part of LitterBox.
 *
 * LitterBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * LitterBox is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LitterBox. If not, see <http://www.gnu.org/licenses/>.
 */
package scratch.ast.model.statement;

import scratch.ast.model.AbstractNode;
import scratch.ast.model.expression.list.ExpressionList;
import scratch.ast.model.variable.Identifier;
import scratch.ast.visitor.ScratchVisitor;
import scratch.utils.Preconditions;

public class CallStmt extends AbstractNode implements Stmt {

    private final Identifier ident;
    private final ExpressionList expressions;

    public CallStmt(Identifier ident, ExpressionList expressions) {
        super(ident, expressions);
        this.ident = Preconditions.checkNotNull(ident);
        this.expressions = Preconditions.checkNotNull(expressions);
    }

    @Override
    public void accept(ScratchVisitor visitor) {
        visitor.visit(this);
    }

    public Identifier getIdent() {
        return ident;
    }

    public ExpressionList getExpressions() {
        return expressions;
    }
}