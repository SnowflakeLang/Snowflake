package snowflake.parsing.parser;

import snowflake.block.Block;
import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeParserException;
import snowflake.lexical.Token;
import snowflake.lexical.TokenStream;
import snowflake.lexical.type.DataType;
import snowflake.lexical.type.TokenType;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.expression.FunctionExpression;
import snowflake.utils.StreamUtils;
import snowflake.utils.TypeUtils;

public class FunctionParser extends SnowflakeParser<FunctionExpression> {

    @Override
    public boolean shouldEvaluate(TokenStream stream) {
        try {
            int line = stream.getLine();

            //TODO: Add Parameters to function calls :) (Use while loop)
            return StreamUtils.compares(stream, new TokenStream(line,
                    new Token(DataType.IDENTIFIER, stream.read(0).getValue(), line),
                    new Token(DataType.IDENTIFIER, stream.read(1).getValue(), line),
                    new Token(TokenType.OPAREN, "(", line),
                    new Token(TokenType.CPAREN, ")", line),
                    new Token(TokenType.OBRACE, "{", line)));
        } catch (SnowflakeException ex) {
            System.out.println(ex.getMessage());
        }

        return false;
    }

    @Override
    public FunctionExpression evaluate(Block superBlock, TokenStream stream) throws SnowflakeException {
        Token typeToken= stream.read();
        Token identifierToken = stream.read();
        TypeUtils.ObjectType type = TypeUtils.isType(typeToken);

        if (type == null) {
            throw new SnowflakeParserException("Line " + stream.getLine() + ": Function " + identifierToken.getValue() + " must return a valid Object!");
        }

        return new FunctionExpression(stream.getLine(), superBlock, type, identifierToken.getValue());
    }
}
