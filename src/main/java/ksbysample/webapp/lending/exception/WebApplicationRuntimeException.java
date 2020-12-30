package ksbysample.webapp.lending.exception;

/**
 * ???
 */
public class WebApplicationRuntimeException extends RuntimeException {

    @SuppressWarnings("PMD.FieldNamingConventions")
    private static final long serialVersionUID = 3845674924872653036L;

    /**
     * ???
     */
    public WebApplicationRuntimeException() {
        super();
    }

    /**
     * ???
     *
     * @param message ???
     */
    public WebApplicationRuntimeException(String message) {
        super(message);
    }

    /**
     * ???
     *
     * @param message ???
     * @param cause   ???
     */
    public WebApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * ???
     *
     * @param cause ???
     */
    public WebApplicationRuntimeException(Throwable cause) {
        super(cause);
    }

}
