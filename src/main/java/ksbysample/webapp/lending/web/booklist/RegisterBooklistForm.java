package ksbysample.webapp.lending.web.booklist;

import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ???
 */
@Data
public class RegisterBooklistForm {

    private List<RegisterBooklistRow> registerBooklistRowList;

    private Long lendingAppId;

    /**
     *
     */
    public RegisterBooklistForm() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    /**
     * @param lendingBookList ???
     * @param lendingAppId    ???
     */
    public RegisterBooklistForm(List<LendingBook> lendingBookList, Long lendingAppId) {
        this.registerBooklistRowList = lendingBookList.stream()
                .map(RegisterBooklistRow::new)
                .collect(Collectors.toList());
        this.lendingAppId = lendingAppId;
    }

    /**
     *
     */
    @Data
    public static class RegisterBooklistRow {
        private String isbn;
        private String bookName;

        /**
         * @param lendingBook ???
         */
        public RegisterBooklistRow(LendingBook lendingBook) {
            BeanUtils.copyProperties(lendingBook, this);
        }
    }

}
