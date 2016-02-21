package ksbysample.webapp.lending.web.springmvcmemo;

import ksbysample.webapp.lending.web.springmvcmemo.BeanValidationGroupForm.EditGroup;
import ksbysample.webapp.lending.web.springmvcmemo.BeanValidationGroupForm.FileUploadGroup;
import ksbysample.webapp.lending.web.springmvcmemo.BeanValidationGroupForm.SendmailGroup;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/springMvcMemo/beanValidationGroup")
public class BeanValidationGroupController {

    @RequestMapping
    public String beanValidationGroup(BeanValidationGroupForm beanValidationGroupForm) {
        return "springmvcmemo/beanValidationGroup";
    }

    @RequestMapping("/fileupload")
    public String fileupload(@Validated(FileUploadGroup.class) BeanValidationGroupForm beanValidationGroupForm
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "springmvcmemo/beanValidationGroup";
        }

        return "springmvcmemo/beanValidationGroup";
    }

    @RequestMapping("/edit")
    public String edit(@Validated(EditGroup.class) BeanValidationGroupForm beanValidationGroupForm
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "springmvcmemo/beanValidationGroup";
        }

        return "springmvcmemo/beanValidationGroup";
    }

    @RequestMapping("/sendmail")
    public String sendmail(@Validated(SendmailGroup.class) BeanValidationGroupForm beanValidationGroupForm
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "springmvcmemo/beanValidationGroup";
        }

        return "springmvcmemo/beanValidationGroup";
    }

}
