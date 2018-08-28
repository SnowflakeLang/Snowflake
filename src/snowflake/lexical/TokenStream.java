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

    TokenStream(int line) {
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

    private void skip() {
        position++;
    }

    public void skip(int offset) {
        for (int i = 1; i <= offset; i++) {
            skip();
        }
    }

    private void reset() {
        position = 0;
    }

    public Token peek() {
        return tokens.get(tokens.size() - 1);
    }

    public boolean match(Token token, int index) {
        return tokens.get(index).equals(token);
    }

    public int size() {
        return tokens.size();
    }
}
