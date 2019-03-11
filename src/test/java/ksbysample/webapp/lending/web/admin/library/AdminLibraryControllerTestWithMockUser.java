package ksbysample.webapp.lending.web.admin.library;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminLibraryControllerTestWithMockUser {

    @Autowired
    private MockMvc mvc;

    // AdminLibraryController 内で DI している AdminLibraryService は mock にする
    @MockBean
    private AdminLibraryService adminLibraryService;

    @WithAnonymousUser
    @Test
    void 認証していなければログイン画面にリダイレクトされる() throws Exception {
        mvc.perform(get("/admin/library"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/"));
    }

    @WithMockUser(roles = "USER")
    @Test
    void ROLE_ADMINがなければ403が返る() throws Exception {
        mvc.perform(get("/admin/library"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "ADMIN")
//    @WithUserDetails("tanaka.taro@sample.com")
    @Test
    void ROLE_ADMINがあれば200が返る() throws Exception {
        mvc.perform(get("/admin/library"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().hasNoErrors());
    }

}
