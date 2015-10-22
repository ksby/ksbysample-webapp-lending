package ksbysample.webapp.lending.web.booklist;

import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Data
public class RegisterBooklistForm {

    private List<RegisterBooklistRow> registerBooklistRowList;

    private Long lendingAppId;

    public RegisterBooklistForm() {
    }
    
    public RegisterBooklistForm(List<LendingBook> lendingBookList, Long lendingAppId) {
        this.registerBooklistRowList = lendingBookList.stream()
                .map(RegisterBooklistRow::new)
                .collect(Collectors.toList());
        this.lendingAppId = lendingAppId;
    }

    @Data
    public class RegisterBooklistRow {
        private String isbn;
        private String bookName;

        public RegisterBooklistRow(LendingBook lendingBook) {
            BeanUtils.copyProperties(lendingBook, this);
        }
    }

}