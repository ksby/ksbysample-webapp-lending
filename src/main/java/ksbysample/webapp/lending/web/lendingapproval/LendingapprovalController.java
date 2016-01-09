package ksbysample.webapp.lending.web.lendingapproval;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/lendingapproval")
public class LendingapprovalController {

    @RequestMapping
    public String index() {
        return "lendingapproval/lendingapproval";        
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public String complete() {
        return "lendingapproval/lendingapproval";
    }                        

}
