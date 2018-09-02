package snowflake.parsing.parser;

import snowflake.block.AssignmentBlock;
import snowflake.block.Block;
import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeParserException;
import snowflake.lexical.Token;
import snowflake.lexical.TokenStream;
import snowflake.lexical.type.DataType;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.expression.AssignmentExpression;
import snowflake.utils.TypeUtils;
import snowflake.utils.Value;

public class AssignmentParser extends SnowflakeParser<AssignmentExpression> {

    @Override
    public boolean shouldEvaluate(TokenStream stream) {
        try {
            int line = stream.getLine();

            //Stream size should ALWAYS be 1
            Token token = stream.read();

            if (DataType.isType(token)) {
                if (token.getTokenType() == DataType.IDENTIFIER) {
                    throw new SnowflakeParserException("Line " + line + ": Can't assign identifiers!");
                }

                return true;
            }
        } catch (SnowflakeException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public AssignmentExpression evaluate(Block superBlock, TokenStream stream) {
        Token token = stream.read();

        if (token.getTokenType() == DataType.INTEGER_LITERAL) {
            return new AssignmentExpression(stream.getLine(), new Value(TypeUtils.ObjectType.INT, token.getValue()));
        }

        if (stream.read().getTokenType() == DataType.STRING_LITERAL) {
            return new AssignmentExpression(stream.getLine(), new Value(TypeUtils.ObjectType.STRING, token.getValue()));
        }

        return null;
    }
}
