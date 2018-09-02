package snowflake.block;

import snowflake.exception.SnowflakeException;

public class ReadableBlock extends Block {

    ReadableBlock(Block superBlock) {
        super(superBlock);
    }

    @Override
    public <T extends Block> void add(T subBlock) throws SnowflakeException {
        throw new SnowflakeException("Read-only block cannot have any sub blocks!");
    }

    @Override
    public void run() throws SnowflakeException {
        throw new SnowflakeException("Can't run a Readable Block!");
    }
}
