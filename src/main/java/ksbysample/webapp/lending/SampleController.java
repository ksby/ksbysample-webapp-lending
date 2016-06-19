package ksbysample.webapp.lending;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sample")
public class SampleController {

    @Value("${spring.thymeleaf.cache:}")
    private String springThymeleafCache;

    @RequestMapping
    @ResponseBody
    public String index() {
        return "spring.thymeleaf.cache = " + springThymeleafCache;
    }

}
