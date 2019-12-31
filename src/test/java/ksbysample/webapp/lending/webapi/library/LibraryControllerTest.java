package ksbysample.webapp.lending.webapi.library;

import ksbysample.common.test.extension.db.TestDataExtension;
import ksbysample.common.test.extension.mockmvc.SecurityMockMvcExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class LibraryControllerTest {

    @RegisterExtension
    @Autowired
    public TestDataExtension testDataExtension;

    @RegisterExtension
    @Autowired
    public SecurityMockMvcExtension mvc;

    @Test
    void 正しい都道府県を指定した場合には図書館一覧が返る() throws Exception {
        mvc.noauth.perform(get("/webapi/library/getLibraryList?pref=東京都"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errcode", is(0)))
                .andExpect(jsonPath("$.errmsg", is("")))
                .andExpect(jsonPath("$.content[0].address", startsWith("東京都")))
                .andExpect(jsonPath("$.content[?(@.formal=='国立国会図書館東京本館')]").exists());
    }

    @Test
    void 間違った都道府県を指定した場合にはエラーが返る() throws Exception {
        mvc.noauth.perform(get("/webapi/library/getLibraryList?pref=東a京都"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errcode", is(-2)))
                .andExpect(jsonPath("$.errmsg", is("都道府県名が正しくありません。")))
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

}
