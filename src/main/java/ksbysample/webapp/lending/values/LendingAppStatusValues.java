package ksbysample.webapp.lending.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LendingAppStatusValues implements Values {

    TENPORARY_SAVE("1", "一時保存")
    , UNAPPLIED("2", "未申請")
    , PENDING("3", "申請中")
    , APPLOVED("4", "承認済");

    private final String value;
    private final String text;

}
