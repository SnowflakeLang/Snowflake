package snowflake.parsing;

import snowflake.lexical.TokenStream;
import snowflake.parsing.expression.Expression;

abstract class SnowflakeParser<T extends Expression> {

    abstract boolean shouldEvaluate(TokenStream stream);

    abstract T evaluate(TokenStream stream);

    //Here we will add all of our parsers!


}
