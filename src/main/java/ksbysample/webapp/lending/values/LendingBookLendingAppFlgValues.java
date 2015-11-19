package ksbysample.webapp.lending.values;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum LendingBookLendingAppFlgValues implements Values {

    NOT_APPLY("", "しない")
    , APPLY("1", "する");

    private final String value;
    private final String text;

    LendingBookLendingAppFlgValues(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public static String getText(String value) {
        String result = "";
        for (Values val : LendingBookLendingAppFlgValues.values()) {
            if (StringUtils.equals(val.getValue(), value)) {
                result = val.getText();
            }
        }

        return result;
    }

}
