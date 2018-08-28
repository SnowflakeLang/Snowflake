package snowflake;

import snowflake.parsing.expression.Expression;

public interface Visitor {

    void visit(Expression expression);

}
