package de.uni_passau.fim.se2.litterbox.analytics.pattern;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;

public class ChangeAndCloneTest implements JsonTest {
    @Test
    public void testEmptyProgram() throws IOException, ParsingException {
        Program empty = getAST("./src/test/fixtures/emptyProject.json");
        ChangeAndClone parameterName = new ChangeAndClone();
        Set<Issue> reports = parameterName.check(empty);
        Assertions.assertEquals(0, reports.size());
    }

    @Test
    public void testOneProgram() throws IOException, ParsingException {
        Program program = getAST("./src/test/fixtures/ChangeAndCloneOne.json");
        ChangeAndClone parameterName = new ChangeAndClone();
        Set<String> blocks = parameterName.findBlocks(program);
        for (String block : blocks) {
            System.out.println(block);
        }
        Assertions.assertEquals(2, blocks.size());
    }

    @Test
    public void testThreeProgram() throws IOException, ParsingException {
        Program program = getAST("./src/test/fixtures/ChangeAndCloneThree.json");
        ChangeAndClone parameterName = new ChangeAndClone();
        Set<String> blocks = parameterName.findBlocks(program);
        for (String block : blocks) {
            System.out.println(block);
        }
        Assertions.assertEquals(6, blocks.size());
    }
}
