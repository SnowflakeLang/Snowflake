package snowflake.parsing.expression;

import snowflake.Visitor;
import snowflake.block.Block;
import snowflake.block.ObjectBlock;
import snowflake.parsing.Expression;

public class ObjectExpression extends Expression {

    private int line;

    private String name;

    public ObjectExpression(int line, String name) {
        this.line = line;

        this.name = name;
    }

    public int getLine() {
        return line;
    }

    public String getName() {
        return name;
    }

    @Override
    public Block accept(Visitor visitor) {
        return new ObjectBlock(name);
    }
}
