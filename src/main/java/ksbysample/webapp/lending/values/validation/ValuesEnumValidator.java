package ksbysample.webapp.lending.values.validation;

import ksbysample.webapp.lending.values.Values;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

public class ValuesEnumValidator implements ConstraintValidator<ValuesEnum, String> {

    private Class<? extends Enum<?>> enumClass;

    private boolean allowEmpty;

    @Override
    public void initialize(ValuesEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        this.allowEmpty = constraintAnnotation.allowEmpty();

        // enumClass 属性に Values インターフェースを実装していない列挙型が指定されている場合にはエラーにする
        try {
            if (!Values.class.isAssignableFrom(Class.forName(this.enumClass.getName()))) {
                throw new RuntimeException(
                        MessageFormat.format("enumClass 属性に Values インターフェースを実装した列挙型が指定されていません ( {0} )"
                                , this.enumClass.getName()));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return isValueCheck(value);
    }

    private boolean isValueCheck(String value) {
        boolean result = false;

        if (StringUtils.isBlank(value)) {
            if (this.allowEmpty) {
                result = true;
            }
        }
        else {
            Values[] valuesList = (Values[]) this.enumClass.getEnumConstants();
            for (Values values : valuesList) {
                if (StringUtils.equals(value, values.getValue())) {
                    result = true;
                }
            }
        }

        return result;
    }

}
