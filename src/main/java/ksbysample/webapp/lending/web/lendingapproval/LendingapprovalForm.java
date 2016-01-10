package ksbysample.webapp.lending.web.lendingapproval;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LendingapprovalForm {

    private LendingApp lendingApp;

    private String username;
    
    private List<ApplyingBookForm> applyingBookFormList;

    public void setApplyingBookFormList(List<LendingBook> lendingBookList) {
        if (lendingBookList == null) {
            this.applyingBookFormList = null;
        }
        else {
            this.applyingBookFormList = lendingBookList.stream()
                    .map(ApplyingBookForm::new)
                    .collect(Collectors.toList());
        }
    }
    
}
