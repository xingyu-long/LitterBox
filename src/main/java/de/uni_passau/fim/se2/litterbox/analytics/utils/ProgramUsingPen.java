/*
 * Copyright (C) 2020 LitterBox contributors
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
package de.uni_passau.fim.se2.litterbox.analytics.utils;

import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.analytics.IssueFinder;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.pen.*;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ProgramUsingPen implements IssueFinder, ScratchVisitor {
    public static final String NAME = "using_pen";
    public static final String SHORT_NAME = "usingPen";
    private boolean found = false;
    private List<String> actorNames = new LinkedList<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Set<Issue> check(Program program) {
        Preconditions.checkNotNull(program);
        found = false;
        actorNames = new LinkedList<>();
        program.accept(this);
        int count = 0;
        if (found) {
            count = 1;
        }
        // TODO: This is not an issue.
        return Collections.emptySet();

        // return new IssueReport(NAME, count, actorNames, "");
    }

    @Override
    public void visit(PenDownStmt node) {
        found = true;
    }

    @Override
    public void visit(PenUpStmt node) {
        found = true;
    }

    @Override
    public void visit(PenClearStmt node) {
        found = true;
    }

    @Override
    public void visit(SetPenSizeTo node) {
        found = true;
    }

    @Override
    public void visit(ChangePenSizeBy node) {
        found = true;
    }

    @Override
    public void visit(SetPenColorToColorStmt node) {
        found = true;
    }

    @Override
    public void visit(PenStampStmt node) {
        found = true;
    }

    @Override
    public void visit(ChangePenColorParamBy node) {
        found = true;
    }

    @Override
    public void visit(SetPenColorParamTo node) {
        found = true;
    }
}
