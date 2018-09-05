package snowflake;

import snowflake.block.*;
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
                    "Integer lol" + "\n";
            Visitor visitor = new Visitor();
            ObjectBlock superBlock = null;

            Block current = null;
            SnowflakeParser<?>[] parsers = new SnowflakeParser[] {
                    new ObjectParser(),
                    new FunctionParser(),
                    new VarDeclarationParser(),
            };

            SnowflakeParser<?>[] singleSubParsers = new SnowflakeParser[] {
                    new NullParser(),
                    new AssignmentParser()
            };

            int line = 1;

            for (String str : code.split("\n")) {
                if (str.startsWith("//")) {
                    //If the line does start with a double forward slash, we want to SKIP the line BEFORE being pushed into a TokenStream (otherwise we'd get a complete line of Identifiers!)

                    continue;
                }

                TokenStream stream = lexer.getStream(str, line);

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
                                if (stream.size() == 4) {
                                    TokenStream subStream = new TokenStream(stream.getLine(), stream.peek());

                                    for (SnowflakeParser subParser : singleSubParsers) {
                                        if (subParser.shouldEvaluate(subStream)) {
                                            Expression subExpr = subParser.evaluate(superBlock, subStream);

                                            if (subExpr instanceof NullExpression) {
                                                NullBlock nBlock = (NullBlock) visitor.visit(subExpr);
                                                ((VarDeclarationExpression) expression).setValue(nBlock.getValue());
                                            } else if (subExpr instanceof AssignmentExpression) {
                                                if (((AssignmentExpression) subExpr).getValue().getType() != ((VarDeclarationExpression) expression).getReturnType()) {
                                                    throw new SnowflakeRuntimeException("Line " + line + ": Expected " + ((VarDeclarationExpression) expression).getReturnType().getValue() + ", got " +
                                                            ((AssignmentExpression) subExpr).getValue().getType().getValue() + "!");
                                                }

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
                                    //It's a reference to a function
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
