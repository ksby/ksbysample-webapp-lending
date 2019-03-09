package ksbysample.common.test.helper;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class ExtensionContextWrapper implements TestContextWrapper {

    private ExtensionContext context;

    public ExtensionContextWrapper(ExtensionContext context) {
        this.context = context;
    }

    @Override
    public Collection<Annotation> getAnnotations() {
        return context.getTestMethod()
                .flatMap(m -> Optional.ofNullable(m.getAnnotations()))
                .flatMap(a -> Optional.ofNullable(Arrays.asList(a)))
                .orElse(null);
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return context.getTestMethod()
                .flatMap(m -> Optional.ofNullable(m.getAnnotation(annotationType)))
                .orElse(null);
    }

    @Override
    public Class<?> getTestClass() {
        return context.getTestClass().orElse(null);
    }

}
