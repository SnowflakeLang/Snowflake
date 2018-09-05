package snowflake.parsing.parser;

import snowflake.block.Block;
import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeParserException;
import snowflake.lexical.Token;
import snowflake.lexical.TokenStream;
import snowflake.lexical.type.DataType;
import snowflake.lexical.type.KeywordType;
import snowflake.lexical.type.TokenType;
import snowflake.parsing.SnowflakeParser;
import snowflake.parsing.expression.VarDeclarationExpression;
import snowflake.utils.TypeUtils;

public class VarDeclarationParser extends SnowflakeParser<VarDeclarationExpression> {

    @Override
    public boolean shouldEvaluate(TokenStream stream) {
        try {
            int line = stream.getLine();

            if (stream.matches(0, 2, new TokenStream(line,
                    new Token(KeywordType.INTEGER, "Integer", line),
                    new Token(DataType.IDENTIFIER, stream.read(1).getValue(), line),
                    new Token(TokenType.EQUAL, stream.read(2).getValue(), line)))) {
                return true;
            } else if (stream.matches(0, 2, new TokenStream(line,
                    new Token(KeywordType.STRING, "String", line),
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

        return new VarDeclarationExpression(stream.getLine(), superBlock, type, identifierToken.getValue(), true);
    }
}
