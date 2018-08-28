package snowflake.block;

import snowflake.utils.TypeUtils;

import java.util.ArrayList;

public class FunctionBlock extends Block {

    private TypeUtils.ObjectType returnType;
    private String name;
    private ArrayList<Block> subBlocks;

    public FunctionBlock(Block superBlock, TypeUtils.ObjectType returnType, String name) {
        super(superBlock);

        this.returnType = returnType;
        this.name = name;
        this.subBlocks = new ArrayList<>();
    }

    public TypeUtils.ObjectType getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Block> getSubBlocks() {
        return subBlocks;
    }

    public <T extends Block> void add(T subBlock) {
        subBlocks.add(subBlock);
    }

    @Override
    public void run() {
        System.out.println("Return Type -> " + returnType.getValue());
        System.out.println("Name -> " + name);
    }
}
