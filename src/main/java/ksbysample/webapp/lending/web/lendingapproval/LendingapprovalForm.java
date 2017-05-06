package ksbysample.webapp.lending.web.lendingapproval;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LendingapprovalForm {

    private LendingApp lendingApp;

    private String username;

    @Valid
    private List<ApplyingBookForm> applyingBookFormList;

    /**
     * @param lendingBookList ???
     */
    public void setApplyingBookFormListFromLendingBookList(List<LendingBook> lendingBookList) {
        this.applyingBookFormList = null;
        if (lendingBookList != null) {
            this.applyingBookFormList = lendingBookList.stream()
                    .map(ApplyingBookForm::new)
                    .collect(Collectors.toList());
        }
    }

}
