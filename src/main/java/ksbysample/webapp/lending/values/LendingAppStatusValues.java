package ksbysample.webapp.lending.values;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum LendingAppStatusValues implements Values {

    TENPORARY_SAVE("1", "一時保存")
    , UNAPPLIED("2", "未申請")
    , PENDING("3", "申請中")
    , APPLOVED("4", "承認済");

    private final String value;
    private final String text;

    LendingAppStatusValues(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public static String getText(String value) {
        String result = "";
        for (LendingAppStatusValues val : LendingAppStatusValues.values()) {
            if (StringUtils.equals(val.getValue(), value)) {
                result = val.getText();
            }
        }

        return result;
    }

}
