package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class LendingBookDto {

    private Long lendingBookId;

    private String isbn;

    private String bookName;

    private String lendingState;

    @Pattern(regexp = "^(|1)$")
    private String lendingAppFlg;

    @Size(max = 128)
    private String lendingAppReason;

    private Long version;
    
    LendingBookDto(LendingBook lendingBook) {
        BeanUtils.copyProperties(lendingBook, this);
    }

}
