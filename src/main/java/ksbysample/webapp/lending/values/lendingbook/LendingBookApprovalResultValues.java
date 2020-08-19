package ksbysample.webapp.lending.values.lendingbook;

import ksbysample.webapp.lending.values.Values;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ???
 */
@SuppressWarnings("MissingOverride")
@Getter
@AllArgsConstructor
public enum LendingBookApprovalResultValues implements Values {

    APPROVAL("1", "承認")
    , REJECT("2", "却下");

    private final String value;
    private final String text;

}
