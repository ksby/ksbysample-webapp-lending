package ksbysample.common.test.rule.db;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD })
@Retention(RUNTIME)
@Documented
@Repeatable(TestSqlList.class)
public @interface TestSql {

    long order() default 1;

    String sql();

}
