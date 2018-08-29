package snowflake.parsing.expression;

import snowflake.Visitor;
import snowflake.block.Block;
import snowflake.block.VariableBlock;
import snowflake.parsing.Expression;
import snowflake.utils.TypeUtils;
import snowflake.utils.Value;

public class VarDeclarationExpression extends Expression {

    private int line;

    private Block superBlock;
    private TypeUtils.ObjectType returnType;
    private String name;
    private Value value;

    public VarDeclarationExpression(int line, Block superBlock, TypeUtils.ObjectType returnType, String name) {
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

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public Block accept(Visitor visitor) {
        return new VariableBlock(superBlock, returnType, name, value);
    }
}
