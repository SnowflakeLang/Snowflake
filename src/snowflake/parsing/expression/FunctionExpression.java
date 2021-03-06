package snowflake.parsing.expression;

import snowflake.Visitor;
import snowflake.block.Block;
import snowflake.block.FunctionBlock;
import snowflake.parsing.Expression;
import snowflake.utils.TypeUtils;

public class FunctionExpression extends Expression {

    private int line;

    private Block superBlock;
    private TypeUtils.ObjectType returnType;
    private String name;

    public FunctionExpression(int line, Block superBlock, TypeUtils.ObjectType returnType, String name) {
        this.line = line;

        this.superBlock = superBlock;
        this.returnType = returnType;
        this.name = name;
    }

    public int getLine() {
        return line;
    }

    public Block getSuperBlock() {
        return superBlock;
    }

    public TypeUtils.ObjectType getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    @Override
    public Block accept(Visitor visitor) {
        return new FunctionBlock(superBlock, returnType, name);
    }
}
