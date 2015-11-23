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

    String isbn;

    String bookName;

    String lendingState;

    @Pattern(regexp = "^(|1)$")
    String lendingAppFlg;

    @Size(max = 128)
    String lendingAppReason;

    LendingBookDto(LendingBook lendingBook) {
        BeanUtils.copyProperties(lendingBook, this);
    }

}
