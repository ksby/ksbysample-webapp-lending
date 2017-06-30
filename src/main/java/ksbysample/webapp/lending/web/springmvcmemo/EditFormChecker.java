package ksbysample.webapp.lending.web.springmvcmemo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

/**
 * ???
 */
@Data
public class EditFormChecker {

    @NotBlank
    private Long id;

    private MultipartFile fileupload;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    private String mailAddress;

}
