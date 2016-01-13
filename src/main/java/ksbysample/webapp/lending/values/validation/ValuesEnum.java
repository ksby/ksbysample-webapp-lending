package ksbysample.webapp.lending.values.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { ValuesEnumValidator.class })
public @interface ValuesEnum {

    String message() default "{ksbysample.webapp.lending.values.validation.ValuesEnum.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    Class<? extends Enum<?>> enumClass();

    boolean allowEmpty() default false;
    
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ValuesEnum[] value();
    }

}
