package snowflake;

import snowflake.block.Block;
import snowflake.block.ObjectBlock;
import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeParserException;
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
            String code = "class Hello {" + "\n" +
                    "   Integer main () {" + "\n";
            Visitor visitor = new Visitor();
            ObjectBlock superBlock = null;

            Block current = null;
            SnowflakeParser<?>[] parsers = new SnowflakeParser[]{
                    new ObjectParser(),
                    new FunctionParser()
            };

            int line = 1;

            for (String str : code.split("\n")) {
                TokenStream stream = lexer.getStream(str, line);

                for (SnowflakeParser parser : parsers) {
                    if (parser.shouldEvaluate(stream)) {
                        Expression expression = parser.evaluate(superBlock, stream);
                        Block block = visitor.visit(expression);

                        if (expression instanceof ObjectExpression) {
                            if (superBlock != null) {
                                throw new SnowflakeParserException("Line " + line + ": Can't create two Objects in one file!");
                            } else if (!(block instanceof ObjectBlock)) {
                                throw new SnowflakeParserException("Line " + line + ": Couldn't assign block to \"" + str + "\"!");
                            } else {
                                superBlock = (ObjectBlock) block;
                                current = block;
                            }
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
