package snowflake;

import snowflake.block.Block;
import snowflake.block.ObjectBlock;
import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeRuntimeException;
import snowflake.lexical.Lexer;
import snowflake.lexical.TokenStream;
import snowflake.parsing.expression.FunctionExpression;
import snowflake.parsing.parser.FunctionParser;
import snowflake.parsing.parser.ObjectParser;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.Expression;
import snowflake.parsing.expression.ObjectExpression;

public class Runtime {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            Lexer lexer = new Lexer();
            String code = "class Hello {";
            Visitor visitor = new Visitor();
            ObjectBlock superBlock = null;

            Block current = null;
            SnowflakeParser<?>[] parsers = new SnowflakeParser[]{
                    new ObjectParser(),
                    new FunctionParser(),
            };

            int line = 1;

            for (String str : code.split("\n")) {
                TokenStream stream = lexer.getStream(str, line);

                //TODO: Fix this so that the parser looks for a PART of the stream and not the whole stream
                //TODO: This would be necessary for Variable Declaration (Use a UnassignedVarDeclaration first than use
                //TODO: Equals to see if it is assigned using another expression (ValueExpression)
                for (SnowflakeParser parser : parsers) {
                    if (parser.shouldEvaluate(stream)) {
                        Expression expression = parser.evaluate(superBlock, stream);
                        Block block = visitor.visit(expression);

                        if (expression instanceof ObjectExpression) {
                            superBlock = (ObjectBlock) block;
                            current = block;
                        }

                        if (expression instanceof FunctionExpression) {
                            if (superBlock != null) {
                                superBlock.add(block);
                                current = block;
                            } else {
                                throw new SnowflakeRuntimeException("Line " + line + ": Function call without Object!");
                            }
                        }
                    }
                }

                line++;
            }

            if (superBlock == null) {
                throw new SnowflakeRuntimeException("Couldn't find any Object!");
            } else {
                superBlock.run();
            }
        } catch (SnowflakeException ex) {
            ex.printStackTrace();
        }

        System.out.println("Compiled in " + (System.currentTimeMillis() - start) +  "ms");
    }

}
