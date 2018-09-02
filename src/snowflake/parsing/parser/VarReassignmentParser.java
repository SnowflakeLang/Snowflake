package snowflake.parsing.parser;

import snowflake.block.Block;
import snowflake.block.VariableBlock;
import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeParserException;
import snowflake.lexical.Token;
import snowflake.lexical.TokenStream;
import snowflake.lexical.type.DataType;
import snowflake.lexical.type.KeywordType;
import snowflake.lexical.type.TokenType;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.expression.VarDeclarationExpression;
import snowflake.utils.TypeUtils;
import snowflake.utils.Value;

public class VarReassignmentParser extends SnowflakeParser<VarDeclarationExpression> {

    @Override
    public boolean shouldEvaluate(TokenStream stream) {
        try {
            int line = stream.getLine();

            stream.reset();

            if (stream.matches(0, 2, new TokenStream(line,
                    new Token(DataType.IDENTIFIER, stream.read(1).getValue(), line),
                    new Token(TokenType.EQUAL, stream.read(2).getValue(), line)))) {
                return true;
            }
        } catch (SnowflakeException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public VarDeclarationExpression evaluate(Block superBlock, TokenStream stream) throws SnowflakeException {
        for (Block block : superBlock.getSubBlocks()) {
            if (block instanceof VariableBlock) {
                VariableBlock vBlock = (VariableBlock) block;

                if (vBlock.getName().equals(stream.read().getValue())) {
                    Token token = stream.peek();

                    if (TypeUtils.isType(token) == vBlock.getType()) {
                        Value value = new Value(TypeUtils.isType(token), token.getValue());
                        VarDeclarationExpression expr =  new VarDeclarationExpression(stream.getLine(), superBlock, vBlock.getType(), vBlock.getName());

                        expr.setValue(value);
                    }

                    throw new SnowflakeParserException("Line " + stream.getLine() + ": Incompatible type " +
                        token.getTokenType().toString() + " and " + token.getTokenType().toString() + "!");
                }
            }
        }

        throw new SnowflakeParserException("Line " + stream.getLine() + ": Tried to assign to a non-existing variable!");
    }
}
