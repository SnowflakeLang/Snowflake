package snowflake;

import snowflake.block.AssignmentBlock;
import snowflake.block.Block;
import snowflake.block.NullBlock;
import snowflake.block.ObjectBlock;
import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeRuntimeException;
import snowflake.lexical.Lexer;
import snowflake.lexical.TokenStream;
import snowflake.lexical.type.TokenType;
import snowflake.parsing.expression.*;
import snowflake.parsing.parser.*;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.Expression;

public class Runtime {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            Lexer lexer = new Lexer();
            String code = "class Hello {" + "\n" +
                    "String lol = null" + "\n";
            Visitor visitor = new Visitor();
            ObjectBlock superBlock = null;

            Block current = null;
            SnowflakeParser<?>[] parsers = new SnowflakeParser[] {
                    new ObjectParser(),
                    new FunctionParser(),
                    new VarDeclarationParser(),
                    new NullParser()
            };

            SnowflakeParser<?>[] subParsers = new SnowflakeParser[] {
                    new NullParser(),
                    new AssignmentParser()
            };

            int line = 1;

            for (String str : code.split("\n")) {
                if (str.startsWith("//")) {
                    System.out.println("Skipped line " + line);

                    //If the line does start with a double forward slash, we want to SKIP the line BEFORE being pushed into a TokenStream (otherwise we'd get a complete line of Identifiers!)

                    continue;
                }

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
                                int remainder = stream.size() - stream.getPosition();

                                if (remainder == 1) {
                                    TokenStream subStream = new TokenStream(stream.getLine(), stream.peek());
                                    for (SnowflakeParser subParser : subParsers) {
                                        if (subParser.shouldEvaluate(subStream)) {
                                            Expression subExpr = subParser.evaluate(superBlock, subStream);

                                            if (subExpr instanceof NullExpression) {
                                                NullBlock nBlock = (NullBlock) visitor.visit(subExpr);
                                                ((VarDeclarationExpression) expression).setValue(nBlock.getValue());
                                            } else if (subExpr instanceof AssignmentExpression) {
                                                AssignmentBlock aBlock = (AssignmentBlock) visitor.visit(subExpr);
                                                ((VarDeclarationExpression) expression).setValue(aBlock.getValue());
                                            } else {
                                                throw new SnowflakeRuntimeException("Line " + line + ": Expected " + ((VarDeclarationExpression) expression).getReturnType().getValue() + ", got " +
                                                        stream.read(0).getTokenType().toString() + "!");
                                            }
                                        }
                                    }

                                    //Check Value
                                    //Throw exception if it's neither

                                    if (stream.read().getTokenType() == TokenType.SEMI_COLON) {
                                        throw new SnowflakeRuntimeException("Line " + line + ": Expected " + ((VarDeclarationExpression) expression).getReturnType().getValue() + ", got \";\"!");
                                    }
                                } else {
                                    //It's a multi expression
                                    //TODO: Make this work
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
