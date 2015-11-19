package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import lombok.Data;

import java.util.List;

@Data
public class LendingappForm {

    private LendingApp lendingApp;

    private List<LendingBook> lendingBookList;
    
}
