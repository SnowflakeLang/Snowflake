package snowflake.utils;

public class Value {

    private TypeUtils.ObjectType type;
    private Object value;

    public Value(TypeUtils.ObjectType type) {
        this.type = type;
    }

    public Value(TypeUtils.ObjectType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public TypeUtils.ObjectType getType() {
        return type;
    }

    public Object getValue() {
        if (type == null || value == null) {
            return null;
        } else if (type == TypeUtils.ObjectType.INT) {
            return Integer.valueOf(value.toString());
        } else if (type == TypeUtils.ObjectType.STRING) {
            return value.toString();
        } else {
            return value;
        }
    }

    public void setValue(Object value) {
        if (type == null || value == null) {
            this.value = value;
        } else if (type == TypeUtils.ObjectType.INT) {
            this.value = Integer.valueOf(value.toString());
        } else if (type == TypeUtils.ObjectType.STRING) {
            this.value = value.toString();
        } else {
            this.value = value;
        }
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (!(other instanceof Value)) {
            return false;
        } else if (((Value) other).getType() == null && getType() != null) {
            return false;
        } else if (((Value) other).getType() != null && getType() == null) {
            return false;
        } else if (((Value) other).getType() != null && getType() != null && !((Value) other).getType().equals(getType())) {
            return false;
        } else if (((Value) other).getValue() == null && getValue() != null) {
            return false;
        } else if (((Value) other).getValue() != null && getValue() == null) {
            return false;
        } else if (((Value) other).getValue() != null && getValue() != null && !((Value) other).getValue().equals(getValue())) {
            return false;
        }

        return true;
    }
}
