package ksbysample.webapp.lending.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LendingBookApprovalResultValues implements Values {

    APPROVAL("1", "承認")
    , REJECT("2", "却下");

    private final String value;
    private final String text;

}
