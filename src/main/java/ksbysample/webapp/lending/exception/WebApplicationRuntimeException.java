package ksbysample.webapp.lending.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

/**
 * ???
 */
@StandardException
public class WebApplicationRuntimeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3845674924872653036L;

}
