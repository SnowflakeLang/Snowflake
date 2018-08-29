package snowflake.block;

import snowflake.exception.SnowflakeException;

import java.util.ArrayList;

public class ObjectBlock extends Block {

    private String name;
    private ArrayList<Block> subBlocks;

    public ObjectBlock(String name) {
        super(null);

        this.name = name;
        this.subBlocks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    @Override
    public Block getSuperBlock() {
        return null;
    }

    public ArrayList<Block> getSubBlocks() {
        return subBlocks;
    }

    public <T extends Block> void add(T subBlock) {
        subBlocks.add(subBlock);
    }

    @Override
    public void run() {
        System.out.println("Object -> " + name);

        for (Block block : subBlocks) {
            try {
                block.run();
            } catch (SnowflakeException ex) {
                ex.printStackTrace();
            }
        }
    }
}
