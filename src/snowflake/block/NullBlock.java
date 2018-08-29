package snowflake.block;

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

    @Override
    public void run() {

    }
}
