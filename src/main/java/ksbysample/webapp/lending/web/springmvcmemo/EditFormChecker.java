package ksbysample.webapp.lending.web.springmvcmemo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

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
