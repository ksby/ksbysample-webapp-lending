package ksbysample.webapp.lending.web;

import ksbysample.common.test.extension.db.TestDataExtension;
import ksbysample.common.test.extension.mockmvc.SecurityMockMvcExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ExceptionHandlerAdviceTest {

    private static final String MESSAGE_EXCEPTION = "Exception が throw されました";
    private static final String MESSAGE_RUNTIMEEXCEPTION = "RuntimeException が throw されました";

    @RegisterExtension
    @Autowired
    public TestDataExtension testDataExtension;

    @RegisterExtension
    @Autowired
    public SecurityMockMvcExtension mvc;

    @Controller
    @RequestMapping("/exceptionHandlerAdviceTest")
    public static class ExceptionHandlerAdviceTestController {
        @RequestMapping("/exception")
        public String exception() throws Exception {
            if (true) {
                throw new Exception(MESSAGE_EXCEPTION);
            }
            return null;
        }

        @RequestMapping("/runtimeException")
        public String runtimeException() {
            if (true) {
                throw new RuntimeException(MESSAGE_RUNTIMEEXCEPTION);
            }
            return null;
        }
    }

    @Test
    void testHandleException_Controllerクラス内でExceptionがthrowされた場合() throws Exception {
        // when
        MvcResult result = mvc.authTanakaTaro.perform(get("/exceptionHandlerAdviceTest/exception?parama=1&paramb=2"))
                // 以下のコメントを解除すると MockMvc で取得したコンテンツが標準出力に出力される
                // .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("error"))
                .andReturn();

        // then
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains("エラーが発生したURI：　/exceptionHandlerAdviceTest/exception")
                .contains("<span>" + MESSAGE_EXCEPTION + "</span>")
                .contains("parama：1")
                .contains("paramb：2")
                .contains("<span>java.lang.Exception: " + MESSAGE_EXCEPTION + "</span>");
    }

    @Test
    void testHandleException_Controllerクラス内でRuntimeExceptionがthrowされた場合() throws Exception {
        // when
        MvcResult result = mvc.authTanakaTaro.perform(get("/exceptionHandlerAdviceTest/runtimeException"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("error"))
                .andReturn();

        // then
        String content = result.getResponse().getContentAsString();
        assertThat(content)
                .contains("エラーが発生したURI：　/exceptionHandlerAdviceTest/runtimeException")
                .contains("<span>" + MESSAGE_RUNTIMEEXCEPTION + "</span>")
                .contains("<span>java.lang.RuntimeException: " + MESSAGE_RUNTIMEEXCEPTION + "</span>");
    }

}
