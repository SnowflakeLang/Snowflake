package snowflake.parsing.expression;

import snowflake.Visitor;

public abstract class Expression {

    abstract void accept(Visitor visitor);

}
