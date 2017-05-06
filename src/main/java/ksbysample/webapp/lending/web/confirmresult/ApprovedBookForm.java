package ksbysample.webapp.lending.web.confirmresult;

import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class ApprovedBookForm {

    private Long lendingBookId;

    private String isbn;

    private String bookName;

    private String lendingAppReason;

    private String approvalResult;

    private String approvalReason;

    /**
     * @param lendingBook ???
     */
    public ApprovedBookForm(LendingBook lendingBook) {
        BeanUtils.copyProperties(lendingBook, this);
    }

}
