package ksbysample.webapp.lending.service.file;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import ksbysample.common.test.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BooklistCsvFileServiceTest {

    @Autowired
    private BooklistCsvFileService booklistCsvFileService;

    @Test
    void testValidateUploadFile_NoErrorCsvFile() throws Exception {
        MockMultipartFile multipartFile = createNoErrorCsvFile();
        Errors errors = TestHelper.createErrors();
        booklistCsvFileService.validateUploadFile(multipartFile, errors);
        assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    void testValidateUploadFile_ErrorCsvFile() throws Exception {
        MockMultipartFile multipartFile = createErrorCsvFile();
        Errors errors = TestHelper.createErrors();
        booklistCsvFileService.validateUploadFile(multipartFile, errors);
        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getErrorCount()).isEqualTo(6);
        assertThat(errors.getAllErrors())
                .contains(new ObjectError(""
                        , new String[]{"UploadBooklistForm.fileupload.lengtherr"}
                        , new Object[]{2, 3}, null))
                .contains(new ObjectError(""
                        , new String[]{"UploadBooklistForm.fileupload.isbn.patternerr"}
                        , new Object[]{3, "978-4-7741-5x77-3"}, null))
                .contains(new ObjectError(""
                        , new String[]{"UploadBooklistForm.fileupload.isbn.lengtherr"}
                        , new Object[]{4, "978-4-79173-8014-9"}, null))
                .contains(new ObjectError(""
                        , new String[]{"UploadBooklistForm.fileupload.isbn.numlengtherr"}
                        , new Object[]{4, "97847917380149"}, null))
                .contains(new ObjectError(""
                        , new String[]{"UploadBooklistForm.fileupload.isbn.numlengtherr"}
                        , new Object[]{5, "97847197347784"}, null))
                .contains(new ObjectError(""
                        , new String[]{"UploadBooklistForm.fileupload.bookname.lengtherr"}
                        , new Object[]{6, "123456789012345678901234567890123456789012345678901234567890123456789"
                        + "012345678901234567890123456789012345678901234567890123456789"}
                        , null));
    }

    @Test
    public void testConvertFileToList() throws Exception {
        MockMultipartFile multipartFile = createNoErrorCsvFile();
        List<BooklistCsvRecord> booklistCsvRecordList = booklistCsvFileService.convertFileToList(multipartFile);
        assertThat(booklistCsvRecordList).hasSize(5);
        assertThat(booklistCsvRecordList).contains(new BooklistCsvRecord("978-4-7741-5377-3", "JUnit実践入門"));
    }

    public MockMultipartFile createNoErrorCsvFile() throws Exception {
        Path path = Files.createTempFile("テスト", "csv");
        try (BufferedWriter bw = Files.newBufferedWriter(path, Charset.forName("Windows-31J"))) {
            CsvWriterSettings settings = new CsvWriterSettings();
            settings.setQuoteAllFields(true);
            CsvWriter writer = new CsvWriter(bw, settings);
            writer.writeHeaders("ISBN", "書名");
            writer.writeRow("978-4-7741-6366-6", "GitHub実践入門");
            writer.writeRow("978-4-7741-5377-3", "JUnit実践入門");
            writer.writeRow("978-4-7973-8014-9", "Java最強リファレンス");
            writer.writeRow("978-4-7973-4778-4", "アジャイルソフトウェア開発の奥義");
            writer.writeRow("978-4-87311-704-1", "Javaによる関数型プログラミング");
            writer.close();
        }

        MockMultipartFile multipartFile;
        try (InputStream is = Files.newInputStream(path)) {
            multipartFile = new MockMultipartFile("テスト.csv", is);
        }

        return multipartFile;
    }

    public MockMultipartFile createErrorCsvFile() throws Exception {
        Path path = Files.createTempFile("テスト２", "csv");
        try (BufferedWriter bw = Files.newBufferedWriter(path, Charset.forName("Windows-31J"))) {
            CsvWriterSettings settings = new CsvWriterSettings();
            settings.setQuoteAllFields(true);
            CsvWriter writer = new CsvWriter(bw, settings);
            writer.writeHeaders("ISBN", "書名");
            writer.writeRow("978-4-7741-6366-6", "GitHub実践入門", "項目が３つ");
            writer.writeRow("978-4-7741-5x77-3", "JUnit実践入門");
            writer.writeRow("978-4-79173-8014-9", "Java最強リファレンス");
            writer.writeRow("978-4-719734778-4", "アジャイルソフトウェア開発の奥義");
            writer.writeRow("978-4-87311-704-1", "12345678901234567890123456789012345678901234567890123456789"
                    + "0123456789012345678901234567890123456789012345678901234567890123456789");
            writer.close();
        }

        MockMultipartFile multipartFile;
        try (InputStream is = Files.newInputStream(path)) {
            multipartFile = new MockMultipartFile("テスト２.csv", is);
        }

        return multipartFile;
    }
}
