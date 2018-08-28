package snowflake;

import snowflake.block.Block;
import snowflake.parsing.Expression;

public class Visitor {

    public Block visit(Expression expression) {
        return expression.accept(this);
    }

}
