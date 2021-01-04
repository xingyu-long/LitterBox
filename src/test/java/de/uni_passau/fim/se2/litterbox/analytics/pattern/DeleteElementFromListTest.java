package de.uni_passau.fim.se2.litterbox.analytics.pattern;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;

public class DeleteElementFromListTest implements JsonTest {
    @Test
    public void testEmptyProgram() throws IOException, ParsingException {
        Program empty = getAST("./src/test/fixtures/emptyProject.json");
        DeleteElementFromList parameterName = new DeleteElementFromList();
        Set<Issue> reports = parameterName.check(empty);
        Assertions.assertEquals(0, reports.size());
    }

    @Test
    public void testRemoveElementProgram() throws IOException, ParsingException {
        Program program = getAST("./src/test/fixtures/DeleteElementFromList.json");
        DeleteElementFromList parameterName = new DeleteElementFromList();
        Set<String> blocks= parameterName.findBlocks(program);
        for (String block : blocks) {
            System.out.println(block);
        }
        Assertions.assertEquals(1, blocks.size());
    }

    @Test
    public void testRemoveElementTwoProgram() throws IOException, ParsingException {
        Program program = getAST("./src/test/fixtures/DeleteElementFromListTwo.json");
        DeleteElementFromList parameterName = new DeleteElementFromList();
        Set<String> blocks= parameterName.findBlocks(program);
        for (String block : blocks) {
            System.out.println(block);
        }
        Assertions.assertEquals(2, blocks.size());
    }
}
