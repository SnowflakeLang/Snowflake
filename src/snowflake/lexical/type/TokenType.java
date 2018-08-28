package snowflake.lexical.type;

public enum TokenType implements AbstractType {

    //Types of surroundings
    OPAREN("("),
    CPAREN(")"),
    OBRACKET("["),
    CBRACKET("]"),
    OBRACE("{"),
    CBRACE("}"),

    //Punctuations
    DOT("."),
    COMMA(","),
    SEMI_COLON(";"),
    COLON(":"),

    //Binary Operators
    PLUS("+"),
    MINUS("-"),
    ASTERISK("*"),
    FSLASH("/"),
    PERCENT("%"),

    //(Comparisons in Binary Operators)
    AND("&"),
    EQUAL("="),
    EXCLAMATION("!"),
    GTHAN(">"),
    LTHAN("<");

    private String pattern;

    TokenType(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

}
