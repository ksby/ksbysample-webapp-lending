package ksbysample.common.test.rule.db;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface BaseTestData {

    /**
     * @return ???
     */
    String value();

}
