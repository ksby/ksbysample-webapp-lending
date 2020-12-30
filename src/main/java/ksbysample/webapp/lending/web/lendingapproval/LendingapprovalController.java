package ksbysample.webapp.lending.web.lendingapproval;

import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import ksbysample.webapp.lending.helper.thymeleaf.SuccessMessagesHelper;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;

/**
 * ???
 */
@Controller
@PreAuthorize("hasRole('ROLE_APPROVER')")
@RequestMapping("/lendingapproval")
public class LendingapprovalController {

    private final LendingapprovalService lendingapprovalService;

    private final MessagesPropertiesHelper mph;

    private final LendingapprovalFormValidator lendingapprovalFormValidator;

    /**
     * ???
     *
     * @param lendingapprovalService       ???
     * @param mph                          ???
     * @param lendingapprovalFormValidator ???
     */
    public LendingapprovalController(LendingapprovalService lendingapprovalService
            , MessagesPropertiesHelper mph
            , LendingapprovalFormValidator lendingapprovalFormValidator) {
        this.lendingapprovalService = lendingapprovalService;
        this.mph = mph;
        this.lendingapprovalFormValidator = lendingapprovalFormValidator;
    }

    /**
     * ???
     *
     * @param binder ???
     */
    @InitBinder(value = "lendingapprovalForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(lendingapprovalFormValidator);
    }

    /**
     * ???
     *
     * @param lendingapprovalParamForm           ???
     * @param bindingResult                      ???
     * @param lendingapprovalForm                ???
     * @param bindingResultOfLendingapprovalForm ???
     * @return ???
     */
    @GetMapping
    public String index(@Validated LendingapprovalParamForm lendingapprovalParamForm
            , BindingResult bindingResult
            , LendingapprovalForm lendingapprovalForm
            , BindingResult bindingResultOfLendingapprovalForm) {
        if (bindingResult.hasErrors()) {
            throw new WebApplicationRuntimeException(
                    mph.getMessage("LendingapprovalParamForm.lendingAppId.emptyerr", null));
        }

        // 画面に表示するデータを取得する
        lendingapprovalService.setDispData(lendingapprovalParamForm.getLendingAppId(), lendingapprovalForm);

        // 指定された貸出申請IDで申請中、承認済のデータがない場合には、貸出承認画面上にエラーメッセージを表示する
        if (lendingapprovalForm.getLendingApp() == null) {
            bindingResultOfLendingapprovalForm.reject("LendingapprovalForm.lendingApp.nodataerr");
        }

        return "lendingapproval/lendingapproval";
    }

    /**
     * ???
     *
     * @param lendingapprovalForm ???
     * @param bindingResult       ???
     * @param model               ???
     * @return ???
     * @throws MessagingException ???
     */
    @PostMapping("/complete")
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
            lendingapprovalService.setDispData(lendingapprovalForm.getLendingApp().getLendingAppId()
                    , lendingapprovalForm);

            // 画面に表示する通常メッセージをセットする
            SuccessMessagesHelper successMessagesHelper = new SuccessMessagesHelper("確定しました");
            successMessagesHelper.setToModel(model);
        } catch (OptimisticLockException e) {
            bindingResult.reject("Global.optimisticLockException");
        }

        return "lendingapproval/lendingapproval";
    }

}
