package snowflake.lexical;

import snowflake.lexical.type.AbstractType;

public class Token {

    private AbstractType tokenType;
    private String value;

    //Implement this for error handling
    private int line;

    public Token(AbstractType tokenType, String value, int line) {
        this.tokenType = tokenType;
        this.value = value;
        this.line = line;
    }

    public AbstractType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "Type=" + tokenType.toString() + ",Value=" + value;
    }
}
