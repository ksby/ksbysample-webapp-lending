package ksbysample.webapp.lending.values;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ???
 */
@Component("vh")
public final class ValuesHelper {

    private final Map<String, String> valuesObjList;

    private ValuesHelper() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
        Set<BeanDefinition> beans = provider.findCandidateComponents(this.getClass().getPackage().getName());
        valuesObjList = beans.stream()
                .map(bean -> {
                    try {
                        return Class.forName(bean.getBeanClassName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(clazz -> !clazz.equals(Values.class) && Values.class.isAssignableFrom(clazz))
                .collect(Collectors.toMap(clazz -> clazz.getSimpleName(), clazz -> clazz.getName()));
    }

    /**
     * ???
     *
     * @param classSimpleName ???
     * @param valueName       ???
     * @param <T>             ???
     * @return ???
     * @throws ClassNotFoundException ???
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & Values> String getValue(String classSimpleName, String valueName)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        T val = Enum.valueOf(enumType, valueName);
        return val.getValue();
    }

    /**
     * ???
     *
     * @param enumType  ???
     * @param valueName ???
     * @param <T>       ???
     * @return ???
     */
    public <T extends Enum<T> & Values> String getValue(Class<T> enumType, String valueName) {
        T val = Enum.valueOf(enumType, valueName);
        return val.getValue();
    }

    /**
     * ???
     *
     * @param classSimpleName ???
     * @param value           ???
     * @param <T>             ???
     * @return ???
     * @throws ClassNotFoundException ???
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & Values> String getText(String classSimpleName, String value)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        String result = "";
        for (T val : enumType.getEnumConstants()) {
            if (val.getValue().equals(value)) {
                result = val.getText();
                break;
            }
        }
        return result;
    }

    /**
     * ???
     *
     * @param enumType ???
     * @param value    ???
     * @param <T>      ???
     * @return ???
     */
    public <T extends Enum<T> & Values> String getText(Class<T> enumType, String value) {
        String result = "";
        for (T val : enumType.getEnumConstants()) {
            if (val.getValue().equals(value)) {
                result = val.getText();
                break;
            }
        }
        return result;
    }

    /**
     * ???
     *
     * @param classSimpleName ???
     * @param <T>             ???
     * @return ???
     * @throws ClassNotFoundException ???
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & Values> T[] values(String classSimpleName)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        return enumType.getEnumConstants();
    }

}
