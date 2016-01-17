package ksbysample.webapp.lending.web.lendingapproval;

import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import ksbysample.webapp.lending.helper.thymeleaf.SuccessMessagesHelper;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;

@Controller
@RequestMapping("/lendingapproval")
public class LendingapprovalController {

    @Autowired
    private LendingapprovalService lendingapprovalService;

    @Autowired
    private MessagesPropertiesHelper messagesPropertiesHelper;

    @Autowired
    private LendingapprovalFormValidator lendingapprovalFormValidator;

    @InitBinder(value = "lendingapprovalForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(lendingapprovalFormValidator);
    }

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
    public String complete(@Validated LendingapprovalForm lendingapprovalForm
            , BindingResult bindingResult
            , Model model) throws MessagingException {
        if (bindingResult.hasErrors()) {
            return "lendingapproval/lendingapproval";
        }

        try {
            // データを更新し、承認完了メールを送信する
            lendingapprovalService.complete(lendingapprovalForm);
    
            // 画面に表示するデータを取得する
            lendingapprovalService.setDispData(lendingapprovalForm.getLendingApp().getLendingAppId(), lendingapprovalForm);
    
            // 画面に表示する通常メッセージをセットする
            SuccessMessagesHelper successMessagesHelper = new SuccessMessagesHelper("確定しました");
            successMessagesHelper.setToModel(model);
        } catch (OptimisticLockException e) {
            bindingResult.reject("Global.optimisticLockException");
        }        

        return "lendingapproval/lendingapproval";
    }

}
