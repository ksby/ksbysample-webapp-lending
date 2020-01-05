package ksbysample.webapp.lending.web.textareamemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ???
 */
@Controller
@RequestMapping("/textareamemo")
public class TextareaMemoController {

    /**
     * @param textareaMemoForm ???
     * @return ???
     */
    @GetMapping
    public String index(TextareaMemoForm textareaMemoForm) {
        return "textareamemo/index";
    }

    /**
     * @param textareaMemoForm ???
     * @return ???
     */
    @PostMapping("/display")
    public String display(TextareaMemoForm textareaMemoForm) {
        return "textareamemo/display";
    }

}
