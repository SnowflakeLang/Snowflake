package snowflake.lexical;

import snowflake.exception.SnowflakeLexerException;
import snowflake.exception.SnowflakeException;
import snowflake.lexical.type.DataType;
import snowflake.lexical.type.KeywordType;
import snowflake.lexical.type.TokenType;

import javax.sound.midi.SoundbankResource;
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

        return constructStream(stream, str);
    }

    public TokenStream getStreamComparison(String str) throws SnowflakeException {
        TokenStream stream = new TokenStream(line);

        return constructStream(stream, str);
    }

    private TokenStream constructStream(TokenStream stream, String str) throws SnowflakeLexerException {
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

        for (KeywordType keywordType : KeywordType.values()) {
            if (str.startsWith(keywordType.getPattern())) {
                if (str.substring(keywordType.getPattern().length()).isEmpty()) {
                    values.put(new Token(keywordType, str.substring(0, keywordType.getPattern().length()).trim(), line), "");
                } else {
                    values.put(new Token(keywordType, str.substring(0, keywordType.getPattern().length()).trim(), line), str.substring(keywordType.getPattern().length()).trim());
                }

                return values;
            }
        }

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
                    values.put(new Token(tokenType, str.substring(0, 1).trim(), line), "");
                } else {
                    values.put(new Token(tokenType, str.substring(0, 1).trim(), line), str.substring(1).trim());
                }

                return values;
            }
        }

        return null;
    }

}
