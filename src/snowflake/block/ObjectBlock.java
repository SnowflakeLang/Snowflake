package snowflake.block;

import java.util.ArrayList;

public class ObjectBlock extends Block {

    private String name;
    private ArrayList<Block> subBlocks;

    public ObjectBlock(String name) {
        super(null);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Block getSuperBlock() {
        return super.getSuperBlock();
    }

    public ArrayList<Block> getSubBlocks() {
        return subBlocks;
    }

    public <T extends Block> void add(T subBlock) {
        subBlocks.add(subBlock);
    }

    @Override
    public void run() {

    }
}
