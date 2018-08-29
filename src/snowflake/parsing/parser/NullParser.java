package snowflake.parsing.parser;

import snowflake.block.Block;
import snowflake.lexical.Token;
import snowflake.lexical.TokenStream;
import snowflake.lexical.type.KeywordType;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.expression.NullExpression;
import snowflake.utils.StreamUtils;

public class NullParser extends SnowflakeParser<NullExpression> {

    @Override
    public boolean shouldEvaluate(TokenStream stream) {
        int line = stream.getLine();

        return StreamUtils.compares(stream, new TokenStream(line,
                new Token(KeywordType.NULL, "null", line)));
    }

    @Override
    public NullExpression evaluate(Block superBlock, TokenStream stream) {
        return new NullExpression(stream.getLine());
    }
}
