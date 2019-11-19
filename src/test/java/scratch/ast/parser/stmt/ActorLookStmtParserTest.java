package scratch.ast.parser.stmt;

import static junit.framework.TestCase.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.truth.Truth;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import scratch.ast.ParsingException;
import scratch.ast.model.ActorDefinition;
import scratch.ast.model.ActorDefinitionList;
import scratch.ast.model.Program;
import scratch.ast.model.Script;
import scratch.ast.model.elementchoice.WithId;
import scratch.ast.model.expression.string.Str;
import scratch.ast.model.statement.Stmt;
import scratch.ast.model.statement.actorlook.AskAndWait;
import scratch.ast.model.statement.actorlook.ClearGraphicEffects;
import scratch.ast.model.statement.actorlook.SwitchBackdrop;
import scratch.ast.model.statement.spritelook.HideVariable;
import scratch.ast.model.statement.spritelook.ShowVariable;
import scratch.ast.model.statement.termination.StopAll;
import scratch.ast.model.variable.Qualified;
import scratch.ast.parser.ProgramParser;

public class ActorLookStmtParserTest {

    private static JsonNode project;

    @BeforeAll
    public static void setup() {
        String path = "src/test/java/scratch/fixtures/actorLookStmts.json";
        File file = new File(path);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            project = objectMapper.readTree(file);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testProgramStructure() {
        try {
            Program program = ProgramParser.parseProgram("ActorLookStmts", project);
            ActorDefinitionList list = program.getActorDefinitionList();
            Truth.assertThat(list.getDefintions().size()).isEqualTo(2);
        } catch (ParsingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testStmtsInSprite() {
        try {
            Program program = ProgramParser.parseProgram("ActorLookStmts", project);
            ActorDefinitionList list = program.getActorDefinitionList();
            ActorDefinition sprite = list.getDefintions().get(1);

            Script script = sprite.getScripts().getScriptList().get(0);
            List<Stmt> listOfStmt = script.getStmtList().getStmts().getListOfStmt();

            Truth.assertThat(listOfStmt.get(0).getClass()).isEqualTo(AskAndWait.class);
            Truth.assertThat(listOfStmt.get(1).getClass()).isEqualTo(SwitchBackdrop.class);
            Truth.assertThat(listOfStmt.get(2).getClass()).isEqualTo(ShowVariable.class);
            Truth.assertThat(listOfStmt.get(3).getClass()).isEqualTo(HideVariable.class);
            Truth.assertThat(listOfStmt.get(4).getClass()).isEqualTo(ShowVariable.class);
            Truth.assertThat(listOfStmt.get(5).getClass()).isEqualTo(HideVariable.class);
            Truth.assertThat(listOfStmt.get(6).getClass()).isEqualTo(ClearGraphicEffects.class);
            Truth.assertThat(script.getStmtList().getTerminationStmt().getClass()).isEqualTo(StopAll.class);

        } catch (ParsingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAskAndWaitStmt() {
        try {
            Program program = ProgramParser.parseProgram("ActorLookStmts", project);
            ActorDefinitionList list = program.getActorDefinitionList();
            ActorDefinition sprite = list.getDefintions().get(1);

            Script script = sprite.getScripts().getScriptList().get(0);
            List<Stmt> listOfStmt = script.getStmtList().getStmts().getListOfStmt();

            Stmt askAndWaitStmt = listOfStmt.get(0);
            Truth.assertThat(askAndWaitStmt.getClass()).isEqualTo(AskAndWait.class);
            Truth.assertThat(((Str) ((AskAndWait) askAndWaitStmt).getQuestion()).getStr())
                .isEqualTo("What's your name?");

        } catch (ParsingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSwitchBackdrop() {
        try {
            Program program = ProgramParser.parseProgram("ActorLookStmts", project);
            ActorDefinitionList list = program.getActorDefinitionList();
            ActorDefinition sprite = list.getDefintions().get(1);

            Script script = sprite.getScripts().getScriptList().get(0);
            List<Stmt> listOfStmt = script.getStmtList().getStmts().getListOfStmt();

            Stmt switchBackropStmt = listOfStmt.get(1);
            Truth.assertThat(switchBackropStmt.getClass()).isEqualTo(SwitchBackdrop.class);
            Truth.assertThat(((WithId) ((SwitchBackdrop) switchBackropStmt).getElementChoice()).getIdent().getValue())
                .isEqualTo(
                    "Baseball 1");

        } catch (ParsingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testShowHideVar() {
        try {
            Program program = ProgramParser.parseProgram("ActorLookStmts", project);
            ActorDefinitionList list = program.getActorDefinitionList();
            ActorDefinition sprite = list.getDefintions().get(1);

            Script script = sprite.getScripts().getScriptList().get(0);
            List<Stmt> listOfStmt = script.getStmtList().getStmts().getListOfStmt();

            Stmt showVariable = listOfStmt.get(2);
            Truth.assertThat(showVariable.getClass()).isEqualTo(ShowVariable.class);
            Truth.assertThat(((Qualified) ((ShowVariable) showVariable).getVariable()).getFirst().getValue())
                .isEqualTo("Stage");
            Truth.assertThat(((Qualified) ((ShowVariable) showVariable).getVariable()).getSecond().getValue())
                .isEqualTo("my variable");

            Stmt hideVariable = listOfStmt.get(3);
            Truth.assertThat(((Qualified) ((HideVariable) hideVariable).getVariable()).getFirst().getValue())
                .isEqualTo("Stage");
            Truth.assertThat(((Qualified) ((HideVariable) hideVariable).getVariable()).getSecond().getValue())
                .isEqualTo("my variable");

        } catch (ParsingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testShowHideList() {
        try {
            Program program = ProgramParser.parseProgram("ActorLookStmts", project);
            ActorDefinitionList list = program.getActorDefinitionList();
            ActorDefinition sprite = list.getDefintions().get(1);

            Script script = sprite.getScripts().getScriptList().get(0);
            List<Stmt> listOfStmt = script.getStmtList().getStmts().getListOfStmt();

            Stmt showVariable = listOfStmt.get(4);
            Truth.assertThat(showVariable.getClass()).isEqualTo(ShowVariable.class);
            Truth.assertThat(((Qualified) ((ShowVariable) showVariable).getVariable()).getFirst().getValue())
                .isEqualTo("Stage");
            Truth.assertThat(((Qualified) ((ShowVariable) showVariable).getVariable()).getSecond().getValue())
                .isEqualTo("List");

            Stmt hideVariable = listOfStmt.get(5);
            Truth.assertThat(((Qualified) ((HideVariable) hideVariable).getVariable()).getFirst().getValue())
                .isEqualTo("Stage");
            Truth.assertThat(((Qualified) ((HideVariable) hideVariable).getVariable()).getSecond().getValue())
                .isEqualTo("List");

        } catch (ParsingException e) {
            e.printStackTrace();
            fail();
        }
    }
}