package ksbysample.webapp.lending.web.springmvcmemo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BeanValidationGroupForm {

    public static interface FileUploadGroup {}
    public static interface EditGroup {}
    public static interface SendmailGroup {}

    @NotBlank(groups = {FileUploadGroup.class, EditGroup.class, SendmailGroup.class})
    private Long id;

    private MultipartFile fileupload;

    @NotBlank(groups = {EditGroup.class, SendmailGroup.class})
    private String name;

    @NotBlank(groups = {EditGroup.class})
    private String address;

    @NotBlank(groups = {SendmailGroup.class})
    private String mailAddress;

}
