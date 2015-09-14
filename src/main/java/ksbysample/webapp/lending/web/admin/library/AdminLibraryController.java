package ksbysample.webapp.lending.web.admin.library;

import ksbysample.webapp.lending.config.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/admin/library")
public class AdminLibraryController {

    @Autowired
    private AdminLibraryService adminLibraryService;

    @RequestMapping
    public String index() {
        return "admin/library/library";
    }

    @RequestMapping("/addSearchLibrary")
    public String addSearchLibrary(SetSelectedLibraryForm setSelectedLibraryForm) {
        adminLibraryService.deleteAndInsertLibraryForSearch(setSelectedLibraryForm);
        return "redirect:" + Constant.URL_ADMIN_LIBRARY;
    }
    
}
