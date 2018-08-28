package snowflake.block;

import snowflake.exception.SnowflakeException;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Block {

    private Block superBlock;
    private ArrayList<Block> subBlocks;

    public Block(Block superBlock) {
        this.superBlock = superBlock;

        this.subBlocks = new ArrayList<>();
    }

    public Block getSuperBlock() {
        return superBlock;
    }

    public Block[] getBlockTree() {
        ArrayList<Block> tree = new ArrayList<>();

        Block block = this;

        while (block != null) {
            tree.add(block);
            block = block.getSuperBlock();
        }

        Collections.reverse(tree);

        return tree.toArray(new Block[tree.size()]);
    }

    public ArrayList<Block> getSubBlocks() {
        return subBlocks;
    }

    public <T extends Block> void add(T subBlock) {
        subBlocks.add(subBlock);
    }

    public abstract void run() throws SnowflakeException;

}
