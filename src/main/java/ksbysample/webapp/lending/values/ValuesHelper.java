package ksbysample.webapp.lending.values;

import com.google.common.reflect.ClassPath;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;

@Component("vh")
public class ValuesHelper {

    private final Map<String, String> valuesObjList;
    
    private ValuesHelper() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        valuesObjList = ClassPath.from(loader).getTopLevelClasses(this.getClass().getPackage().getName())
                .stream()
                .filter(classInfo -> !StringUtils.equals(classInfo.getName(), this.getClass().getName()))
                .collect(Collectors.toMap(ClassPath.ClassInfo::getSimpleName, ClassPath.ClassInfo::getName));
    }

    public String getValue(String classSimpleName, String valueName)
            throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName(this.valuesObjList.get(classSimpleName));
        Method method = clazz.getMethod("valueOf", String.class);
        Values value = (Values) method.invoke(null, valueName);
        return value.getValue();
    }
    
    public String getText(String classSimpleName, String value)
            throws ClassNotFoundException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName(this.valuesObjList.get(classSimpleName));
        Method method = clazz.getMethod("getText", String.class);
        String result = (String) method.invoke(null, value);
        return result;
    }

    public Values[] values(String classSimpleName)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName(this.valuesObjList.get(classSimpleName));
        Method method = clazz.getMethod("values");
        Values[] result = (Values[]) method.invoke(null);
        return result;
    }
    
}
