package ksbysample.webapp.lending.web.booklist;

import ksbysample.common.test.helper.TestHelper;
import ksbysample.webapp.lending.service.file.BooklistCsvFileServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UploadBooklistFormValidatorTest {

    @Autowired
    private UploadBooklistFormValidator uploadBooklistFormValidator;

    private BooklistCsvFileServiceTest booklistCsvFileServiceTest = new BooklistCsvFileServiceTest();

    @Test
    void testValidate_NoErrorCsvFile() throws Exception {
        UploadBooklistForm uploadBooklistForm = new UploadBooklistForm();
        MultipartFile multipartFile = booklistCsvFileServiceTest.createNoErrorCsvFile();
        uploadBooklistForm.setFileupload(multipartFile);
        Errors errors = TestHelper.createErrors();
        uploadBooklistFormValidator.validate(uploadBooklistForm, errors);
        assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    void testValidate_ErrorCsvFile() throws Exception {
        UploadBooklistForm uploadBooklistForm = new UploadBooklistForm();
        MultipartFile multipartFile = booklistCsvFileServiceTest.createErrorCsvFile();
        uploadBooklistForm.setFileupload(multipartFile);
        Errors errors = TestHelper.createErrors();
        uploadBooklistFormValidator.validate(uploadBooklistForm, errors);
        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getErrorCount()).isEqualTo(6);
        assertThat(errors.getAllErrors())
                .contains(new ObjectError("", new String[]{"UploadBooklistForm.fileupload.lengtherr"}, new Object[]{2, 3}, null))
                .contains(new ObjectError("", new String[]{"UploadBooklistForm.fileupload.isbn.patternerr"}, new Object[]{3, "978-4-7741-5x77-3"}, null))
                .contains(new ObjectError("", new String[]{"UploadBooklistForm.fileupload.isbn.lengtherr"}, new Object[]{4, "978-4-79173-8014-9"}, null))
                .contains(new ObjectError("", new String[]{"UploadBooklistForm.fileupload.isbn.numlengtherr"}, new Object[]{4, "97847917380149"}, null))
                .contains(new ObjectError("", new String[]{"UploadBooklistForm.fileupload.isbn.numlengtherr"}, new Object[]{5, "97847197347784"}, null))
                .contains(new ObjectError("", new String[]{"UploadBooklistForm.fileupload.bookname.lengtherr"}, new Object[]{6, "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"}, null));
    }

}
