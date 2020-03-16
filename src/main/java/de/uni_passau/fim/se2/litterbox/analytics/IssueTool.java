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
package de.uni_passau.fim.se2.litterbox.analytics;

import static de.uni_passau.fim.se2.litterbox.utils.GroupConstants.*;


import de.uni_passau.fim.se2.litterbox.analytics.bugpattern.*;
import de.uni_passau.fim.se2.litterbox.analytics.ctscore.FlowControl;
import de.uni_passau.fim.se2.litterbox.analytics.smells.*;
import de.uni_passau.fim.se2.litterbox.analytics.utils.*;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.utils.CSVWriter;
import java.io.IOException;
import java.util.*;
import org.apache.commons.csv.CSVPrinter;

/**
 * Holds all IssueFinder and executes them.
 * Register new implemented checks here.
 */
public class IssueTool {

    private Map<String, IssueFinder> utilFinder = new HashMap<>();
    private Map<String, IssueFinder> bugFinder = new HashMap<>();
    private Map<String, IssueFinder> smellFinder = new HashMap<>();
    private Map<String, IssueFinder> ctScoreFinder = new HashMap<>();

    public IssueTool() {
        bugFinder.put(MissingPenUp.SHORT_NAME, new MissingPenUp());
        bugFinder.put(AmbiguousParameterName.SHORT_NAME, new AmbiguousParameterName());
        bugFinder.put(AmbiguousCustomBlockSignature.SHORT_NAME, new AmbiguousCustomBlockSignature());
        bugFinder.put(MissingPenDown.SHORT_NAME, new MissingPenDown());
        bugFinder.put(MissingEraseAll.SHORT_NAME, new MissingEraseAll());
        bugFinder.put(NoWorkingScripts.SHORT_NAME, new NoWorkingScripts());
        bugFinder.put(MissingCloneInitialization.SHORT_NAME, new MissingCloneInitialization());
        bugFinder.put(MissingCloneCall.SHORT_NAME, new MissingCloneCall());
        bugFinder.put(OrphanedParameter.SHORT_NAME, new OrphanedParameter());
        bugFinder.put(ParameterOutOfScope.SHORT_NAME, new ParameterOutOfScope());
        bugFinder.put(IllegalParameterRefactor.SHORT_NAME, new IllegalParameterRefactor());
        bugFinder.put(CustomBlockWithForever.SHORT_NAME, new CustomBlockWithForever());
        bugFinder.put(CustomBlockWithTermination.SHORT_NAME, new CustomBlockWithTermination());
        bugFinder.put(ForeverInsideLoop.SHORT_NAME, new ForeverInsideLoop());
        bugFinder.put(PositionEqualsCheck.SHORT_NAME, new PositionEqualsCheck());
        bugFinder.put(CallWithoutDefinition.SHORT_NAME, new CallWithoutDefinition());
        bugFinder.put(MessageNeverReceived.SHORT_NAME, new MessageNeverReceived());
        bugFinder.put(MessageNeverSent.SHORT_NAME, new MessageNeverSent());
        bugFinder.put(EndlessRecursion.SHORT_NAME, new EndlessRecursion());
//        finder.put("glblstrt", new GlobalStartingPoint());
//        finder.put("strt", new StartingPoint());
        bugFinder.put(StutteringMovement.SHORT_NAME, new StutteringMovement());
//        finder.put("dblif", new DoubleIf());
        bugFinder.put(MissingLoopSensing.SHORT_NAME, new MissingLoopSensing());
        bugFinder.put(MissingTerminationCondition.SHORT_NAME, new MissingTerminationCondition());
        bugFinder.put(ExpressionAsTouchingOrColor.SHORT_NAME, new ExpressionAsTouchingOrColor());
        bugFinder.put(RecursiveCloning.SHORT_NAME, new RecursiveCloning());
        bugFinder.put(SameVariableDifferentSprite.SHORT_NAME, new SameVariableDifferentSprite());
        bugFinder.put(MissingBackdropSwitch.SHORT_NAME, new MissingBackdropSwitch());
        bugFinder.put(ComparingLiterals.SHORT_NAME, new ComparingLiterals());
        bugFinder.put(MissingWaitUntilCondition.SHORT_NAME, new MissingWaitUntilCondition());
        smellFinder.put(DeadCode.SHORT_NAME, new DeadCode());
//        finder.put("attrmod", new AttributeModification());

//        finder.put("squact", new SequentialActions());
//        finder.put("sprtname", new SpriteNaming());
        smellFinder.put(LongScript.SHORT_NAME, new LongScript());
        smellFinder.put(NestedLoops.SHORT_NAME, new NestedLoops());
        smellFinder.put(UnusedVariable.SHORT_NAME, new UnusedVariable());
        smellFinder.put(UnusedProcedure.SHORT_NAME, new UnusedProcedure());
//        finder.put("dplscrpt", new DuplicatedScript());
//        finder.put("racecnd", new RaceCondition());
        smellFinder.put(EmptyControlBody.SHORT_NAME, new EmptyControlBody());
        smellFinder.put(EmptyScript.SHORT_NAME, new EmptyScript());
        smellFinder.put(EmptySprite.SHORT_NAME, new EmptySprite());
        smellFinder.put(EmptyProject.SHORT_NAME, new EmptyProject());
        smellFinder.put(EmptyProcedure.SHORT_NAME, new EmptyProcedure());
//        finder.put("mdlman", new MiddleMan());
//        finder.put("vrblscp", new VariableScope());
//        finder.put("dplsprt", new DuplicatedSprite());
//        finder.put("inappint", new InappropriateIntimacy());

        //UtilFinder
        utilFinder.put(BlockCount.SHORT_NAME, new BlockCount());
        utilFinder.put(SpriteCount.SHORT_NAME, new SpriteCount());
        utilFinder.put(ProcedureCount.SHORT_NAME, new ProcedureCount());
        utilFinder.put(WeightedMethodCount.SHORT_NAME, new WeightedMethodCount());
        utilFinder.put(ProgramUsingPen.SHORT_NAME, new ProgramUsingPen());
//
//        // To evaluate the CT score
//        finder.put("logthink", new LogicalThinking());
//        finder.put("abstr", new Abstraction());
//        finder.put("para", new Parallelism());
//        finder.put("synch", new Synchronization());
        ctScoreFinder.put(FlowControl.SHORT_NAME, new FlowControl());
//        finder.put("userint", new UserInteractivity());
//        finder.put("datarep", new DataRepresentation());
    }

    /**
     * Executes all checks. Only creates console output for a single project.
     *
     * @param program the project to check
     */
    public void checkRaw(Program program, String dtctrs) {
        String[] detectors;
        switch (dtctrs) {
            case ALL:
                detectors = getAllFinder().keySet().toArray(new String[0]);
                break;
            case BUGS:
                detectors = getBugFinder().keySet().toArray(new String[0]);
                break;
            case SMELLS:
                detectors = getSmellFinder().keySet().toArray(new String[0]);
                break;
            case CTSCORE:
                detectors = getCTScoreFinder().keySet().toArray(new String[0]);
                break;
            default:
                detectors = dtctrs.split(",");
                break;
        }
        for (String s : detectors) {
            if (getAllFinder().containsKey(s)) {
                IssueFinder iF = getAllFinder().get(s);
                if (program != null) {
                    IssueReport issueReport = iF.check(program);
                    System.out.println(issueReport);
                }
            }
        }
    }

    /**
     * Executes all checks
     *
     * @param program the project to check
     */
    public void check(Program program, CSVPrinter printer, String dtctrs) {
        List<IssueReport> issueReports = new ArrayList<>();
        String[] detectors;
        switch (dtctrs) {
            case ALL:
                detectors = getAllFinder().keySet().toArray(new String[0]);
                break;
            case BUGS:
                detectors = getBugFinder().keySet().toArray(new String[0]);
                break;
            case SMELLS:
                detectors = getSmellFinder().keySet().toArray(new String[0]);
                break;
            case CTSCORE:
                detectors = getCTScoreFinder().keySet().toArray(new String[0]);
                break;
            default:
                detectors = dtctrs.split(",");
                break;
        }
        for (String s : detectors) {
            if (getAllFinder().containsKey(s)) {
                IssueFinder iF = getAllFinder().get(s);
                if (program != null) {
                    IssueReport issueReport = iF.check(program);
                    issueReports.add(issueReport);
                    //System.out.println(issueReport);
                } else {
                    return;
                }
            }
        }
        try {
            CSVWriter.addData(printer, issueReports, program);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, IssueFinder> getAllFinder() {
        Map<String, IssueFinder> returnMap = new HashMap<>(smellFinder);
        returnMap.putAll(utilFinder);
        returnMap.putAll(bugFinder);
        returnMap.putAll(ctScoreFinder);
        return returnMap;
    }

    public Map<String, IssueFinder> getSmellFinder() {
        Map<String, IssueFinder> returnMap = new HashMap<>(smellFinder);
        returnMap.putAll(utilFinder);
        return returnMap;
    }

    public Map<String, IssueFinder> getBugFinder() {
        Map<String, IssueFinder> returnMap = new HashMap<>(bugFinder);
        returnMap.putAll(utilFinder);
        return returnMap;
    }

    public Map<String, IssueFinder> getCTScoreFinder() {
        Map<String, IssueFinder> returnMap = new HashMap<>(ctScoreFinder);
        returnMap.putAll(utilFinder);
        return returnMap;
    }

    public static List<String> getOnlyUniqueActorList(List<String> foundSpritesWithIssues) {
        Set<String> uniqueSprites = new TreeSet<>(foundSpritesWithIssues);
        return new ArrayList<>(uniqueSprites);

    }
}