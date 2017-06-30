package ksbysample.webapp.lending.helper.download.booklistcsv;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

/**
 * ???
 */
@Data
public class BookListCsvData {

    @Parsed(field = "ISBN")
    private String isbn;

    @Parsed(field = "書名")
    private String bookName;

    @Parsed(field = "申請理由")
    private String lendingAppReason;

    @Parsed(field = "承認／却下")
    private String approvalResultStr;

    @Parsed(field = "却下理由")
    private String approvalReason;

}
