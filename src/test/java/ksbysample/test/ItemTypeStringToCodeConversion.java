package ksbysample.test;

import com.univocity.parsers.conversions.Conversion;
import lombok.Getter;

public class ItemTypeStringToCodeConversion implements Conversion<String, String> {
    
    public ItemTypeStringToCodeConversion(String... args) {
    }

    @Override
    public String execute(String input) {
        return ItemTypeValues.getValue(input);
    }

    @Override
    public String revert(String input) {
        return ItemTypeValues.getText(input);
    }

    @Getter
    public enum ItemTypeValues {
        NORMAL_ITEM("0001", "通常商品")
        , SENDFROMMAKER_ITEM("0002", "メーカー直送商品")
        , TEST_ITEM("9999", "テスト商品");

        private final String value;
        private final String text;

        ItemTypeValues(String value, String text) {
            this.value = value;
            this.text = text;
        }

        public static String getValue(String text) {
            String result = "";
            for (ItemTypeValues val : ItemTypeValues.values()) {
                if (val.getText().equals(text)) {
                    result = val.getValue();
                }
            }

            return result;
        }

        public static String getText(String value) {
            String result = "";
            for (ItemTypeValues val : ItemTypeValues.values()) {
                if (val.getValue().equals(value)) {
                    result = val.getText();
                }
            }

            return result;
        }
    }
}
