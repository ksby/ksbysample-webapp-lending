package ksbysample.webapp.lending.aspect.logging;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * ???
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface LoggingEventName {

    /**
     * ???
     *
     * @return ???
     */
    String value();

}
