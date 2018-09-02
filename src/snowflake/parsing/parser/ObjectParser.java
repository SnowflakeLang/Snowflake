package snowflake.parsing.parser;

import snowflake.block.Block;
import snowflake.exception.SnowflakeException;
import snowflake.lexical.Token;
import snowflake.lexical.TokenStream;
import snowflake.lexical.type.DataType;
import snowflake.lexical.type.KeywordType;
import snowflake.lexical.type.TokenType;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.Expression;
import snowflake.parsing.expression.ObjectExpression;
import snowflake.utils.StreamUtils;

public class ObjectParser extends SnowflakeParser {

    @Override
    public boolean shouldEvaluate(TokenStream stream) {
        try {
            int line = stream.getLine();

            return StreamUtils.compares(stream, new TokenStream(line,
                    new Token(KeywordType.CLASS, "class", line),
                    new Token(DataType.IDENTIFIER, stream.read(1).getValue(), line),
                    new Token(TokenType.OBRACE, "{", line)));
        } catch (SnowflakeException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public Expression evaluate(Block superBlock, TokenStream stream) {
        stream.skip(1);

        Token identifierToken = stream.read();

        return new ObjectExpression(stream.getLine(), identifierToken.getValue());
    }
}
