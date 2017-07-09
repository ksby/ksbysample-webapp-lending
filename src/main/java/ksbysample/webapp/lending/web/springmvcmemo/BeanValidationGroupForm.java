package ksbysample.webapp.lending.web.springmvcmemo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * ???
 */
@Data
public class BeanValidationGroupForm {

    private Long id;

    private MultipartFile fileupload;

    private String name;

    private String address;

    private String mailAddress;

}
