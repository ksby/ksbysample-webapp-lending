package ksbysample.webapp.lending.values;

import com.google.common.reflect.ClassPath;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Component("vh")
public class ValuesHelper {

    private final Map<String, String> valuesObjList;

    private ValuesHelper() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        valuesObjList = ClassPath.from(loader).getTopLevelClassesRecursive(this.getClass().getPackage().getName())
                .stream()
                .filter(classInfo -> {
                    try {
                        Class<?> clazz = Class.forName(classInfo.getName());
                        return !clazz.equals(Values.class) && Values.class.isAssignableFrom(clazz);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toMap(ClassPath.ClassInfo::getSimpleName, ClassPath.ClassInfo::getName));
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & Values> String getValue(String classSimpleName, String valueName)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        T val = Enum.valueOf(enumType, valueName);
        return val.getValue();
    }

    public <T extends Enum<T> & Values> String getValue(Class<T> enumType, String valueName) {
        T val = Enum.valueOf(enumType, valueName);
        return val.getValue();
    }

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

    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & Values> T[] values(String classSimpleName)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        return enumType.getEnumConstants();
    }

}
