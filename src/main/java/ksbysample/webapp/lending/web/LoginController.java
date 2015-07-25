package ksbysample.webapp.lending.web;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class LoginController {
    
    @RequestMapping
    public String index() {
        return "login";
    }

    @RequestMapping("/encode")
    @ResponseBody
    public String encode(@RequestParam String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @RequestMapping("/loginsuccess")
    public String loginsuccess() {
        return "loginsuccess";
    }
}
