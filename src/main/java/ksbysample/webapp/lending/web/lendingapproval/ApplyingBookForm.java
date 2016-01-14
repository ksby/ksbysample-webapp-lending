package ksbysample.webapp.lending.web.lendingapproval;

import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.values.LendingBookApprovalResultValues;
import ksbysample.webapp.lending.values.validation.ValuesEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class ApplyingBookForm {

    private Long lendingBookId;

    private String isbn;

    private String bookName;

    private String lendingAppReason;

    @ValuesEnum(enumClass = LendingBookApprovalResultValues.class, allowEmpty = true)
    private String approvalResult;

    @Size(max = 128)
    private String approvalReason;

    private Long version;

    public ApplyingBookForm(LendingBook lendingBook) {
        BeanUtils.copyProperties(lendingBook, this);
    }

}
