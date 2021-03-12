package uk.me.pilgrim.invoke.exceptions;

public enum CompilerExceptionType implements ServicesExceptionType {
    DUPLICATE_OBJECT_NAME;

    @Override
    public String getName() {
        return name();
    }
}
