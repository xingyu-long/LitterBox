package de.uni_passau.fim.se2.litterbox.analytics.pattern;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;

public class ChangeAndWaitTest implements JsonTest {
    @Test
    public void testEmptyProgram() throws IOException, ParsingException {
        Program empty = getAST("./src/test/fixtures/emptyProject.json");
        ChangeAndWait parameterName = new ChangeAndWait();
        Set<Issue> reports = parameterName.check(empty);
        Assertions.assertEquals(0, reports.size());
    }

    @Test
    public void testTwoProgram() throws IOException, ParsingException {
        Program program = getAST("./src/test/fixtures/TestTwo.json");
        ChangeAndWait parameterName = new ChangeAndWait();
        List<String> blocks = parameterName.findBlocks(program);
        for (String block : blocks) {
            System.out.println(block);
        }
        Assertions.assertEquals(4, blocks.size());
    }

    @Test
    public void testTwoInGameProgram() throws IOException, ParsingException {
        // Multiple Sprite.
        Program program = getAST("./src/test/fixtures/ChangeAndWaitTwo.json");
        ChangeAndWait parameterName = new ChangeAndWait();
        List<String> blocks = parameterName.findBlocks(program);
        for (String block : blocks) {
            System.out.println(block);
        }
        Assertions.assertEquals(12, blocks.size());
    }
}
