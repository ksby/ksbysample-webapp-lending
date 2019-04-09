package ksbysample.webapp.lending.web.booklist;

import ksbysample.common.test.extension.db.TestDataExtension;
import ksbysample.common.test.extension.mockmvc.SecurityMockMvcExtension;
import ksbysample.webapp.lending.service.file.BooklistCsvFileServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import static ksbysample.common.test.matcher.ErrorsResultMatchers.errors;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BooklistControllerTest {

    @Nested
    @SpringBootTest
    class ユーザ権限とURLの呼び出し可否のテスト {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Test
        void ログインしていなければ貸出希望書籍CSVファイルアップロード画面は表示できない() throws Exception {
            mvc.noauth.perform(get("/booklist"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"));
        }

        @Test
        void 一般ユーザ権限を持つユーザは貸出希望書籍CSVファイルアップロード画面を表示できる() throws Exception {
            mvc.authSuzukiHanako.perform(get("/booklist"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("booklist/booklist"));
        }

        @Test
        void ログインしていなければアップロード用のURLは呼び出せない() throws Exception {
            mvc.noauth.perform(post("/booklist/fileupload"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void ログインしていなければ確認画面の登録ボタン用のURLは呼び出せない() throws Exception {
            mvc.noauth.perform(post("/booklist/register"))
                    .andExpect(status().isForbidden());
        }

    }

    @Nested
    @SpringBootTest
    class ファイルアップロードのテスト_エラーになる場合 {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Test
        void エラーが出るCSVファイルをアップロードするとアップロード画面にエラーメッセージを表示する() throws Exception {
            BooklistCsvFileServiceTest booklistCsvFileServiceTest = new BooklistCsvFileServiceTest();
            MockMultipartFile multipartFile = booklistCsvFileServiceTest.createErrorCsvFile();
            mvc.authSuzukiHanako.perform(
                    multipart("/booklist/fileupload")
                            // 単に .file(multipartFile) ではダメで、
                            // 第１引数に <input type="file" name="..."> の name 属性の文字列を、第２引数にファイルのバイト配列を渡す
                            .file("fileupload", multipartFile.getBytes())
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("booklist/booklist"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(6))
                    .andExpect(errors().hasGlobalError("uploadBooklistForm"
                            , "UploadBooklistForm.fileupload.lengtherr"))
                    .andExpect(errors().hasGlobalError("uploadBooklistForm"
                            , "UploadBooklistForm.fileupload.isbn.patternerr"))
                    .andExpect(errors().hasGlobalError("uploadBooklistForm"
                            , "UploadBooklistForm.fileupload.isbn.lengtherr"))
                    .andExpect(errors().hasGlobalError("uploadBooklistForm"
                            , "UploadBooklistForm.fileupload.isbn.numlengtherr"))
                    .andExpect(errors().hasGlobalError("uploadBooklistForm"
                            , "UploadBooklistForm.fileupload.isbn.numlengtherr"))
                    .andExpect(errors().hasGlobalError("uploadBooklistForm"
                            , "UploadBooklistForm.fileupload.bookname.lengtherr"));
        }

    }

    @Nested
    @SpringBootTest
    class ファイルアップロードのテスト_正常な場合 {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Test
        void エラーのないCSVファイルをアップロードしてから確認画面で登録ボタンをクリックすると完了画面が表示される() throws Exception {
            BooklistCsvFileServiceTest booklistCsvFileServiceTest = new BooklistCsvFileServiceTest();
            MockMultipartFile multipartFile = booklistCsvFileServiceTest.createNoErrorCsvFile();

            // CSVファイルをアップロードする
            MvcResult result = mvc.authSuzukiHanako.perform(
                    multipart("/booklist/fileupload")
                            // 単に .file(multipartFile) ではダメで、
                            // 第１引数に <input type="file" name="..."> の name 属性の文字列を、第２引数にファイルのバイト配列を渡す
                            .file("fileupload", multipartFile.getBytes())
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("booklist/fileupload"))
                    .andExpect(model().hasNoErrors())
                    .andReturn();
            RegisterBooklistForm registerBooklistForm
                    = (RegisterBooklistForm) result.getModelAndView().getModel().get("registerBooklistForm");

            // 確認画面で登録ボタンをクリックする
            mvc.authSuzukiHanako.perform(
                    post("/booklist/register")
                            .param("lendingAppId", registerBooklistForm.getLendingAppId().toString())
                            .with(csrf()))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/booklist/complete"));

            // 完了画面を表示する
            mvc.authSuzukiHanako.perform(get("/booklist/complete"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("booklist/complete"));
        }

    }

}
