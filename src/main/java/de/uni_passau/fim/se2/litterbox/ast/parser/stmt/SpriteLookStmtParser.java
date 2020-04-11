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
package de.uni_passau.fim.se2.litterbox.ast.parser.stmt;

import com.fasterxml.jackson.databind.JsonNode;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.Expression;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.num.Mult;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.num.NumExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.StringExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.NumberLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritelook.*;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.SpriteLookStmtOpcode;
import de.uni_passau.fim.se2.litterbox.ast.parser.CostumeChoiceParser;
import de.uni_passau.fim.se2.litterbox.ast.parser.NumExprParser;
import de.uni_passau.fim.se2.litterbox.ast.parser.StringExprParser;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

import static de.uni_passau.fim.se2.litterbox.ast.Constants.*;
import static de.uni_passau.fim.se2.litterbox.ast.opcodes.SpriteLookStmtOpcode.looks_gotofrontback;

public class SpriteLookStmtParser {

    public static SpriteLookStmt parse(JsonNode current, JsonNode allBlocks) throws ParsingException {
        Preconditions.checkNotNull(current);
        Preconditions.checkNotNull(allBlocks);

        final String opcodeString = current.get(OPCODE_KEY).asText();
        Preconditions
                .checkArgument(SpriteLookStmtOpcode.contains(opcodeString),
                        "Given blockID does not point to a sprite look block. Opcode is " + opcodeString);

        final SpriteLookStmtOpcode opcode = SpriteLookStmtOpcode.valueOf(opcodeString);
        StringExpr stringExpr;
        NumExpr numExpr;

        switch (opcode) {
            case looks_show:
                return new Show();
            case looks_hide:
                return new Hide();
            case looks_sayforsecs:
                stringExpr = StringExprParser.parseStringExprWithName(current, MESSAGE_KEY, allBlocks);
                numExpr = NumExprParser.parseNumExprWithName(current, SECS_KEY, allBlocks);
                return new SayForSecs(stringExpr, numExpr);
            case looks_say:
                stringExpr = StringExprParser.parseStringExprWithName(current, MESSAGE_KEY, allBlocks);
                return new Say(stringExpr);
            case looks_thinkforsecs:
                stringExpr = StringExprParser.parseStringExprWithName(current, MESSAGE_KEY, allBlocks);
                numExpr = NumExprParser.parseNumExprWithName(current, SECS_KEY, allBlocks);
                return new ThinkForSecs(stringExpr, numExpr);
            case looks_think:
                stringExpr = StringExprParser.parseStringExprWithName(current, MESSAGE_KEY, allBlocks);
                return new Think(stringExpr);
            case looks_nextcostume:
                return new NextCostume();
            case looks_switchcostumeto:
                Expression costumeChoice = CostumeChoiceParser.parse(current, allBlocks);
                return new SwitchCostumeTo(costumeChoice);
            case looks_changesizeby:
                numExpr = NumExprParser.parseNumExprWithName(current, CHANGE_KEY, allBlocks);
                return new ChangeSizeBy(numExpr);
            case looks_setsizeto:
                numExpr = NumExprParser.parseNumExprWithName(current, SIZE_KEY_CAP, allBlocks);
                return new SetSizeTo(numExpr);
            case looks_gotofrontback:
                return parseGoToLayer(current, allBlocks);
            case looks_goforwardbackwardlayers:
                return parseGoForwardBackwardLayer(current, allBlocks);
            default:
                throw new RuntimeException("Not implemented for opcode " + opcodeString);
        }
    }

    private static SpriteLookStmt parseGoForwardBackwardLayer(JsonNode current, JsonNode allBlocks)
            throws ParsingException {
        JsonNode front_back = current.get(FIELDS_KEY).get("FORWARD_BACKWARD").get(FIELD_VALUE);

        NumExpr num = NumExprParser.parseNumExprWithName(current, NUM_KEY, allBlocks);

        String layerOption = front_back.asText();
        if (layerOption.equals("forward")) {
            return new ChangeLayerBy(num);
        } else if (layerOption.equals("backward")) {
            NumExpr negated = new Mult(new NumberLiteral(-1), num);
            return new ChangeLayerBy(negated);
        } else {
            throw new ParsingException("Unknown option " + layerOption +
                    "+ when parsing block with opcode " + current.get(OPCODE_KEY));
        }
    }

    private static SpriteLookStmt parseGoToLayer(JsonNode current, JsonNode allBlocks) throws ParsingException {
        Preconditions.checkArgument(current.get(OPCODE_KEY).asText().equals(looks_gotofrontback.toString()));

        JsonNode front_back = current.get(FIELDS_KEY).get("FRONT_BACK").get(FIELD_VALUE);
        String layerOption = front_back.asText();
        try {
            return new GoToLayer(LayerChoice.fromString(layerOption));
        } catch (IllegalArgumentException e) {
            throw new ParsingException("Unknown LayerChoice label for GoToLayer.");
        }
    }
}
