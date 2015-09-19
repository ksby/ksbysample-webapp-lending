package ksbysample.test;

import com.univocity.parsers.conversions.Conversion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTimeConversion implements Conversion<String, LocalDateTime> {

    public StringToLocalDateTimeConversion(String... args) {
    }
    
    @Override
    public LocalDateTime execute(String input) {
        LocalDateTime result = null;
        if (input != null) {
            result = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        }
        return result;
    }

    @Override
    public String revert(LocalDateTime input) {
        String result = null;
        if (input != null) {
            result = input.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        }
        return result;
    }

}
