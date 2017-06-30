package ksbysample.webapp.lending.web.booklist;

import ksbysample.webapp.lending.entity.LendingBook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * ???
 */
@Controller
@RequestMapping("/booklist")
public class BooklistController {

    private final UploadBooklistFormValidator uploadBooklistFormValidator;

    private final BooklistService booklistService;

    /**
     * @param uploadBooklistFormValidator ???
     * @param booklistService             ???
     */
    public BooklistController(UploadBooklistFormValidator uploadBooklistFormValidator
            , BooklistService booklistService) {
        this.uploadBooklistFormValidator = uploadBooklistFormValidator;
        this.booklistService = booklistService;
    }

    /**
     * @param binder ???
     */
    @InitBinder("uploadBooklistForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(uploadBooklistFormValidator);
    }

    /**
     * @param uploadBooklistForm ???
     * @return ???
     */
    @RequestMapping
    public String index(UploadBooklistForm uploadBooklistForm) {
        return "booklist/booklist";
    }

    /**
     * @param uploadBooklistForm ???
     * @param bindingResult      ???
     * @param model              ???
     * @return ???
     */
    @RequestMapping("/fileupload")
    public String fileupload(@Validated UploadBooklistForm uploadBooklistForm
            , BindingResult bindingResult
            , Model model) {
        if (bindingResult.hasErrors()) {
            return "booklist/booklist";
        }

        // アップロードされたCSVファイルのデータをDBに保存する
        Long lendingAppId = booklistService.temporarySaveBookListCsvFile(uploadBooklistForm);

        // 確認画面に表示するデータを取得する
        List<LendingBook> lendingBookList = booklistService.getLendingBookList(lendingAppId);
        RegisterBooklistForm registerBooklistForm = new RegisterBooklistForm(lendingBookList, lendingAppId);
        model.addAttribute("registerBooklistForm", registerBooklistForm);

        return "booklist/fileupload";
    }

    /**
     * @param registerBooklistForm ???
     * @param redirectAttributes   ???
     * @return ???
     */
    @RequestMapping("/register")
    public String register(RegisterBooklistForm registerBooklistForm
            , RedirectAttributes redirectAttributes) {
        booklistService.sendMessageToInquiringStatusOfBookQueue(registerBooklistForm);
        redirectAttributes.addFlashAttribute("lendingAppId", registerBooklistForm.getLendingAppId());
        return "redirect:/booklist/complete";
    }

    /**
     * @return ???
     */
    @RequestMapping("/complete")
    public String complete() {
        return "booklist/complete";
    }

}
