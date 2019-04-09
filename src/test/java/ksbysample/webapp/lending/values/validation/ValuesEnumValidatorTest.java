package ksbysample.webapp.lending.values.validation;

import ksbysample.webapp.lending.values.Values;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ValuesEnumValidatorTest {

    // テスト用 Value 列挙型
    @SuppressWarnings("MissingOverride")
    @Getter
    @AllArgsConstructor
    private enum TestValues implements Values {
        FIRST("1", "１番目"), SECOND("2", "２番目"), THIRD("3", "３番目");

        private final String value;
        private final String text;
    }

    // テスト用 POJO クラス
    @Data
    private static class NotAllowEmptyTestClass {
        @ValuesEnum(enumClass = TestValues.class)
        private String testStr;
    }

    // テスト用 POJO クラス
    @Data
    private static class AllowEmptyTestClass {
        @ValuesEnum(enumClass = TestValues.class, allowEmpty = true)
        private String testStr;
    }

    @Test
    void ValuesEnumBeanValidationTest_AllowEmpty_False() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        NotAllowEmptyTestClass notAllowEmptyTestClass = new NotAllowEmptyTestClass();

        // null の場合にはエラーにはエラーは発生しない ( チェックが実行されない )
        notAllowEmptyTestClass.setTestStr(null);
        Set<ConstraintViolation<NotAllowEmptyTestClass>> constraintViolations = validator.validate(notAllowEmptyTestClass);
        assertThat(constraintViolations).hasSize(0);

        // Value に定義されている値の場合にはエラーは発生しない
        notAllowEmptyTestClass.setTestStr("1");
        constraintViolations = validator.validate(notAllowEmptyTestClass);
        assertThat(constraintViolations).hasSize(0);

        // Value に定義されていない値の場合にはエラーが発生する
        notAllowEmptyTestClass.setTestStr("4");
        constraintViolations = validator.validate(notAllowEmptyTestClass);
        assertThat(constraintViolations).hasSize(1);

        // Value に定義されている値が含まれていてもエラーが発生する
        notAllowEmptyTestClass.setTestStr("2test");
        constraintViolations = validator.validate(notAllowEmptyTestClass);
        assertThat(constraintViolations).hasSize(1);

        // Text に定義されている値の場合にはエラーが発生する
        notAllowEmptyTestClass.setTestStr("３番目");
        constraintViolations = validator.validate(notAllowEmptyTestClass);
        assertThat(constraintViolations).hasSize(1);

        // 空文字列の場合にはエラーが発生する
        notAllowEmptyTestClass.setTestStr("");
        constraintViolations = validator.validate(notAllowEmptyTestClass);
        assertThat(constraintViolations).hasSize(1);
    }

    @Test
    void ValuesEnumBeanValidationTest_AllowEmpty_True() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        AllowEmptyTestClass allowEmptyTestClass = new AllowEmptyTestClass();

        // null の場合にはエラーにはエラーは発生しない ( チェックが実行されない )
        allowEmptyTestClass.setTestStr(null);
        Set<ConstraintViolation<AllowEmptyTestClass>> constraintViolations = validator.validate(allowEmptyTestClass);
        assertThat(constraintViolations).hasSize(0);

        // Value に定義されている値の場合にはエラーは発生しない
        allowEmptyTestClass.setTestStr("1");
        constraintViolations = validator.validate(allowEmptyTestClass);
        assertThat(constraintViolations).hasSize(0);

        // Value に定義されていない値の場合にはエラーが発生する
        allowEmptyTestClass.setTestStr("4");
        constraintViolations = validator.validate(allowEmptyTestClass);
        assertThat(constraintViolations).hasSize(1);

        // Value に定義されている値が含まれていてもエラーが発生する
        allowEmptyTestClass.setTestStr("2test");
        constraintViolations = validator.validate(allowEmptyTestClass);
        assertThat(constraintViolations).hasSize(1);

        // Text に定義されている値の場合にはエラーが発生する
        allowEmptyTestClass.setTestStr("３番目");
        constraintViolations = validator.validate(allowEmptyTestClass);
        assertThat(constraintViolations).hasSize(1);

        // 空文字列の場合にはエラーは発生しない
        allowEmptyTestClass.setTestStr("");
        constraintViolations = validator.validate(allowEmptyTestClass);
        assertThat(constraintViolations).hasSize(0);
    }

}
