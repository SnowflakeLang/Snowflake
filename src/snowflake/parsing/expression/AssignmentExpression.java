package snowflake.parsing.expression;

import snowflake.Visitor;
import snowflake.block.AssignmentBlock;
import snowflake.block.Block;
import snowflake.block.NullBlock;
import snowflake.parsing.Expression;
import snowflake.utils.Value;

public class AssignmentExpression extends Expression {

    private int line;
    private Value value;

    public AssignmentExpression(int line, Value value) {
        this.line = line;

        this.value = value;
    }

    public int getLine() {
        return line;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public Block accept(Visitor visitor) {
        return new AssignmentBlock(null, value);
    }

}
