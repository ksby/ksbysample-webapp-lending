package ksbysample.common.test.helper;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestHelper {

    /**
     * Formクラスをパラメータに持つMockHttpServletRequestBuilderクラスのインスタンスを生成する
     * 
     * @param urlTemplate
     * @param form
     * @return
     * @throws IllegalAccessException
     */
    public static MockHttpServletRequestBuilder postForm(String urlTemplate, Object form) throws IllegalAccessException {
        MockHttpServletRequestBuilder request = post(urlTemplate).contentType(MediaType.APPLICATION_FORM_URLENCODED);
        setParameterFromForm(request, form, null, null);
        return request;
    }

    /**
     * FormValidator クラスのテスト等で使用するための Errors オブジェクト
     * ( MapBindingResult クラスのインスタンス ) を生成する
     * 
     * @return
     */
    public static Errors createErrors() {
        return new MapBindingResult(new HashMap<String, String>(), "");
    }

    /**
     * Form クラスのインスタンスのフィールド名と値を request にセットする
     *
     * @param request
     * @param form
     * @throws IllegalAccessException
     */
    private static void setParameterFromForm(MockHttpServletRequestBuilder request, Object form, String fieldName, Integer arrayIndex) throws IllegalAccessException {
        for (Field field : form.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(form) == null) {
                request = request.param(makeParameterName(fieldName, field.getName(), arrayIndex), "");
            }
            else if (field.get(form) instanceof ArrayList<?>) {
                Integer i = 0;
                for (Object obj : (List<?>)field.get(form)) {
                    if (isPrimitiveOrString(obj)) {
                        request = request.param(makeParameterName(fieldName, field.getName(), i), obj.toString());
                    }
                    else {
                        setParameterFromForm(request, obj, makeParameterName(fieldName, field.getName(), arrayIndex), i);
                    }
                    i++;
                }
            }
            else if (isPrimitiveOrString(field.get(form))) {
                request = request.param(makeParameterName(fieldName, field.getName(), arrayIndex), field.get(form).toString());
            }
            else {
                setParameterFromForm(request, field.get(form), makeParameterName(fieldName, field.getName(), arrayIndex), null);
            }
        }
    }

    /**
     * 指定されたオブジェクトがプリミティブ型かチェックする
     *
     * @param obj
     * @return
     */
    private static boolean isPrimitiveOrString(Object obj) {
        boolean result = false;
        if (obj instanceof Byte) result = true;
        else if (obj instanceof Short) result = true;
        else if (obj instanceof Integer) result = true;
        else if (obj instanceof Long) result = true;
        else if (obj instanceof Character) result = true;
        else if (obj instanceof Float) result = true;
        else if (obj instanceof Double) result = true;
        else if (obj instanceof Boolean) result = true;
        else if (obj instanceof String) result = true;
        return result;
    }

    /**
     * MockHttpServletRequestBuilderクラスのインスタンス ( request ) にセットするパラメータ名を生成する
     *
     * @param rootFieldName
     * @param fieldName
     * @param arrayIndex
     * @return
     */
    private static String makeParameterName(String rootFieldName, String fieldName, Integer arrayIndex) {
        StringBuilder sb = new StringBuilder();
        if (rootFieldName != null) {
            sb.append(rootFieldName);
            if (arrayIndex != null) {
                sb.append("[");
                sb.append(arrayIndex);
                sb.append("]");
            }
            sb.append(".");
        }
        sb.append(fieldName);
        return sb.toString();
    }
    
}
