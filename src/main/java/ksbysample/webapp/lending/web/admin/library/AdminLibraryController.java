package ksbysample.webapp.lending.web.admin.library;

import ksbysample.webapp.lending.config.Constant;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin/library")
public class AdminLibraryController {

    private final AdminLibraryService adminLibraryService;

    /**
     * @param adminLibraryService ???
     */
    public AdminLibraryController(AdminLibraryService adminLibraryService) {
        this.adminLibraryService = adminLibraryService;
    }

    /**
     * @return ???
     */
    @RequestMapping
    public String index() {
        return "admin/library/library";
    }

    /**
     * @param setSelectedLibraryForm ???
     * @return ???
     */
    @RequestMapping("/addSearchLibrary")
    public String addSearchLibrary(SetSelectedLibraryForm setSelectedLibraryForm) {
        adminLibraryService.deleteAndInsertLibraryForSearch(setSelectedLibraryForm);
        return "redirect:" + Constant.URL_ADMIN_LIBRARY;
    }

}
