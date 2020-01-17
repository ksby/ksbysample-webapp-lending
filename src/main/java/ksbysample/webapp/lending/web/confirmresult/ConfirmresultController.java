package ksbysample.webapp.lending.web.confirmresult;

import ksbysample.webapp.lending.aspect.logging.LoggingControllerName;
import ksbysample.webapp.lending.aspect.logging.LoggingEventName;
import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.download.DataDownloadHelper;
import ksbysample.webapp.lending.helper.download.booklistcsv.BookListCsvData;
import ksbysample.webapp.lending.helper.download.booklistcsv.BookListCsvDownloadHelper;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import ksbysample.webapp.lending.security.LendingUserDetailsHelper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * ???
 */
@Controller
@RequestMapping("/confirmresult")
@LoggingControllerName("貸出申請結果確認画面")
public class ConfirmresultController {

    private final MessagesPropertiesHelper mph;

    private final ConfirmresultService confirmresultService;

    private final LendingUserDetailsHelper lendingUserDetailsHelper;

    /**
     * @param mph                      ???
     * @param confirmresultService     ???
     * @param lendingUserDetailsHelper ???
     */
    public ConfirmresultController(MessagesPropertiesHelper mph
            , ConfirmresultService confirmresultService
            , LendingUserDetailsHelper lendingUserDetailsHelper) {
        this.mph = mph;
        this.confirmresultService = confirmresultService;
        this.lendingUserDetailsHelper = lendingUserDetailsHelper;
    }

    /**
     * @param confirmresultParamForm           ???
     * @param bindingResult                    ???
     * @param confirmresultForm                ???
     * @param bindingResultOfConfirmresultForm ???
     * @param response                         ???
     * @return ???
     */
    @GetMapping
    @LoggingEventName("初期表示処理")
    public String index(@Validated ConfirmresultParamForm confirmresultParamForm
            , BindingResult bindingResult
            , ConfirmresultForm confirmresultForm
            , BindingResult bindingResultOfConfirmresultForm
            , HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new WebApplicationRuntimeException(
                    mph.getMessage("ConfirmresultParamForm.lendingAppId.emptyerr", null));
        }

        // 画面に表示するデータを取得する
        confirmresultService.setDispData(confirmresultParamForm.getLendingAppId(), confirmresultForm);

        // 指定された貸出申請IDで承認済のデータがない場合には、貸出申請結果確認画面上にエラーメッセージを表示する
        if (confirmresultForm.getLendingApp() == null) {
            bindingResultOfConfirmresultForm.reject("ConfirmresultForm.lendingApp.nodataerr");
        } else {
            // 指定された貸出申請IDの申請者とログインしているユーザが一致しない場合にはエラーメッセージを表示し、
            // HTTP ステータスコードも 403 を返す
            if (!Objects.equals(confirmresultForm.getLendingUserId(), lendingUserDetailsHelper.getLoginUserId())) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                throw new WebApplicationRuntimeException(
                        mph.getMessage("Confirmresult.lendingUserId.notequalerr", null));
            }
        }

        return "confirmresult/confirmresult";
    }

    /**
     * @param confirmresultForm ???
     * @param bindingResult     ???
     * @param response          ???
     * @throws IOException
     */
    @PostMapping("/filedownloadByResponse")
    public void filedownloadByResponse(ConfirmresultForm confirmresultForm
            , BindingResult bindingResult
            , HttpServletResponse response) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new WebApplicationRuntimeException(
                    mph.getMessage("ConfirmresultParamForm.lendingAppId.emptyerr", null));
        }

        // データを取得する
        List<BookListCsvData> bookListCsvDataList
                = confirmresultService.getDownloadData(confirmresultForm.getLendingApp().getLendingAppId());

        // response に CSVデータを出力する
        DataDownloadHelper dataDownloadHelper
                = new BookListCsvDownloadHelper(confirmresultForm.getLendingApp().getLendingAppId()
                , bookListCsvDataList);
        dataDownloadHelper.setFileNameToResponse(response);
        dataDownloadHelper.writeDataToResponse(response);
    }

    /**
     * @param confirmresultForm ???
     * @param bindingResult     ???
     * @return ???
     */
    @PostMapping("/filedownloadByView")
    public ModelAndView filedownloadByView(ConfirmresultForm confirmresultForm
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new WebApplicationRuntimeException(
                    mph.getMessage("ConfirmresultParamForm.lendingAppId.emptyerr", null));
        }

        // データを取得する
        List<BookListCsvData> bookListCsvDataList
                = confirmresultService.getDownloadData(confirmresultForm.getLendingApp().getLendingAppId());

        ModelAndView modelAndView = new ModelAndView("BookListCsvView");
        modelAndView.addObject("lendingAppId", confirmresultForm.getLendingApp().getLendingAppId());
        modelAndView.addObject("bookListCsvDataList", bookListCsvDataList);

        return modelAndView;
    }

}
