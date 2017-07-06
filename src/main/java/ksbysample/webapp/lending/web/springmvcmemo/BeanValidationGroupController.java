package ksbysample.webapp.lending.web.springmvcmemo;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ???
 */
@Controller
@RequestMapping("/springMvcMemo/beanValidationGroup")
public class BeanValidationGroupController {

    private static final String THYMELEAF_TEMPLATE = "springmvcmemo/beanValidationGroup";

    private final Validator mvcValidator;

    /**
     * @param mvcValidator ???
     */
    public BeanValidationGroupController(Validator mvcValidator) {
        this.mvcValidator = mvcValidator;
    }

    /**
     * @param beanValidationGroupForm ???
     * @return ???
     */
    @RequestMapping
    public String beanValidationGroup(BeanValidationGroupForm beanValidationGroupForm) {
        return THYMELEAF_TEMPLATE;
    }

    /**
     * @param beanValidationGroupForm ???
     * @param bindingResult           ???
     * @return ???
     */
    @RequestMapping("/fileupload")
    public String fileupload(@Validated BeanValidationGroupForm beanValidationGroupForm
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return THYMELEAF_TEMPLATE;
        }

        return THYMELEAF_TEMPLATE;
    }

    /**
     * @param beanValidationGroupForm ???
     * @param bindingResult           ???
     * @param editFormChecker         ???
     * @return ???
     */
    @RequestMapping("/edit")
    public String edit(@Validated BeanValidationGroupForm beanValidationGroupForm
            , BindingResult bindingResult
            , EditFormChecker editFormChecker) {
        mvcValidator.validate(editFormChecker, bindingResult);
        if (bindingResult.hasErrors()) {
            return THYMELEAF_TEMPLATE;
        }

        return THYMELEAF_TEMPLATE;
    }

    /**
     * @param beanValidationGroupForm ???
     * @param bindingResult           ???
     * @param sendmailFormChecker     ???
     * @return ???
     */
    @RequestMapping("/sendmail")
    public String sendmail(@Validated BeanValidationGroupForm beanValidationGroupForm
            , BindingResult bindingResult
            , SendmailFormChecker sendmailFormChecker) {
        mvcValidator.validate(sendmailFormChecker, bindingResult);
        if (bindingResult.hasErrors()) {
            return THYMELEAF_TEMPLATE;
        }

        return THYMELEAF_TEMPLATE;
    }

}
