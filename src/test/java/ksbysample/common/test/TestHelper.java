package ksbysample.common.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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
        for (Field field : form.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(form) == null) {
                request = request.param(field.getName(), "");
            }
            else if (field.get(form) instanceof List<?>) {
                for (Object str : (List<?>)field.get(form)) {
                    request = request.param(field.getName(), str.toString());
                }
            }
            else {
                request = request.param(field.getName(), field.get(form).toString());
            }
        }
        return request;
    }

    /**
     * EntityクラスとFormクラスの値が同じかチェックする
     * 
     * @param entity
     * @param form
     * @throws IllegalAccessException
     */
    public static void assertEntityByForm(Object entity, Object form) throws IllegalAccessException {
        for (Field entityField : entity.getClass().getDeclaredFields()) {
            entityField.setAccessible(true);
            try {
                Field formField = form.getClass().getDeclaredField(entityField.getName());
                formField.setAccessible(true);
                assertThat(entityField.get(entity), is(formField.get(form)));
            }
            catch (NoSuchFieldException ignored) {}
        }
    }

}
