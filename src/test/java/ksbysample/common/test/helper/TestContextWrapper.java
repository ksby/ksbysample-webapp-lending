package ksbysample.common.test.helper;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface TestContextWrapper {

    Collection<Annotation> getAnnotations();

    <T extends Annotation> T getAnnotation(Class<T> annotationType);

    Class<?> getTestClass();

}
