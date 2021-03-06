package ksbysample.common.test.helper;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.RequestBuilder;

import javax.servlet.ServletContext;

public class SimpleRequestBuilder implements RequestBuilder {

    private final MockHttpServletRequest request;

    public SimpleRequestBuilder(MockHttpServletRequest request) {
        this.request = request;
    }

    @Override
    public MockHttpServletRequest buildRequest(ServletContext servletContext) {
        return request;
    }

}
