package ksbysample.webapp.lending.helper.thymeleaf;

import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

public class SuccessMessagesHelper {

    private List<String> successMessages = new ArrayList<>();

    public SuccessMessagesHelper() {}
    
    public SuccessMessagesHelper(String msg) {
        this.successMessages.add(msg);
    }

    public void addMsg(String msg) {
        this.successMessages.add(msg);
    }

    public void setToModel(Model model) {
        model.addAttribute("successMessages", this.successMessages);
    }
    
}
