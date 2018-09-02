package snowflake.block;

import snowflake.parsing.expression.VarDeclarationExpression;
import snowflake.utils.TypeUtils;
import snowflake.utils.Value;

public class VariableBlock extends Block {

    private Block superBlock;
    private TypeUtils.ObjectType type;
    private String name;
    private Value value;

    public VariableBlock(Block superBlock, TypeUtils.ObjectType type, String name, Value value) {
        super(superBlock);

        this.superBlock = superBlock;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    @Override
    public Block getSuperBlock() {
        return superBlock;
    }

    public TypeUtils.ObjectType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public void reDeclare(VarDeclarationExpression expression) {
        this.value = expression.getValue();
    }

    @Override
    public void run() {
        if (value == null) {
            System.out.println("Variable -> " + name + ", returns " + type.getValue() + " with value null");
        } else {
            System.out.println("Variable -> " + name + ", returns " + type.getValue() + " with value " + value.getValue().toString());
        }
    }
}
