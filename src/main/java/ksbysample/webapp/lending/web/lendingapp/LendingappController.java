package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    
    @RequestMapping
    public String index(@Validated LendingappParamForm lendingappParamForm
            , BindingResult bindingResultForLendingappParamForm
            , LendingappForm lendingappForm) {
        if (bindingResultForLendingappParamForm.hasErrors()) {
            throw new WebApplicationRuntimeException(
                    messagesPropertiesHelper.getMessage("LendingappForm.lendingAppId.emptyerr", null));
        }

        LendingApp lendingApp = lendingappService.getLendingApp(lendingappParamForm.getLendingAppId());
        List<LendingBook> lendingBookList = lendingappService.getLendingBookList(lendingappParamForm.getLendingAppId());
        lendingappForm.setLendingApp(lendingApp);
        lendingappForm.setLendingBookList(lendingBookList);
        
        return "lendingapp/lendingapp";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public String apply() {
        return "lendingapp/lendingapp";
    }

    @RequestMapping(value = "/temporarySave", method = RequestMethod.POST)
    public String temporarySave() {
        return "lendingapp/lendingapp";
    }

}
