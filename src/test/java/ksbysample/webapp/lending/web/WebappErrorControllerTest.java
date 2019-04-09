package ksbysample.webapp.lending.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WebappErrorControllerTest {

    private static final String REQUEST_URI_NOT_FOUND = "/notFoundUrl";

    @Autowired
    private WebappErrorController webappErrorController;

    @SuppressWarnings("unchecked")
    @Test
    void testIndex() throws Exception {
        // setup
        Exception e = new Exception("");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/error");
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, REQUEST_URI_NOT_FOUND);
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());

        // when
        ModelAndView modelAndView = webappErrorController.index(e, request, response);

        // then
        Map<String, Object> model = modelAndView.getModel();
        assertThat(model.get("errorMessage")).isEqualTo(HttpStatus.NOT_FOUND.value() + " " + HttpStatus.NOT_FOUND.getReasonPhrase());
        List<String> errorInfoList = (List<String>) model.get("errorInfoList");
        assertThat(errorInfoList)
                .contains("エラーが発生したURL：　" + REQUEST_URI_NOT_FOUND);
    }

}
