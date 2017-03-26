package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.common.test.helper.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.yaml.snakeyaml.Yaml;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LendingappFormValidatorTest {

    // テストデータ
    private LendingappForm lendingappForm_001
            = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_001.yaml"));
    private LendingappForm lendingappForm_002
            = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_002.yaml"));
    private LendingappForm lendingappForm_003
            = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_003.yaml"));
    private LendingappForm lendingappForm_004
            = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_004.yaml"));

    @Autowired
    private LendingappFormValidator lendingappFormValidator;

    @Test
    public void testValidate_一時保存ボタン押下時は入力チェックは行われない() throws Exception {
        Errors errors = TestHelper.createErrors();
        lendingappFormValidator.validate(lendingappForm_001, errors);
        assertThat(errors.hasFieldErrors()).isFalse();
    }

    @Test
    public void testValidate_１つも申請するが選択されていない場合はエラーになる() throws Exception {
        Errors errors = TestHelper.createErrors();
        lendingappFormValidator.validate(lendingappForm_002, errors);
        assertThat(errors.hasFieldErrors()).isTrue();
        assertThat(errors.getFieldErrorCount()).isEqualTo(3);
        assertThat(errors.getFieldErrors())
                .extracting(FieldError::getField)
                .containsOnly("lendingBookDtoList[0].lendingAppFlg"
                        , "lendingBookDtoList[1].lendingAppFlg"
                        , "lendingBookDtoList[2].lendingAppFlg");
    }

    @Test
    public void testValidate_申請するを選択して申請理由を入力していない場合はエラーになる() throws Exception {
        Errors errors = TestHelper.createErrors();
        lendingappFormValidator.validate(lendingappForm_003, errors);
        assertThat(errors.hasFieldErrors()).isTrue();
        assertThat(errors.getFieldErrorCount()).isEqualTo(2);
        assertThat(errors.getFieldErrors())
                .extracting(FieldError::getField)
                .containsOnly("lendingBookDtoList[0].lendingAppReason"
                        , "lendingBookDtoList[2].lendingAppReason");
    }

    @Test
    public void testValidate_申請するを選択して申請理由も入力している場合はエラーにならない() throws Exception {
        Errors errors = TestHelper.createErrors();
        lendingappFormValidator.validate(lendingappForm_004, errors);
        assertThat(errors.hasFieldErrors()).isFalse();
    }

}