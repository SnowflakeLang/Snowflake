package snowflake.parsing;

import snowflake.exception.SnowflakeException;
import snowflake.lexical.TokenStream;

public abstract class SnowflakeParser<T extends Expression> {

    public abstract boolean shouldEvaluate(TokenStream stream) throws SnowflakeException;

    public abstract T evaluate(TokenStream stream);

    //Here we will add all of our parsers!


}
