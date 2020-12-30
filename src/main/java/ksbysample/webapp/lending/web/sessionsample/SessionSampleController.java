package ksbysample.webapp.lending.web.sessionsample;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

/**
 * ???
 */
@Controller
@RequestMapping("/sessionsample")
@SessionAttributes("sessionSampleForm")
public class SessionSampleController {

    /**
     * ???
     *
     * @param sessionSampleForm ???
     * @return ???
     */
    @SuppressFBWarnings("SPRING_CSRF_UNRESTRICTED_REQUEST_MAPPING")
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String index(SessionSampleForm sessionSampleForm) {
        return "sessionsample/first";
    }

    /**
     * ???
     *
     * @param sessionSampleForm ???
     * @return ???
     */
    @PostMapping("/next")
    public String next(SessionSampleForm sessionSampleForm) {
        return "sessionsample/next";
    }

    /**
     * ???
     *
     * @param sessionSampleForm ???
     * @return ???
     */
    @PostMapping("/confirm")
    public String confirm(SessionSampleForm sessionSampleForm) {
        return "sessionsample/confirm";
    }

    /**
     * ???
     *
     * @param sessionStatus ???
     * @return ???
     */
    @PostMapping("/clear")
    public String clear(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/sessionsample";
    }

}
