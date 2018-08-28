package snowflake.parsing;

import snowflake.lexical.TokenStream;
import snowflake.parsing.expression.Expression;

public class ObjectParser extends SnowflakeParser {

    @Override
    boolean shouldEvaluate(TokenStream stream) {
        return false;
    }

    @Override
    Expression evaluate(TokenStream stream) {
        return null;
    }
}
