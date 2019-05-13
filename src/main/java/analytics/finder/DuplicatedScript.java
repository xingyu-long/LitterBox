package analytics.finder;

import analytics.Issue;
import analytics.IssueFinder;
import scratch.data.Script;
import scratch.structure.Project;
import scratch.structure.Scriptable;
import utils.Version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Checks for duplicated scripts. Only uses full String representation comparison.
 */
public class DuplicatedScript implements IssueFinder {

    @Override
    public Issue check(Project project) {
        List<Scriptable> scriptables = new ArrayList<>();
        scriptables.add(project.getStage());
        scriptables.addAll(project.getSprites());
        int count;
        List<String> pos = new ArrayList<>();
        List<String> duplicated = new ArrayList<>();
        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                if (script != null) {
                    if (script.getBlocks().size() > 1) {
                        searchBlocks(scriptables, scable, script, pos, duplicated);
                    }
                }
            }
        }
        count = pos.size();
        String notes = "No duplicated code found.";
        if (count > 0) {
            notes = "Some scripts have duplicated code.";
        }

        String name = "duplicated_script";
        return new Issue(name, count, pos, project.getPath(), notes);
    }

    private void searchBlocks(List<Scriptable> scriptables, Scriptable currentSc, Script sc, List<String> pos, List<String> duplicated) {
        String toSearch = sc.getBlocks().toString();
        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                if (script != null) {
                    if (script.getBlocks().size() > 1) {
                        if (script.getBlocks().toString().equals(toSearch) && script.getPosition() != sc.getPosition() && !duplicated.contains(toSearch)) {
                            pos.add(currentSc.getName() + " and " + scable.getName() + " at " + Arrays.toString(sc.getPosition()) + " and " + Arrays.toString(script.getPosition()));
                            duplicated.add(toSearch);
                        }
                    }
                }
            }
        }
    }
}
