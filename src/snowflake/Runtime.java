package snowflake;

import snowflake.block.Block;
import snowflake.block.ObjectBlock;
import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeParserException;
import snowflake.exception.SnowflakeRuntimeException;
import snowflake.lexical.Lexer;
import snowflake.lexical.TokenStream;
import snowflake.parsing.parser.ObjectParser;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.Expression;
import snowflake.parsing.expression.ObjectExpression;

public class Runtime {

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        String code = "class Hello {";
        Visitor visitor = new Visitor();
        ObjectBlock superBlock = null;

        Block current = null;
        SnowflakeParser<?>[] parsers = new SnowflakeParser[] {
                new ObjectParser(),
        };

        try {
            int line = 0;

            /*
            for (String str : code.split("\n")) {
            */
                line++;

                TokenStream stream = lexer.getStream(code, line);

                for (SnowflakeParser parser : parsers) {
                    if (parser.shouldEvaluate(stream)) {
                        Expression expression = parser.evaluate(stream);

                        if (expression instanceof ObjectExpression) {
                            Block block = visitor.visit(expression);

                            if (superBlock != null) {
                                throw new SnowflakeParserException("Line " + line + ": Can't create two Objects in one file!");
                            } else if (!(block instanceof ObjectBlock)) {
                                throw new SnowflakeParserException("Line " + line + ": Couldn't assign block to \"" + code + "\"!");
                            } else {
                                superBlock = (ObjectBlock) block;
                                current = block;
                            }
                        }
                    }
                }
                /*
            }
            */
        } catch (SnowflakeException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            if (superBlock == null) {
                throw new SnowflakeRuntimeException("Couldn't find any Object!");
            } else {
                superBlock.run();
            }
        } catch (SnowflakeException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
