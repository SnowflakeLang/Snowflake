package snowflake.block;

import snowflake.utils.TypeUtils;

public class VariableBlock extends ReadableBlock {

    private Block superBlock;
    private TypeUtils.ObjectType type;
    private String name;

    public VariableBlock(Block superBlock, TypeUtils.ObjectType type, String name) {
        super(superBlock);

        this.superBlock = superBlock;
        this.type = type;
        this.name = name;
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

    @Override
    public void run() { }
}
