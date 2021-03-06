package ksbysample.webapp.lending.web.springmvcmemo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * ???
 */
@Data
public class SendmailFormChecker {

    @NotNull
    private Long id;

    private MultipartFile fileupload;

    @NotBlank
    private String name;

    private String address;

    @NotBlank
    private String mailAddress;

}
