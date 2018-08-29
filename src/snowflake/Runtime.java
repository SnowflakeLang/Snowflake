package snowflake;

import snowflake.block.Block;
import snowflake.block.ObjectBlock;
import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeRuntimeException;
import snowflake.lexical.Lexer;
import snowflake.lexical.TokenStream;
import snowflake.parsing.expression.FunctionExpression;
import snowflake.parsing.expression.NullExpression;
import snowflake.parsing.expression.VarDeclarationExpression;
import snowflake.parsing.parser.FunctionParser;
import snowflake.parsing.parser.NullParser;
import snowflake.parsing.parser.ObjectParser;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.Expression;
import snowflake.parsing.expression.ObjectExpression;
import snowflake.parsing.parser.VarDeclarationParser;
import snowflake.utils.StreamUtils;

public class Runtime {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            Lexer lexer = new Lexer();
            String code = "class Hello {" + "\n" +
                    "Integer lol = 5" + "\n";
            Visitor visitor = new Visitor();
            ObjectBlock superBlock = null;

            Block current = null;
            SnowflakeParser<?>[] parsers = new SnowflakeParser[]{
                    new ObjectParser(),
                    new FunctionParser(),
                    new VarDeclarationParser(),
                    new NullParser()
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

                        if (expression instanceof ObjectExpression) {
                            Block oBlock = visitor.visit(expression);

                            superBlock = (ObjectBlock) oBlock;
                            current = oBlock;
                        }

                        if (expression instanceof FunctionExpression) {
                            if (superBlock != null) {
                                Block fBlock = visitor.visit(expression);

                                superBlock.add(fBlock);
                                current = fBlock;
                            } else {
                                throw new SnowflakeRuntimeException("Line " + line + ": Function call outside of Object!");
                            }
                        }

                        if (expression instanceof VarDeclarationExpression) {
                            if (superBlock != null) {
                                TokenStream subStream = StreamUtils.getStream(stream, 4);

                                for (SnowflakeParser subParser : parsers) {
                                    if (subParser.shouldEvaluate(subStream)) {
                                        Expression sExpr = subParser.evaluate(superBlock, subStream);

                                        if (sExpr instanceof NullExpression) {
                                            ((VarDeclarationExpression) expression).setValue(null);
                                        }
                                    }
                                }

                                Block vBlock = visitor.visit(expression);

                                superBlock.add(vBlock);

                                //No need to add current because Variable Blocks are Read-Only
                            } else {
                                throw new SnowflakeRuntimeException("Line " + line + ": Variable declaration outside of Object!");
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
