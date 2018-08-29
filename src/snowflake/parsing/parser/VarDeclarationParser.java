package snowflake.parsing.parser;

import snowflake.block.Block;
import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeParserException;
import snowflake.lexical.Token;
import snowflake.lexical.TokenStream;
import snowflake.lexical.type.DataType;
import snowflake.lexical.type.TokenType;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.expression.VarDeclarationExpression;
import snowflake.utils.StreamUtils;
import snowflake.utils.TypeUtils;

public class VarDeclarationParser extends SnowflakeParser<VarDeclarationExpression> {

    @Override
    public boolean shouldEvaluate(TokenStream stream) {
        try {
            int line = stream.getLine();

            Token typeToken = stream.read(0);

            if (typeToken.getValue().equals("Void")) {
                throw new SnowflakeParserException("Line " + line + ": Can't assign \"Void\" to variable!");
            }

            //TODO: Debug this!

            if (StreamUtils.compares(stream, new TokenStream(line,
                    new Token(DataType.IDENTIFIER, "Integer", line),
                    new Token(DataType.IDENTIFIER, stream.read(1).getValue(), line),
                    new Token(TokenType.EQUAL, stream.read(2).getValue(), line)))) {
                return true;
            } else if (StreamUtils.compares(stream, new TokenStream(line,
                    new Token(DataType.IDENTIFIER, "String", line),
                    new Token(DataType.IDENTIFIER, stream.read(1).getValue(), line),
                    new Token(TokenType.EQUAL, stream.read(2).getValue(), line)))) {
                return true;
            }
        } catch (SnowflakeException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public VarDeclarationExpression evaluate(Block superBlock, TokenStream stream) throws SnowflakeException {
        Token typeToken= stream.read();
        Token identifierToken = stream.read();
        TypeUtils.ObjectType type = TypeUtils.isType(typeToken);

        if ((type == null) || (type == TypeUtils.ObjectType.VOID)) {
            throw new SnowflakeParserException("Line " + stream.getLine() + ": Can't assign \"Void\" to variable!");
        }

        stream.skip(1); //Skip "="

        return new VarDeclarationExpression(stream.getLine(), superBlock, type, identifierToken.getValue());
    }
}
