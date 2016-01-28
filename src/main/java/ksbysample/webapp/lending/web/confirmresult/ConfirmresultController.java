package ksbysample.webapp.lending.web.confirmresult;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/confirmresult")
public class ConfirmresultController {

    @RequestMapping
    public String index() {
        return "confirmresult/confirmresult";
    }

    @RequestMapping(value = "/filedownload", method = RequestMethod.POST)
    public String filedownload() {
        return "confirmresult/confirmresult";
    }
    
}
