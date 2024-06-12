package cz.muni.fi.obs.exceptions;

// fixme: add meaningful messages
public class ResourceNotFoundException extends RuntimeException {

    private Object id;

    private Class<?> resourceClass;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ResourceNotFoundException(Class<?> resourceClass, Object resourceId) {
        this.resourceClass = resourceClass;
        this.id = resourceId;
    }
}
