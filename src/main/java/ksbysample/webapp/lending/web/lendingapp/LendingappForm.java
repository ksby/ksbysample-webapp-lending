package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LendingappForm {

    private LendingApp lendingApp;

    private String btn;

    @Valid
    private List<LendingBookDto> lendingBookDtoList;

    public void setLendingBookList(List<LendingBook> lendingBookList) {
        this.lendingBookDtoList = lendingBookList.stream()
                .map(LendingBookDto::new)
                .collect(Collectors.toList());
    }

}
