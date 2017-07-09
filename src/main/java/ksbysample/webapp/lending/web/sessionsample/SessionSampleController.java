package ksbysample.webapp.lending.web.sessionsample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * ???
 */
@Controller
@RequestMapping("/sessionsample")
@SessionAttributes("sessionSampleForm")
public class SessionSampleController {

    /**
     * @param sessionSampleForm ???
     * @return ???
     */
    @RequestMapping
    public String index(SessionSampleForm sessionSampleForm) {
        return "sessionsample/first";
    }

    /**
     * @param sessionSampleForm ???
     * @return ???
     */
    @RequestMapping("/next")
    public String next(SessionSampleForm sessionSampleForm) {
        return "sessionsample/next";
    }

    /**
     * @param sessionSampleForm ???
     * @return ???
     */
    @RequestMapping("/confirm")
    public String confirm(SessionSampleForm sessionSampleForm) {
        return "sessionsample/confirm";
    }

    /**
     * @param sessionStatus ???
     * @return ???
     */
    @RequestMapping("/clear")
    public String clear(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/sessionsample";
    }

}
