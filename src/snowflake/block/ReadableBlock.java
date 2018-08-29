package snowflake.block;

import snowflake.exception.SnowflakeException;

public abstract class ReadableBlock extends Block {

    ReadableBlock(Block superBlock) {
        super(superBlock);
    }

    @Override
    public <T extends Block> void add(T subBlock) throws SnowflakeException {
        throw new SnowflakeException("Read-only block cannot have any sub blocks!");
    }

}
