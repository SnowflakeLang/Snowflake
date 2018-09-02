package snowflake.lexical;

import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeStreamException;

import java.util.ArrayList;
import java.util.Arrays;

public class TokenStream {

    private ArrayList<Token> tokens;
    private Token current;

    private int line;
    private int position;

    TokenStream(int line) {
        this.tokens = new ArrayList<>();
        this.line = line;

        position = 0;
    }

    public TokenStream(int line, Token... tokens) {
        this.tokens = new ArrayList<>(Arrays.asList(tokens));
        this.line = line;

        position = 0;
    }

    public TokenStream(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.line = 0;

        position = 0;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public Token getCurrent() {
        return current;
    }

    public int getLine() {
        return line;
    }

    public int getPosition() {
        return position;
    }

    public void write(Token token) {
        tokens.add(token);
    }

    public Token read() {
        if (position == tokens.size()) {
            reset();
        }

        Token token = tokens.get(position);
        position++;

        return token;
    }

    public Token read(int position) throws SnowflakeException {
        if (position >= size()) {
            throw new SnowflakeStreamException("Line " + line + ": Couldn't read Token on index " + position + "!");
        }

        return tokens.get(position);
    }

    private void skip() {
        position++;
    }

    public void skip(int offset) {
        for (int i = 0; i < offset; i++) {
            skip();
        }
    }

    public void reset() {
        position = 0;
    }

    public Token peek() {
        return tokens.get(tokens.size() - 1);
    }

    public boolean matches(int beginIndex, int endIndex, TokenStream stream) {
        boolean isSame = true;

        for (int index = 0; index < (endIndex - beginIndex); index++) {
            Token thisToken = tokens.get(index);
            Token otherToken = stream.getTokens().get(index);

            if (thisToken.getTokenType() != otherToken.getTokenType()) {
                isSame = false;
            }

            if (!thisToken.getValue().equals(otherToken.getValue())) {
                isSame = false;
            }
        }

        return isSame;
    }

    public int size() {
        return tokens.size();
    }

    @Override
    public String toString() {
        String result = "{";
        for (Token token : tokens) {
            result = result + "[" + token.toString() + "]";
        }

        result = result + "}";

        return result;
    }
}
