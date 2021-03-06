package ksbysample.webapp.lending.web.confirmresult;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ???
 */
@Data
@NoArgsConstructor
public class ConfirmresultForm {

    private LendingApp lendingApp;

    private Long lendingUserId;

    private String lendingUserName;

    private String approvalUserName;

    private List<ApprovedBookForm> approvedBookFormList;

    /**
     * ???
     *
     * @param lendingBookList ???
     */
    public void setApprovedBookFormListFromLendingBookList(List<LendingBook> lendingBookList) {
        this.approvedBookFormList = lendingBookList == null
                ? null
                : lendingBookList.stream()
                .map(ApprovedBookForm::new)
                .collect(Collectors.toList());
    }

}
