package ksbysample.webapp.lending.helper.thymeleaf;

import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * ???
 */
public class SuccessMessagesHelper {

    private final List<String> successMessages = new ArrayList<>();

    /**
     *
     */
    public SuccessMessagesHelper() {
    }

    /**
     * @param msg ???
     */
    public SuccessMessagesHelper(String msg) {
        this.successMessages.add(msg);
    }

    /**
     * @param msg ???
     */
    public void addMsg(String msg) {
        this.successMessages.add(msg);
    }

    /**
     * @param model ???
     */
    public void setToModel(Model model) {
        model.addAttribute("successMessages", this.successMessages);
    }

}
