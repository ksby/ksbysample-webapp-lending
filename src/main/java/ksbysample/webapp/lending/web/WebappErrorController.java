package ksbysample.webapp.lending.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/error")
public class WebappErrorController implements ErrorController {

    @Autowired
    private ExceptionHandlerAdvice exceptionHandlerAdvice;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping
    public ModelAndView index(Exception e, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView model = exceptionHandlerAdvice.handleException(e, request, response);
        return model;
    }
    
}
