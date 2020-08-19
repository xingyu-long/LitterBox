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
package de.uni_passau.fim.se2.litterbox.analytics;

import de.uni_passau.fim.se2.litterbox.analytics.bugpattern.*;
import de.uni_passau.fim.se2.litterbox.analytics.smells.*;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

import java.util.*;

/**
 * Holds all IssueFinder and executes them.
 * Register new implemented checks here.
 */
public class IssueTool {

    private final Map<String, IssueFinder> bugFinder = new LinkedHashMap<>();
    private final Map<String, IssueFinder> smellFinder = new LinkedHashMap<>();
    private final Map<String, IssueFinder> allFinder;

    public IssueTool() {
        registerBugFinder(new AmbiguousCustomBlockSignature());
        registerBugFinder(new AmbiguousParameterName());
        registerBugFinder(new AmbiguousParameterNameStrict());
        registerBugFinder(new CallWithoutDefinition());
        registerBugFinder(new ComparingLiterals());
        registerBugFinder(new ComparingLiteralsStrict());
        registerBugFinder(new CustomBlockWithForever());
        registerBugFinder(new CustomBlockWithTermination());
        registerBugFinder(new EndlessRecursion());
        registerBugFinder(new ExpressionAsTouchingOrColor());
        registerBugFinder(new ForeverInsideLoop());
        registerBugFinder(new IllegalParameterRefactor());
        registerBugFinder(new MessageNeverReceived());
        registerBugFinder(new MessageNeverSent());
        registerBugFinder(new MissingBackdropSwitch());
        registerBugFinder(new MissingCloneCall());
        registerBugFinder(new MissingCloneInitialization());
        registerBugFinder(new MissingInitialization());
        registerBugFinder(new MissingEraseAll());
        registerBugFinder(new MissingLoopSensing());
        registerBugFinder(new MissingPenDown());
        registerBugFinder(new MissingPenUp());
        registerBugFinder(new MissingTerminationCondition());
        registerBugFinder(new MissingWaitUntilCondition());
        registerBugFinder(new NoWorkingScripts());
        registerBugFinder(new OrphanedParameter());
        registerBugFinder(new ParameterOutOfScope());
        registerBugFinder(new PositionEqualsCheck());
        registerBugFinder(new PositionEqualsCheckStrict());
        registerBugFinder(new RecursiveCloning());
        registerBugFinder(new StutteringMovement());

        // Smells
        registerSmellFinder(new EmptyControlBody());
        registerSmellFinder(new EmptyCustomBlock());
        registerSmellFinder(new EmptyProject());
        registerSmellFinder(new EmptyScript());
        registerSmellFinder(new EmptySprite());
        registerSmellFinder(new DeadCode());
        registerSmellFinder(new LongScript());
        registerSmellFinder(new NestedLoops());
        registerSmellFinder(new SameVariableDifferentSprite());
        registerSmellFinder(new UnusedVariable());
        registerSmellFinder(new UnusedCustomBlock());

        allFinder = new LinkedHashMap<>(bugFinder);
        allFinder.putAll(smellFinder);
    }

    /**
     * Executes all checks.
     *
     * @param program the project to check
     */
    public Set<Issue> check(Program program, String[] detectors) {
        Preconditions.checkNotNull(program);
        Set<Issue> issues = new LinkedHashSet<>();
        for (String s : detectors) {
            if (getAllFinder().containsKey(s)) {
                IssueFinder iF = getAllFinder().get(s);
                issues.addAll(iF.check(program));
            }
        }
        return issues;
    }

    public Map<String, IssueFinder> getAllFinder() {
        Map<String, IssueFinder> returnMap = new HashMap<>(allFinder);
        return returnMap;
    }

    public Map<String, IssueFinder> getSmellFinder() {
        Map<String, IssueFinder> returnMap = new HashMap<>(smellFinder);
        return returnMap;
    }

    public Map<String, IssueFinder> getBugFinder() {
        Map<String, IssueFinder> returnMap = new HashMap<>(bugFinder);
        return returnMap;
    }

    public void registerSmellFinder(IssueFinder finder) {
        if (finder.getIssueType() != IssueFinder.IssueType.SMELL) {
            throw new RuntimeException("Cannot register IssueFinder of Type "
                    + finder.getIssueType()
                    + " as Smell IssueFinder");
        }

        smellFinder.put(finder.getName(), finder);
    }

    public void registerBugFinder(IssueFinder finder) {
        if (finder.getIssueType() != IssueFinder.IssueType.BUG) {
            throw new RuntimeException("Cannot register IssueFinder of Type "
                    + finder.getIssueType()
                    + " as Bug IssueFinder");
        }
        bugFinder.put(finder.getName(), finder);
    }
}
