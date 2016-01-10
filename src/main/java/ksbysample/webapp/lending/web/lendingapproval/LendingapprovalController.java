package ksbysample.webapp.lending.web.lendingapproval;

import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/lendingapproval")
public class LendingapprovalController {

    @Autowired
    private LendingapprovalService lendingapprovalService;

    @Autowired
    private MessagesPropertiesHelper messagesPropertiesHelper;

    @RequestMapping
    public String index(@Validated LendingapprovalParamForm lendingapprovalParamForm
            , BindingResult bindingResult
            , LendingapprovalForm lendingapprovalForm
            , BindingResult bindingResultOfLendingapprovalForm) {
        if (bindingResult.hasErrors()) {
            throw new WebApplicationRuntimeException(
                    messagesPropertiesHelper.getMessage("LendingapprovalParamForm.lendingAppId.emptyerr", null));
        }

        // 画面に表示するデータを取得する
        lendingapprovalService.setDispData(lendingapprovalParamForm.getLendingAppId(), lendingapprovalForm);

        // 指定された貸出申請IDで申請中、承認済のデータがない場合には、貸出承認画面上にエラーメッセージを表示する
        if (lendingapprovalForm.getLendingApp() == null) {
            bindingResultOfLendingapprovalForm.reject("LendingapprovalForm.lendingApp.nodataerr");
        }
        
        return "lendingapproval/lendingapproval";        
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public String complete() {
        return "lendingapproval/lendingapproval";
    }

}
