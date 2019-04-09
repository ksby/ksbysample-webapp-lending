package ksbysample.common.test.helper;

import org.junit.runner.Description;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class DescriptionWrapper implements TestContextWrapper {

    private Description description;

    public DescriptionWrapper(Description description) {
        this.description = description;
    }

    @Override
    public Collection<Annotation> getAnnotations() {
        return description.getAnnotations();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return description.getAnnotation(annotationType);
    }

    @Override
    public Class<?> getTestClass() {
        return description.getTestClass();
    }

}
