package snowflake.lexical;

import snowflake.exception.SnowflakeException;
import snowflake.exception.SnowflakeStreamException;

import java.util.ArrayList;
import java.util.List;

public class TokenStream {

    private List<Token> tokens;
    private Token current;

    private int line;
    private int position;

    public TokenStream(int line) {
        this.tokens = new ArrayList<>();
        this.line = line;

        position = 0;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public Token getCurrent() {
        return current;
    }

    public int getPosition() {
        return position;
    }

    public void write(Token token) {
        tokens.add(token);
    }

    public Token read() throws SnowflakeException {
        if (position == tokens.size()) {
            throw new SnowflakeStreamException("Line " + line + ": Couldn't read next Token in stream!");
        }

        Token token = tokens.get(position);
        position++;

        return token;
    }

    public void skip() {
        position++;
    }

    public void skip(int offset) {
        for (int i = 1; i <= offset; i++) {
            skip();
        }
    }

    public void recall() {
        position--;
    }

    public void recall(int offset) {
        for (int i = 1; i <= offset; i++) {
            recall();
        }
    }

    public Token peek() {
        return tokens.get(tokens.size() - 1);
    }

    public boolean match(Token token, int index) {
        return tokens.get(index).equals(token);
    }

}
