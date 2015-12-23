package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.cookie.CookieLastLendingAppId;
import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import ksbysample.webapp.lending.helper.thymeleaf.SuccessMessagesHelper;
import ksbysample.webapp.lending.util.cookie.CookieUtils;
import org.apache.commons.lang.StringUtils;
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
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ksbysample.webapp.lending.values.LendingAppStatusValues.UNAPPLIED;

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
            , LendingappForm lendingappForm
            , HttpServletResponse response) {
        if (bindingResultForLendingappParamForm.hasErrors()) {
            throw new WebApplicationRuntimeException(
                    messagesPropertiesHelper.getMessage("LendingappForm.lendingAppId.emptyerr", null));
        }

        // 画面に表示するデータを取得する
        setDispData(lendingappParamForm.getLendingAppId(), lendingappForm);

        // 未申請の場合には LastLendingAppId Cookie に貸出申請ID をセットする
        if (StringUtils.equals(lendingappForm.getLendingApp().getStatus(), UNAPPLIED.getValue())) {
            CookieUtils.addCookie(CookieLastLendingAppId.class
                    , response, String.valueOf(lendingappParamForm.getLendingAppId()));
        }

        return "lendingapp/lendingapp";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public String apply(@Validated LendingappForm lendingappForm
            , BindingResult bindingResult
            , HttpServletResponse response) throws MessagingException {
        if (bindingResult.hasErrors()) {
            return "lendingapp/lendingapp";
        }

        try {
            // 入力された内容で申請する
            lendingappService.apply(lendingappForm);

            // 画面に表示するデータを取得する
            setDispData(lendingappForm.getLendingApp().getLendingAppId(), lendingappForm);

            // LastLendingAppId Cookie を削除する
            CookieUtils.removeCookie(CookieLastLendingAppId.class, response);
        } catch (OptimisticLockException e) {
            bindingResult.reject("Global.optimisticLockException");
        }

        return "lendingapp/lendingapp";
    }

    @RequestMapping(value = "/temporarySave", method = RequestMethod.POST)
    public String temporarySave(@Validated LendingappForm lendingappForm
            , BindingResult bindingResult
            , Model model) {
        if (bindingResult.hasErrors()) {
            return "lendingapp/lendingapp";
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
        
        return "lendingapp/lendingapp";
    }

    private void setDispData(Long lendingAppId, LendingappForm lendingappForm) {
        LendingApp lendingApp = lendingappService.getLendingApp(lendingAppId);
        List<LendingBook> lendingBookList = lendingappService.getLendingBookList(lendingAppId);
        lendingappForm.setLendingApp(lendingApp);
        lendingappForm.setLendingBookList(lendingBookList);
    }

}
