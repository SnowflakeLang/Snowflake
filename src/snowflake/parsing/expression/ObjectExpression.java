package snowflake.parsing.expression;

import snowflake.Visitor;
import snowflake.lexical.type.TokenType;

public class ObjectExpression extends Expression {

    private int line;

    private Expression left, right;
    private TokenType operator;

    public ObjectExpression(int line, Expression left, Expression right, TokenType operator) {
        this.line = line;

        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public int getLine() {
        return line;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public TokenType getOperator() {
        return operator;
    }

    @Override
    void accept(Visitor visitor) {

    }
}
