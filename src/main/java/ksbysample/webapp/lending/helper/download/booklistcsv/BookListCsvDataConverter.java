package ksbysample.webapp.lending.helper.download.booklistcsv;

import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.values.lendingbook.LendingBookApprovalResultValues;
import ksbysample.webapp.lending.values.ValuesHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookListCsvDataConverter {

    @Autowired
    private ValuesHelper vh;

    public List<BookListCsvData> convertFrom(List<LendingBook> lendingBookList) {
        List<BookListCsvData> bookListCsvDataList = null;
        if (lendingBookList != null) {
            bookListCsvDataList = lendingBookList.stream()
                    .map(lendingBook -> {
                        BookListCsvData bookListCsvData = new BookListCsvData();
                        BeanUtils.copyProperties(lendingBook, bookListCsvData);
                        bookListCsvData.setApprovalResultStr(
                                vh.getText(LendingBookApprovalResultValues.class
                                        , lendingBook.getApprovalResult()));
                        return bookListCsvData;
                    })
                    .collect(Collectors.toList());
        }
        return bookListCsvDataList;
    }

}
