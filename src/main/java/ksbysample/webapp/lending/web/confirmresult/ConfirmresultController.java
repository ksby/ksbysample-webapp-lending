package ksbysample.webapp.lending.web.confirmresult;

import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import ksbysample.webapp.lending.web.lendingapproval.LendingapprovalForm;
import ksbysample.webapp.lending.web.lendingapproval.LendingapprovalParamForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/confirmresult")
public class ConfirmresultController {

    @Autowired
    private MessagesPropertiesHelper messagesPropertiesHelper;

    @Autowired
    private ConfirmresultService confirmresultService;
    
    @RequestMapping
    public String index(@Validated ConfirmresultParamForm confirmresultParamForm
            , BindingResult bindingResult
            , ConfirmresultForm confirmresultForm
            , BindingResult bindingResultOfConfirmresultForm) {
        if (bindingResult.hasErrors()) {
            throw new WebApplicationRuntimeException(
                    messagesPropertiesHelper.getMessage("ConfirmresultParamForm.lendingAppId.emptyerr", null));
        }

        // 画面に表示するデータを取得する
        confirmresultService.setDispData(confirmresultParamForm.getLendingAppId(), confirmresultForm);

        // 指定された貸出申請IDで承認済のデータがない場合には、貸出申請結果確認画面上にエラーメッセージを表示する
        if (confirmresultForm.getLendingApp() == null) {
            bindingResultOfConfirmresultForm.reject("ConfirmresultForm.lendingApp.nodataerr");
        }

        return "confirmresult/confirmresult";
    }

    @RequestMapping(value = "/filedownload", method = RequestMethod.POST)
    public String filedownload() {
        return "confirmresult/confirmresult";
    }
    
}
