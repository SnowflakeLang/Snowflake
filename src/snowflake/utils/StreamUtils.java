package snowflake.utils;

import snowflake.lexical.Token;
import snowflake.lexical.TokenStream;

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

}
