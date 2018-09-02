package snowflake.block;

import snowflake.utils.Value;

public class AssignmentBlock extends ReadableBlock {

    private Block superBlock;
    private Value value;

    public AssignmentBlock(Block superBlock, Value value) {
        super(superBlock);

        this.superBlock = superBlock;
        this.value = value;
    }

    @Override
    public Block getSuperBlock() {
        return superBlock;
    }

    public Value getValue() {
        return value;
    }

}
