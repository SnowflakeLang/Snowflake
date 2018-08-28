package snowflake.lexical.type;

public enum KeywordType implements AbstractType {

    IF("if"),
    ELSE("else"),
    ELSEIF("elseif"),
    FOR("for"),
    WHILE("while"),
    USE("use"),
    RETURN("return"),
    NULL("null");

    private String pattern;

    KeywordType(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
