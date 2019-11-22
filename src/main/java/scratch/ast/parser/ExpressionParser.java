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
package scratch.ast.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import scratch.ast.Constants;
import scratch.ast.ParsingException;
import scratch.ast.model.expression.Expression;
import scratch.ast.model.expression.UnspecifiedExpression;
import scratch.ast.model.variable.Qualified;
import scratch.ast.model.variable.StrId;
import scratch.ast.opcodes.BoolExprOpcode;
import scratch.ast.opcodes.NumExprOpcode;
import scratch.ast.opcodes.StringExprOpcode;
import scratch.ast.parser.symboltable.ExpressionListInfo;
import scratch.ast.parser.symboltable.VariableInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static scratch.ast.Constants.OPCODE_KEY;
import static scratch.ast.Constants.POS_DATA_ARRAY;

public class ExpressionParser {

    public static Expression parseExpression(JsonNode block, int pos, JsonNode blocks) throws ParsingException {
        try {
            return NumExprParser.parseNumExpr(block, pos, blocks);
        } catch (Exception e) {
            try {
                return StringExprParser.parseStringExpr(block, pos, blocks);
            } catch (Exception ex) {
                try {
                    return BoolExprParser.parseBoolExpr(block, pos, blocks);
                } catch (Exception exc) {
                    try {
                        return ListExprParser.parseListExpr(block, pos, blocks);
                    } catch (Exception excp) {
                        return new UnspecifiedExpression();
                        //throw new ParsingException("This is no expression we can parse.");
                    }
                }
            }
        }
    }

    static ArrayNode getExprArrayAtPos(JsonNode inputs, int pos) {
        List<Map.Entry> slotEntries = new LinkedList<>();
        inputs.fields().forEachRemaining(slotEntries::add);
        Map.Entry slotEntry = slotEntries.get(pos);
        ArrayNode exprArray = (ArrayNode) slotEntry.getValue();
        String numberName = (String) slotEntry
            .getKey(); // we don't need that here but maybe later for storing additional information
        return exprArray;
    }

    static ArrayNode getExprArrayByName(JsonNode inputs, String inputName) {
        return (ArrayNode) inputs.get(inputName);
    }

    static int getShadowIndicator(ArrayNode exprArray) {
        return exprArray.get(Constants.POS_INPUT_SHADOW).asInt();
    }

    static ArrayNode getDataArrayAtPos(JsonNode inputs, int pos) { // TODO maybe rename or comment
        return (ArrayNode) getExprArrayAtPos(inputs, pos).get(POS_DATA_ARRAY);
    }

    static ArrayNode getDataArrayByName(JsonNode inputs, String inputName) { // TODO maybe rename or comment
        return (ArrayNode) getExprArrayByName(inputs, inputName).get(POS_DATA_ARRAY);
    }

    public static Expression parseExpressionBlock(JsonNode current, JsonNode allBlocks) throws ParsingException {
        if (current instanceof ArrayNode) {
            // it's a list or variable
            String idString = current.get(2).asText();
            if (ProgramParser.symbolTable.getVariables().containsKey(idString)) {
                VariableInfo variableInfo = ProgramParser.symbolTable.getVariables().get(idString);

                return new Qualified(new StrId(variableInfo.getActor()),
                    new StrId((variableInfo.getVariableName())));

            } else if (ProgramParser.symbolTable.getLists().containsKey(idString)) {
                ExpressionListInfo variableInfo = ProgramParser.symbolTable.getLists().get(idString);
                return new Qualified(new StrId(variableInfo.getActor()),
                    new StrId((variableInfo.getVariableName())));
            }
        } else {
            // it's a normal reporter block
            String opcode = current.get(OPCODE_KEY).asText();
            if (NumExprOpcode.contains(opcode)) {
                return NumExprParser.parseBlockNumExpr(current, allBlocks);
            } else if (StringExprOpcode.contains(opcode)) {
                return StringExprParser.parseBlockStringExpr(current, allBlocks);
            } else if (BoolExprOpcode.contains(opcode)) {
                return BoolExprParser.parseBlockBoolExpr(current, allBlocks);
            } else {
                throw new ParsingException(opcode + " is an unexpected opcode for an expression");
            }
        }
        throw new ParsingException("Calling parseExpressionBlock here went wrong");
    }
}