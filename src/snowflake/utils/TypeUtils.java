package snowflake.utils;

import snowflake.lexical.Token;

public class TypeUtils {

    public enum ObjectType {
        VOID("Void"),
        INT("Integer"),
        STRING("String");

        private String value;

        ObjectType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static ObjectType isType(Token token) {
        String value = token.getValue();

        for (ObjectType type : ObjectType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }

        return null;
    }

}
