package snowflake.parsing;

import snowflake.Visitor;
import snowflake.block.Block;

public abstract class Expression {

    public abstract Block accept(Visitor visitor);

}
