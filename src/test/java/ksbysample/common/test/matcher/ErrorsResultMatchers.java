package ksbysample.common.test.matcher;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.ModelResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class ErrorsResultMatchers extends ModelResultMatchers {

    private ErrorsResultMatchers() {
    }

    public static ErrorsResultMatchers errors() {
        return new ErrorsResultMatchers();
    }

    /**
     * @param name  ???
     * @param error ???
     * @return ???
     */
    public ResultMatcher hasGlobalError(String name, String error) {
        return mvcResult -> {
            BindingResult bindingResult = getBindingResult(mvcResult.getModelAndView(), name);
            List<ObjectError> objectErrorList = bindingResult.getGlobalErrors();
            List<String> objectErrorListAsCode
                    = objectErrorList.stream().map(ObjectError::getCode).collect(Collectors.toList());
            assertThat(objectErrorListAsCode).contains(error);
        };
    }

    /**
     * @param name      ???
     * @param fieldName ???
     * @param error     ???
     * @return ???
     */
    public ResultMatcher hasFieldError(String name, String fieldName, String error) {
        return mvcResult -> {
            BindingResult bindingResult = getBindingResult(mvcResult.getModelAndView(), name);
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors(fieldName);
            List<String> fieldErrorListAsCode
                    = fieldErrorList.stream().map(FieldError::getCode).collect(Collectors.toList());
            assertThat(fieldErrorListAsCode).contains(error);
        };
    }

    private BindingResult getBindingResult(ModelAndView mav, String name) {
        BindingResult bindingResult = (BindingResult) mav.getModel().get(BindingResult.MODEL_KEY_PREFIX + name);
        assertTrue("No BindingResult for attribute: " + name, bindingResult != null);
        return bindingResult;
    }

}
