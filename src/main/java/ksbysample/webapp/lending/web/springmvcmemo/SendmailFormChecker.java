package ksbysample.webapp.lending.web.springmvcmemo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

/**
 * ???
 */
@Data
public class SendmailFormChecker {

    @NotBlank
    private Long id;

    private MultipartFile fileupload;

    @NotBlank
    private String name;

    private String address;

    @NotBlank
    private String mailAddress;

}
