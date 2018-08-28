package snowflake.parsing.parser;

import snowflake.exception.SnowflakeException;
import snowflake.lexical.Token;
import snowflake.lexical.TokenStream;
import snowflake.lexical.type.DataType;
import snowflake.lexical.type.TokenType;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.Expression;
import snowflake.parsing.expression.ObjectExpression;

public class ObjectParser extends SnowflakeParser {

    @Override
    public boolean shouldEvaluate(TokenStream stream) {
        Token classToken = stream.read();
        Token identifierToken = stream.read();
        Token openBraceToken = stream.read();

        if (classToken.getTokenType() == DataType.IDENTIFIER) {
            if (identifierToken.getTokenType() == DataType.IDENTIFIER) {
                if (openBraceToken.getTokenType() == TokenType.OBRACE) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Expression evaluate(TokenStream stream) {
        Token classToken = stream.read();
        Token identifierToken = stream.read();

        if (classToken.getValue().equals("class")) {
            return new ObjectExpression(stream.getLine(), identifierToken.getValue());
        }

        return null;
    }
}
