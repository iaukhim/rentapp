package org.example.rentapp.exceptions;

public class NoEntityFoundException extends RuntimeException {

    private Long id;

    private Class<?> entityClass;

    public NoEntityFoundException() {
        super("We couldn't find entity with such id");
    }

    public NoEntityFoundException(String message) {
        super(message);
    }

    public NoEntityFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEntityFoundException(Throwable cause) {
        super(cause);
    }

    public NoEntityFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoEntityFoundException(Long id, Class<?> entityClass) {
        super("No entity " + entityClass.getSimpleName() + " with id " + id);
        this.id = id;
        this.entityClass = entityClass;
    }

    public NoEntityFoundException(Throwable cause, Long id, Class<?> entityClass) {
        super("No entity " + entityClass.getSimpleName() + " with id " + id, cause);
        this.id = id;
        this.entityClass = entityClass;
    }
}
