package snowflake.utils;

import snowflake.exception.SnowflakeException;
import snowflake.lexical.Token;
import snowflake.lexical.TokenStream;

import java.util.ArrayList;

public class StreamUtils {

    public static boolean compares(TokenStream parent, TokenStream child) {
        boolean isSame = true;

        if (parent.getTokens().size() != child.getTokens().size()) {
            return false;
        }

        for (int i = 0; i < parent.getTokens().size(); i++) {
            Token parentToken = parent.getTokens().get(i);
            Token childToken = child.getTokens().get(i);

            if ((!(parentToken.getValue().equals(childToken.getValue()))) || (parentToken.getTokenType() != childToken.getTokenType())) {
                isSame = false;
            }
        }

        return isSame;
    }

    public static TokenStream getStream(TokenStream stream, int offset) {
        TokenStream returnStream = new TokenStream(stream.getLine());
        ArrayList<Token> tokens = new ArrayList<>(stream.getTokens().subList(offset, stream.size() - 1));

        for (Token token : tokens) {
            returnStream.write(token);
        }

        return returnStream;
    }

    public static boolean hasNextStream(TokenStream stream) {
        return stream.read() != null;
    }

    public static boolean hasNextStream(TokenStream stream, int offset) {
        try {
            return stream.read(offset) != null;
        } catch (SnowflakeException ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
