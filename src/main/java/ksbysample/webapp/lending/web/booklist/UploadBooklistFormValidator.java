package ksbysample.webapp.lending.web.booklist;

import ksbysample.webapp.lending.service.file.BooklistCsvFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UploadBooklistFormValidator implements Validator {

    @Autowired
    private BooklistCsvFileService booklistCsvFileService;
    
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UploadBooklistForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UploadBooklistForm uploadBooklistForm = (UploadBooklistForm) target;
        booklistCsvFileService.validateUploadFile(uploadBooklistForm.getFileupload(), errors);
    }

}
