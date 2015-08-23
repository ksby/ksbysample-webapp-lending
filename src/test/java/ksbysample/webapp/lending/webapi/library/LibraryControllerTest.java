package ksbysample.webapp.lending.webapi.library;

import ksbysample.common.test.SecurityMockMvcResource;
import ksbysample.webapp.lending.Application;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class LibraryControllerTest {

    @Rule
    @Autowired
    public SecurityMockMvcResource mvc;

    @Test
    public void 正しい都道府県を指定した場合には図書館一覧が返る() throws Exception {
        mvc.noauth.perform(get("/webapi/library/getLibraryList?pref=東京都"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errcode", is(0)))
                .andExpect(jsonPath("$.errmsg", is("")))
                .andExpect(jsonPath("$.content[0].address", startsWith("東京都")))
                .andExpect(jsonPath("$.content[?(@.formalName=='国立国会図書館東京本館')]").exists());
    }

    @Test
    public void 間違った都道府県を指定した場合にはエラーが返る() throws Exception {
        mvc.noauth.perform(get("/webapi/library/getLibraryList?pref=東a京都"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errcode", is(-2)))
                .andExpect(jsonPath("$.errmsg", is("都道府県名が正しくありません。")))
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

}
