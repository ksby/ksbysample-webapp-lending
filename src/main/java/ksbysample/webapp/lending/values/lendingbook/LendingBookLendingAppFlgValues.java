package ksbysample.webapp.lending.values.lendingbook;

import ksbysample.webapp.lending.values.Values;
import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("MissingOverride")
@Getter
@AllArgsConstructor
public enum LendingBookLendingAppFlgValues implements Values {

    NOT_APPLY("", "しない")
    , APPLY("1", "する");

    private final String value;
    private final String text;

}
