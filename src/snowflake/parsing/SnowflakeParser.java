package snowflake.parsing;

import snowflake.block.Block;
import snowflake.exception.SnowflakeException;
import snowflake.lexical.TokenStream;

public abstract class SnowflakeParser<T extends Expression> {

    public abstract boolean shouldEvaluate(TokenStream stream) throws SnowflakeException;

    public abstract T evaluate(Block superBlock, TokenStream stream) throws SnowflakeException;

}
