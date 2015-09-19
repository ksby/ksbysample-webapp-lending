package ksbysample.webapp.lending.exception;

public class WebApplicationRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 3845674924872653036L;

    public WebApplicationRuntimeException() {
        super();
    }

    public WebApplicationRuntimeException(String message) {
        super(message);
    }

    public WebApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebApplicationRuntimeException(Throwable cause) {
        super(cause);
    }

}
