package ksbysample.webapp.lending.web;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ???
 */
@Controller
@RequestMapping("/error")
public class WebappErrorController implements ErrorController {

    private final ExceptionHandlerAdvice exceptionHandlerAdvice;

    /**
     * @param exceptionHandlerAdvice ???
     */
    public WebappErrorController(ExceptionHandlerAdvice exceptionHandlerAdvice) {
        this.exceptionHandlerAdvice = exceptionHandlerAdvice;
    }

    /**
     * @return ???
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * @param e        ???
     * @param request  ???
     * @param response ???
     * @return ???
     * @throws IOException
     */
    @RequestMapping
    public ModelAndView index(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        return exceptionHandlerAdvice.handleException(e, request, response);
    }

}
