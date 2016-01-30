package ksbysample.webapp.lending.web.confirmresult;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ConfirmresultForm {

    private LendingApp lendingApp;

    private String lendingUserName;

    private String approvalUserName;

    private List<ApprovedBookForm> approvedBookFormList;

    public void setApprovedBookFormListFromLendingBookList(List<LendingBook> lendingBookList) {
        this.approvedBookFormList = null;
        if (lendingBookList != null) {
            this.approvedBookFormList = lendingBookList.stream()
                    .map(ApprovedBookForm::new)
                    .collect(Collectors.toList());
        }
    }

}
