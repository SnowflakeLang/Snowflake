package snowflake;

import snowflake.exception.SnowflakeException;
import snowflake.lexical.Lexer;
import snowflake.lexical.TokenStream;

public class Runtime {

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        String code = "(\"Identifier!\") + (123);";

        try {
            TokenStream stream = lexer.getStream(code, 1);

            int index = 0;
            while (stream.getPosition() <= stream.getTokens().size() - 1) {
                System.out.println(stream.read().getValue());

                index++;
            }
        } catch (SnowflakeException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
