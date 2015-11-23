package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
public class LendingappForm {

    private LendingApp lendingApp;

    @Valid
    private List<LendingBookDto> lendingBookDtoList;

    public void setLendingBookList(List<LendingBook> lendingBookList) {
        this.lendingBookDtoList = new ArrayList<>();
        lendingBookList.stream()
                .forEach(lendingBook -> {
                    LendingBookDto lendingBookDto = new LendingBookDto(lendingBook);
                    this.lendingBookDtoList.add(lendingBookDto);
                });
    }

}
