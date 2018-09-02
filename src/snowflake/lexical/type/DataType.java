package snowflake.lexical.type;

import snowflake.lexical.Token;

public enum DataType implements AbstractType {

    //Data Types
    IDENTIFIER("[a-zA-Z]([a-zA-Z0-9]*)?"),
    INTEGER_LITERAL("^\\d+"),
    STRING_LITERAL("\"(.*)?\"");

    private String pattern;

    DataType(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public static boolean isType(Token token) {
        for (DataType type : values()) {
            if (type == token.getTokenType()) {
                return true;
            }
        }

        return false;
    }
}