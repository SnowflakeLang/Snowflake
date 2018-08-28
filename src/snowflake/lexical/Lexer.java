package snowflake.lexical;

import snowflake.exception.SnowflakeLexerException;
import snowflake.exception.SnowflakeException;
import snowflake.lexical.type.DataType;
import snowflake.lexical.type.KeywordType;
import snowflake.lexical.type.TokenType;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private HashMap<Pattern, DataType> patterns = new HashMap<>();

    private int line = 0;

    public Lexer() {
        for (DataType type : DataType.values()) {
            patterns.put(Pattern.compile("^(" + type.getPattern() + ")"), type);
        }
    }

    public TokenStream getStream(String str, int line) throws SnowflakeException {
        TokenStream stream = new TokenStream(line);

        while (!str.isEmpty()) {
            HashMap<Token, String> values = evaluate(str, line);

            if (values == null) {
                throw new SnowflakeLexerException("Line " + line + ": Invalid token \"" + str + "\"!");
            } else {
                Token token = (Token) values.keySet().toArray()[0];

                str = values.get(token);

                stream.write(token);
            }
        }

        return stream;
    }

    private HashMap<Token, String> evaluate(String str, int line) {
        str = str.trim();

        HashMap<Token, String> values = new HashMap<>();

        for (Pattern pattern : patterns.keySet()) {
            Matcher matcher = pattern.matcher(str);

            if (matcher.find()) {
                if (matcher.start() == 0) {
                    values.put(new Token(patterns.get(pattern), matcher.group().trim(), line), str.substring(matcher.end()));

                    return values;
                }
            }
        }

        for (TokenType tokenType : TokenType.values()) {
            if (str.startsWith(tokenType.getPattern())) {
                if (str.substring(1).isEmpty()) {
                    values.put(new Token(tokenType, str.substring(0, 1), line), "");
                } else {
                    values.put(new Token(tokenType, str.substring(0, 1), line), str.substring(1));
                }

                return values;
            }
        }

        for (KeywordType keywordType : KeywordType.values()) {
            if (str.startsWith(keywordType.getPattern())) {
                if (str.substring(keywordType.getPattern().length() - 1).isEmpty()) {
                    values.put(new Token(keywordType, str.substring(0, keywordType.getPattern().length() - 1), line), "");
                } else {
                    values.put(new Token(keywordType, str.substring(0, keywordType.getPattern().length() - 1), line), str.substring(keywordType.getPattern().length() - 1));
                }

                return values;
            }
        }

        return null;
    }

}
