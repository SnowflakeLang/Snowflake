package snowflake.lexical.type;

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
}