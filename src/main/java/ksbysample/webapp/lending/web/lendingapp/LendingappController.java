package ksbysample.webapp.lending.web.lendingapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/lendingapp")
public class LendingappController {

    @RequestMapping
    public String index() {
        return "lendingapp/lendingapp";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public String apply() {
        return "lendingapp/lendingapp";
    }

    @RequestMapping(value = "/temporarySave", method = RequestMethod.POST)
    public String temporarySave() {
        return "lendingapp/lendingapp";
    }

}
