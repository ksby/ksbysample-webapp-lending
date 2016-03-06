package ksbysample.webapp.lending.web.textareamemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/textareamemo")
public class TextareaMemoController {

    @RequestMapping
    public String index(TextareaMemoForm textareaMemoForm) {
        return "textareamemo/index";
    }

    @RequestMapping("/display")
    public String display(TextareaMemoForm textareaMemoForm) {
        return "textareamemo/display";
    }

}
