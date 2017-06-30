package ksbysample.webapp.lending.web.booklist;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * ???
 */
@Data
public class UploadBooklistForm {

    private MultipartFile fileupload;

}
