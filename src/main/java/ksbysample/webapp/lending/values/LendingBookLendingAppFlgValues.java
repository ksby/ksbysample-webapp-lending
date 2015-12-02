package ksbysample.webapp.lending.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LendingBookLendingAppFlgValues implements Values {

    NOT_APPLY("", "しない")
    , APPLY("1", "する");

    private final String value;
    private final String text;

}
