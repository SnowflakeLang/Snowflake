package snowflake.parsing.expression;

import snowflake.Visitor;
import snowflake.block.Block;
import snowflake.block.NullBlock;
import snowflake.parsing.Expression;

public class NullExpression extends Expression {

    private int line;

    public NullExpression(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public Block accept(Visitor visitor) {
        return new NullBlock(null);
    }
}
