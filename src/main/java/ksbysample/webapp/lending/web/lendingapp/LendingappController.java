package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.cookie.CookieLastLendingAppId;
import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import ksbysample.webapp.lending.helper.thymeleaf.SuccessMessagesHelper;
import ksbysample.webapp.lending.util.cookie.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.jdbc.OptimisticLockException;
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
import javax.servlet.http.HttpServletResponse;

import static ksbysample.webapp.lending.values.lendingapp.LendingAppStatusValues.UNAPPLIED;

/**
 * ???
 */
@Controller
@RequestMapping("/lendingapp")
public class LendingappController {

    private static final String THYMELEAF_TEMPLATE = "lendingapp/lendingapp";

    private final LendingappService lendingappService;

    private final MessagesPropertiesHelper mph;

    private final LendingappFormValidator lendingappFormValidator;

    /**
     * @param lendingappService       ???
     * @param mph                     ???
     * @param lendingappFormValidator ???
     */
    public LendingappController(LendingappService lendingappService
            , MessagesPropertiesHelper mph
            , LendingappFormValidator lendingappFormValidator) {
        this.lendingappService = lendingappService;
        this.mph = mph;
        this.lendingappFormValidator = lendingappFormValidator;
    }

    /**
     * @param binder ???
     */
    @InitBinder(value = "lendingappForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(lendingappFormValidator);
    }

    /**
     * @param lendingappParamForm ???
     * @param bindingResult       ???
     * @param lendingappForm      ???
     * @param response            ???
     * @return ???
     */
    @GetMapping
    public String index(@Validated LendingappParamForm lendingappParamForm
            , BindingResult bindingResult
            , LendingappForm lendingappForm
            , HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new WebApplicationRuntimeException(
                    mph.getMessage("LendingappForm.lendingAppId.emptyerr", null));
        }

        // 画面に表示するデータを取得する
        lendingappService.setDispData(lendingappParamForm.getLendingAppId(), lendingappForm);

        // 未申請の場合には LastLendingAppId Cookie に貸出申請ID をセットする
        if (StringUtils.equals(lendingappForm.getLendingApp().getStatus(), UNAPPLIED.getValue())) {
            CookieUtils.addCookie(CookieLastLendingAppId.class
                    , response, String.valueOf(lendingappParamForm.getLendingAppId()));
        }

        return THYMELEAF_TEMPLATE;
    }

    /**
     * @param lendingappForm ???
     * @param bindingResult  ???
     * @param response       ???
     * @return ???
     * @throws MessagingException
     */
    @PostMapping("/apply")
    public String apply(@Validated LendingappForm lendingappForm
            , BindingResult bindingResult
            , HttpServletResponse response) throws MessagingException {
        if (bindingResult.hasErrors()) {
            return THYMELEAF_TEMPLATE;
        }

        try {
            // 入力された内容で申請する
            lendingappService.apply(lendingappForm);

            // 画面に表示するデータを取得する
            lendingappService.setDispData(lendingappForm.getLendingApp().getLendingAppId(), lendingappForm);

            // LastLendingAppId Cookie を削除する
            CookieUtils.removeCookie(CookieLastLendingAppId.class, response);
        } catch (OptimisticLockException e) {
            bindingResult.reject("Global.optimisticLockException");
        }

        return THYMELEAF_TEMPLATE;
    }

    /**
     * @param lendingappForm ???
     * @param bindingResult  ???
     * @param model          ???
     * @return ???
     */
    @PostMapping("/temporarySave")
    public String temporarySave(@Validated LendingappForm lendingappForm
            , BindingResult bindingResult
            , Model model) {
        if (bindingResult.hasErrors()) {
            return THYMELEAF_TEMPLATE;
        }

        try {
            // 入力された内容を一時保存する
            lendingappService.temporarySave(lendingappForm);

            // 画面に表示する通常メッセージをセットする
            SuccessMessagesHelper successMessagesHelper = new SuccessMessagesHelper("一時保存しました");
            successMessagesHelper.setToModel(model);
        } catch (OptimisticLockException e) {
            bindingResult.reject("Global.optimisticLockException");
        }

        return THYMELEAF_TEMPLATE;
    }

}
