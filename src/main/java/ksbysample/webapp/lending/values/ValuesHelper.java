package ksbysample.webapp.lending.values;

import com.google.common.reflect.ClassPath;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Component("vh")
public class ValuesHelper<T extends Enum<T> & Values> {

    private final Map<String, String> valuesObjList;

    private ValuesHelper() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        valuesObjList = ClassPath.from(loader).getTopLevelClasses(this.getClass().getPackage().getName())
                .stream()
                .filter(classInfo -> !classInfo.getName().equals(this.getClass().getName()))
                .collect(Collectors.toMap(ClassPath.ClassInfo::getSimpleName, ClassPath.ClassInfo::getName));
    }

    @SuppressWarnings("unchecked")
    public String getValue(String classSimpleName, String valueName)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        T val = Enum.valueOf(enumType, valueName);
        return val.getValue();
    }

    @SuppressWarnings("unchecked")
    public String getText(String classSimpleName, String value)
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

    @SuppressWarnings("unchecked")
    public T[] values(String classSimpleName)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        return enumType.getEnumConstants();
    }

}
