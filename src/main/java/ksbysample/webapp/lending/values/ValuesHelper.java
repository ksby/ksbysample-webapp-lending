package ksbysample.webapp.lending.values;

import com.google.common.reflect.ClassPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ???
 */
@Component("vh")
public final class ValuesHelper {

    private final Map<String, String> valuesObjList;

    private ValuesHelper(@Value("${valueshelper.classpath.prefix:}") String classpathPrefix) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        valuesObjList = ClassPath.from(loader)
                .getTopLevelClassesRecursive(classpathPrefix + this.getClass().getPackage().getName())
                .stream()
                .filter(classInfo -> {
                    try {
                        Class<?> clazz = Class.forName(classInfo.getName().replace(classpathPrefix, ""));
                        return !clazz.equals(Values.class) && Values.class.isAssignableFrom(clazz);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toMap(classInfo -> classInfo.getSimpleName()
                        , classInfo -> classInfo.getName().replace(classpathPrefix, "")));
    }

    /**
     * @param classSimpleName ???
     * @param valueName       ???
     * @param <T>             ???
     * @return ???
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & Values> String getValue(String classSimpleName, String valueName)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        T val = Enum.valueOf(enumType, valueName);
        return val.getValue();
    }

    /**
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
     * @param classSimpleName ???
     * @param value           ???
     * @param <T>             ???
     * @return ???
     * @throws ClassNotFoundException
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
     * @param classSimpleName ???
     * @param <T>             ???
     * @return ???
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & Values> T[] values(String classSimpleName)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        return enumType.getEnumConstants();
    }

}
