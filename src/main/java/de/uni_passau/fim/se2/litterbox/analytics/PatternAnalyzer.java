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

import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PatternAnalyzer extends Analyzer {

    // private static final Logger log =
    // Logger.getLogger(BugAnalyzer.class.getName());
    // private List<String> detectorNames;
    private List<IssueFinder> issueFinders;
    private Map<String, List<String>> blockWithPattern;
    // private String annotationOutput;
    private boolean ignoreLooseBlocks;

    public PatternAnalyzer(String input, String output, String detectors, boolean ignoreLooseBlocks, boolean delete) {
        super(input, output, delete);
        issueFinders = IssueTool.getFinders(detectors);
        // detectorNames =
        // issueFinders.stream().map(IssueFinder::getName).collect(Collectors.toList());
        this.ignoreLooseBlocks = ignoreLooseBlocks;
        blockWithPattern = new HashMap<>();
    }

    // public void setAnnotationOutput(String annotationOutput) {
    // this.annotationOutput = annotationOutput;
    // }

    /**
     * The method for analyzing one Scratch project file (ZIP). It will produce only
     * console output.
     *
     * @param fileEntry      the file to analyze
     * @param reportFileName the file in which to write the results
     */
    void check(File fileEntry, String reportFileName) {
        Program program = extractProgram(fileEntry);
        if (program == null) {
            // Todo error message
            return;
        }

        runFinders(program);
        if (reportFileName == null || reportFileName.isEmpty()) {
            reportFileName = "BlocksWithPattern.json";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        // TODO: need to rewrite as JSON object.
        // int i = 0;
        // for (String pattern : blockWithPattern.keySet()) {
        // sb.append("\"" + pattern + "\"" + ": ");
        // sb.append("[");
        // int j = 0;
        // for (String block : blockWithPattern.get(pattern)) {
        // sb.append("\"" + block + "\"");
        // if (j != blockWithPattern.get(pattern).size() - 1) {
        // sb.append(", ");
        // }
        // j += 1;
        // }
        // sb.append("]");
        // if (i != blockWithPattern.size() - 1) {
        // sb.append(", ");
        // }
        // i += 1;
        // }
        // sb.append("}");
        // String json = sb.toString();
        // BufferedWriter writer;
        // try {
        // writer = new BufferedWriter(new FileWriter(reportFileName));
        // writer.write(json);
        // writer.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        for (String p : blockWithPattern.keySet()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            ArrayNode array = objectMapper.createArrayNode();
            for (String id : blockWithPattern.get(p)) {
                array.add(id);
            }
            objectNode.set(p, array);
            rootNode.setAll(objectNode);
        }

        try {
            objectMapper.writeValue(new File(reportFileName), rootNode);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runFinders(Program program) {
        Preconditions.checkNotNull(program);
        // Set<String> patterns = new LinkedHashSet<>();
        for (IssueFinder iF : issueFinders) {
            iF.setIgnoreLooseBlocks(ignoreLooseBlocks);
            System.out.println(iF.getName());
            List<String> blocks = iF.findBlocks(program);
            if (!blocks.isEmpty()) {
                blockWithPattern.putIfAbsent(iF.getName(), new LinkedList<>());
                blockWithPattern.get(iF.getName()).addAll(blocks);
            }
            System.out.println(blocks.toString());
        }
    }
}
