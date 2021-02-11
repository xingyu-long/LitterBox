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

public class RepeatAndChangeTest implements JsonTest {
    @Test
    public void testEmptyProgram() throws IOException, ParsingException {
        Program empty = getAST("./src/test/fixtures/emptyProject.json");
        RepeatAndChange parameterName = new RepeatAndChange();
        List<String> blocks = parameterName.findBlocks(empty);
        Assertions.assertEquals(0, blocks.size());
    }

    @Test
    public void testProgram() throws IOException, ParsingException {
        Program program = getAST("./src/test/fixtures/RepeatAndChange.json");
        RepeatAndChange parameterName = new RepeatAndChange();
        List<String> blocks = parameterName.findBlocks(program);
        for (String block : blocks) {
            System.out.println(block);
        }
        Assertions.assertEquals(1, blocks.size());
    }

}
