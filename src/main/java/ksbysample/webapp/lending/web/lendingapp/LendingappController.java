package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/lendingapp")
public class LendingappController {

    @Autowired
    private LendingappService lendingappService;

    @Autowired
    private MessagesPropertiesHelper messagesPropertiesHelper;

    @Autowired
    private LendingappFormValidator lendingappFormValidator;

    @InitBinder(value = "lendingappForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(lendingappFormValidator);
    }
    
    @RequestMapping
    public String index(@Validated LendingappParamForm lendingappParamForm
            , BindingResult bindingResultForLendingappParamForm
            , LendingappForm lendingappForm) {
        if (bindingResultForLendingappParamForm.hasErrors()) {
            throw new WebApplicationRuntimeException(
                    messagesPropertiesHelper.getMessage("LendingappForm.lendingAppId.emptyerr", null));
        }

        // 画面に表示するデータを取得する
        setDispData(lendingappParamForm.getLendingAppId(), lendingappForm);

        return "lendingapp/lendingapp";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public String apply(@Validated LendingappForm lendingappForm
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "lendingapp/lendingapp";
        }

        // 入力された内容で申請する
        lendingappService.apply(lendingappForm);

        // 画面に表示するデータを取得する
        setDispData(lendingappForm.getLendingApp().getLendingAppId(), lendingappForm);
        
        return "lendingapp/lendingapp";
    }

    @RequestMapping(value = "/temporarySave", method = RequestMethod.POST)
    public String temporarySave() {
        return "lendingapp/lendingapp";
    }

    private void setDispData(Long lendingAppId, LendingappForm lendingappForm) {
        LendingApp lendingApp = lendingappService.getLendingApp(lendingAppId);
        List<LendingBook> lendingBookList = lendingappService.getLendingBookList(lendingAppId);
        lendingappForm.setLendingApp(lendingApp);
        lendingappForm.setLendingBookList(lendingBookList);
    }
    
}
