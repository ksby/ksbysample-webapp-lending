package ksbysample.webapp.lending.web.booklist;

import ksbysample.webapp.lending.service.file.BooklistCsvFileService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * ???
 */
@Component
public class UploadBooklistFormValidator implements Validator {

    private final BooklistCsvFileService booklistCsvFileService;

    /**
     * ???
     *
     * @param booklistCsvFileService ???
     */
    public UploadBooklistFormValidator(BooklistCsvFileService booklistCsvFileService) {
        this.booklistCsvFileService = booklistCsvFileService;
    }

    /**
     * ???
     *
     * @param clazz ???
     * @return ???
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UploadBooklistForm.class);
    }

    /**
     * ???
     *
     * @param target ???
     * @param errors ???
     */
    @Override
    public void validate(Object target, Errors errors) {
        Assert.notNull(target, "target must not be null");
        UploadBooklistForm uploadBooklistForm = (UploadBooklistForm) target;
        booklistCsvFileService.validateUploadFile(uploadBooklistForm.getFileupload(), errors);
    }

}
