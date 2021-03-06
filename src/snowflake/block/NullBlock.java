package snowflake.block;

import snowflake.utils.Value;

public class NullBlock extends ReadableBlock {

    private Block superBlock;

    public NullBlock(Block superBlock) {
        super(superBlock);

        this.superBlock = superBlock;
    }

    @Override
    public Block getSuperBlock() {
        return superBlock;
    }

    public Value getValue() {
        return null;
    }
}
