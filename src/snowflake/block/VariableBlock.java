package snowflake.block;

import snowflake.utils.TypeUtils;
import snowflake.utils.Value;

public class VariableBlock extends ReadableBlock {

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

    @Override
    public void run() {
        System.out.println("Variable -> " + name + ", returns " + type.getValue() + " with value " + value.toString());
    }
}
